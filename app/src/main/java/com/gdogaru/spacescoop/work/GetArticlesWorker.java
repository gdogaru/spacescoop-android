package com.gdogaru.spacescoop.work;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.gdogaru.spacescoop.alarm.AlarmHelper;
import com.gdogaru.spacescoop.api.ApiResponse;
import com.gdogaru.spacescoop.api.SpacescoopClient;
import com.gdogaru.spacescoop.controllers.AnalyticsHelper;
import com.gdogaru.spacescoop.controllers.AppSettingsController;
import com.gdogaru.spacescoop.controllers.ImageDownloader;
import com.gdogaru.spacescoop.db.NewsDao;
import com.gdogaru.spacescoop.db.model.Article;
import com.gdogaru.spacescoop.util.HtmlHelper;
import com.gdogaru.spacescoop.util.Preconditions;
import com.gdogaru.spacescoop.work.di.ChildWorkerFactory;
import com.rometools.rome.feed.synd.SyndEnclosure;
import com.rometools.rome.feed.synd.SyndEntry;
import com.squareup.inject.assisted.Assisted;
import com.squareup.inject.assisted.AssistedInject;
import com.squareup.otto.Bus;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import timber.log.Timber;

public class GetArticlesWorker extends Worker {
    public static final int MAX_LOAD = 50;
    private static final int MAX_SVC_RETRY = 3;
    public static final String PARAM_LANG = "LANG";
    public static final String PARAM_UPDATE_NEW = "UPDATE_NEW";
    public static final String PARAM_NOTIFY = "NOTIFY";


    private final Bus bus;
    private final AppSettingsController settingsController;
    private final ImageDownloader imageDownloader;
    private final NewsDao newsDao;
    private final AnalyticsHelper analyticsHelper;
    private final SpacescoopClient spacescoopClient;
    private final String lang;
    private final boolean updateNew;
    private final boolean notifyOnNew;

    @AssistedInject.Factory
    public interface Factory extends ChildWorkerFactory {
    }

    @AssistedInject
    public GetArticlesWorker(@Assisted @NonNull Context appContext,
                             @Assisted @NonNull WorkerParameters workerParams,
                             Bus bus, AppSettingsController settingsController, ImageDownloader imageDownloader, NewsDao newsDao, AnalyticsHelper analyticsHelper, SpacescoopClient spacescoopClient) {
        super(appContext, workerParams);
        this.bus = bus;
        this.settingsController = settingsController;
        this.imageDownloader = imageDownloader;
        this.newsDao = newsDao;
        this.analyticsHelper = analyticsHelper;
        this.spacescoopClient = spacescoopClient;

        lang = Preconditions.checkNotNull(workerParams.getInputData().getString(PARAM_LANG));
        updateNew = workerParams.getInputData().getBoolean(PARAM_UPDATE_NEW, true);
        notifyOnNew = workerParams.getInputData().getBoolean(PARAM_NOTIFY, false);
    }

    @NonNull
    @Override
    public Result doWork() {
        Timber.i("Running update start: %s lang: %s", updateNew, lang);

        if (!lang.equals(settingsController.getLanguage())) {
            Timber.d("Found other language, current: %s, found: %s", settingsController.getLanguage(), lang);
            return Result.failure();
        }
        if (!updateNew && settingsController.isEndReached()) {
            Timber.w("Found end reached flag, exiting");
            return Result.failure();
        }

        if (notifyOnNew) {
            analyticsHelper.logEvent(AnalyticsHelper.Event.UPDATES_CHECK, 1);
        }

        Date bdTimeLimit = updateNew ? newsDao.getNewestDate() : newsDao.getOldestDate();

        int page = updateNew ? -1 : settingsController.getPagesDownloadedNo();
        int updates = 0;
        List<SyndEntry> items;
        Timber.i("Starting news fetching...");
        do {
            page++;
            items = getItemsRetrying(page);
            if (items == null) {
                Timber.e("Got null page, exiting..");
                return Result.failure();
            }

            List<Article> articleItems = itemToNews(items);
            analyticsHelper.logEvent(AnalyticsHelper.Event.ACTION_ARTICLE_PROCESSING, articleItems.size());
            for (Article articleArticle : articleItems) {
                if (!lang.equals(settingsController.getLanguage())) {
                    Timber.w("Language changed, not saving page ");
                    return Result.failure();
                }
                boolean existing = newsDao.countByGuId(articleArticle.getGuid()) > 0;
                if (!existing) {
                    newsDao.save(articleArticle);
                    imageDownloader.downloadAndPrepare(articleArticle.getHeadImageUrl());
                    updates++;
                } else {
                    Timber.w("Found article with same id. Date condition: %s - db: %s ", articleArticle.getPublishDate().getTime(), bdTimeLimit);
                }
            }
            Timber.i("Got %s new items.", updates);
        } while (!items.isEmpty() && updates < MAX_LOAD && !(updateNew && updates == 0));

        if (page > settingsController.getPagesDownloadedNo()) {
            settingsController.setPagesDownloadedNo(page);
            if (items.isEmpty() && page > 0) {
                Timber.i("End reached for language: %s, page:%s", lang, page);
                settingsController.setEndReached(true);
                analyticsHelper.logEvent(AnalyticsHelper.Event.LIST_END_REACHED, 1);
            }
        }

        if (updates > 0 && notifyOnNew && settingsController.getNotifyUpdates()) {
            AlarmHelper.showNewItemsNotification(getApplicationContext());
            analyticsHelper.logEvent(AnalyticsHelper.Event.NEW_ITEMS_NOTIFY, 1);
        }

        Timber.i("Job execution finished.");
        return Result.success();
    }

    private List<SyndEntry> getItemsRetrying(int page) {
        List<SyndEntry> items = null;
        int count = 0;
        boolean hasError = false;
        do {
            long time = System.currentTimeMillis();
            try {
                ApiResponse<List<SyndEntry>> response = spacescoopClient.getRssItemsSync(lang, page);
                if (response.getStatus() == ApiResponse.Status.SUCCESS) {
                    items = response.getValue();
                } else {
                    hasError = true;
                }
            } catch (Throwable e) {
                Timber.w(e, "Error getting news");
                hasError = true;
            }
            long downTime = System.currentTimeMillis() - time;
            analyticsHelper.logTime("Page Download", downTime);
            if (items == null) items = new ArrayList<>();
            count++;
        } while (count < MAX_SVC_RETRY && hasError);
        return items;
    }


    private List<Article> itemToNews(List<SyndEntry> items) {
        List<Article> result = new ArrayList<>();
        for (SyndEntry item : items) {
            result.add(itemToNews(item));
        }
        return result;
    }

    private Article itemToNews(SyndEntry item) {
        Article article = new Article();
        article.setGuid(item.getTitle());
        article.setTitle(item.getTitle());
        article.setUrl(item.getUri());
        article.setLanguage(lang);
        article.setText(item.getDescription().getValue());
        article.setPreviewText(HtmlHelper.asHtmlPreviewPage(item.getDescription().getValue()));
        if (item.getPublishedDate() != null) {
            article.setPublishDate(item.getPublishedDate());
        } else {
            article.setPublishDate(new Date());
        }
        if (item.getEnclosures().size() > 0) {
            SyndEnclosure enclosure = item.getEnclosures().get(0);
            article.setHeadImageUrl(enclosure.getUrl());
        }
        article.setLink(item.getLink());
        return article;
    }


    private static String toSmallImage(String headImageUrl) {
        if (headImageUrl.contains("/screen/")) {
            String thumb = headImageUrl.replaceAll("/screen/", "/newsmini/");
            if (exists(thumb)) {
                return thumb;
            }
        }
        return headImageUrl + "#small";
    }

    public static boolean exists(String URLName) {
        try {
            HttpURLConnection.setFollowRedirects(false);
            // note : you may also need   HttpURLConnection.setInstanceFollowRedirects(false)
            HttpURLConnection con = (HttpURLConnection) new URL(URLName).openConnection();
            con.setRequestMethod("HEAD");
            return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
        } catch (Exception e) {
            return false;
        }
    }

}

