package com.gdogaru.spacescoop.view.article;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;

import com.evernote.android.state.State;
import com.gdogaru.spacescoop.R;
import com.gdogaru.spacescoop.controllers.ImageDownloader;
import com.gdogaru.spacescoop.db.NewsDao;
import com.gdogaru.spacescoop.db.model.Article;
import com.gdogaru.spacescoop.events.PageChangedEvent;
import com.gdogaru.spacescoop.util.FormatUtil;
import com.gdogaru.spacescoop.util.HtmlHelper;
import com.gdogaru.spacescoop.view.common.BaseFragment;
import com.squareup.otto.Subscribe;

import net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArticleItemFragment extends BaseFragment {

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.dateText)
    TextView dateText;
    @BindView(R.id.newsImage)
    ImageView newsImage;

    @State
    Article article;
    @State
    Long articleId;

    @Inject
    ImageDownloader imageDownloader;
    @Inject
    NewsDao newsDao;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.articles_item, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        AppCompatActivity activity = (AppCompatActivity) requireActivity();
        Typeface tf = ResourcesCompat.getFont(activity, R.font.signika_regular);
        collapsingToolbarLayout.setCollapsedTitleTypeface(tf);
        collapsingToolbarLayout.setExpandedTitleTypeface(tf);

        webView.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
        webView.getSettings().setJavaScriptEnabled(true);

        if (article == null) {
            newsDao.queryForId(articleId).observe(this, result -> {
                article = result;
                displayArticle(article);
            });
        } else {
            displayArticle(article);
        }
    }

    private void displayArticle(Article article) {
        toolbar.setTitle(article.getTitle());

        newsImage.setOnClickListener(v -> FullImageActivity.start(requireActivity(), article.getHeadImageUrl()));

        dateText.setText(FormatUtil.formatForUI(article.getPublishDate(), "en"));
        imageDownloader.display(article.getHeadImageUrl(), newsImage);


        String data = HtmlHelper.asHtmlPage(article.getText(), isLandscapeTablet());
        webView.loadDataWithBaseURL("file:///android_asset/", data, "text/html", "utf-8", null);
    }

    @Subscribe
    public void onPageChanged(PageChangedEvent e) {
        if (e.getId() == articleId) {
            setActivityTitle();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.article_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.share) {
            Intent share = new Intent(android.content.Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_TEXT, article.getLink());

            startActivity(Intent.createChooser(share, "Share scoop!"));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void setActivityTitle() {
        AppCompatActivity activity = (AppCompatActivity) requireActivity();
        activity.setSupportActionBar(toolbar);
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (article != null) toolbar.setTitle(article.getTitle());
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public Article getArticle() {
        return article;
    }


    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public Long getArticleId() {
        return articleId;
    }
}
