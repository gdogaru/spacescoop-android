package com.gdogaru.spacescoop.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;

import com.gdogaru.spacescoop.SpaceScoopApp;
import com.gdogaru.spacescoop.controllers.AnalyticsHelper;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.inject.Singleton;

import lombok.SneakyThrows;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

import static com.gdogaru.spacescoop.controllers.AnalyticsHelper.Event.ACTION_PICTURE_LARGE_PROCESSING;
import static com.gdogaru.spacescoop.controllers.AnalyticsHelper.Event.ACTION_PICTURE_LOAD;
import static com.gdogaru.spacescoop.controllers.AnalyticsHelper.Event.ACTION_PICTURE_PROCESSING;
import static com.gdogaru.spacescoop.view.main.scoops.ScoopsFragment.GRID_COLUMNS;

/**
 * Created by Gabriel Dogaru (gdogaru@gmail.com)
 */

@Singleton
public class CachingDownloader {
    private static final int SOCKET_OPERATION_TIMEOUT = 10000;
    private static final int MAX_RESOLUTION = 700 * 700;
    private final Context appContext;
    private DiskAdapter diskAdapter;
    private AnalyticsHelper analyticsHelper;
    private double THUMBNAIL_WIDTH = 150;
    private double SCREEN_WIDTH = 720;

    @Inject
    public CachingDownloader(Context appContext, DiskAdapter diskAdapter, AnalyticsHelper analyticsHelper) {
        this.appContext = appContext;
        this.diskAdapter = diskAdapter;
        this.analyticsHelper = analyticsHelper;
        initWidths();
    }

    private void initWidths() {
        DisplayMetrics displayMetrics = appContext.getResources().getDisplayMetrics();
        SCREEN_WIDTH = Math.round(Math.min(displayMetrics.widthPixels, displayMetrics.heightPixels) * 1.0); //scale
        THUMBNAIL_WIDTH = Math.min(SCREEN_WIDTH / GRID_COLUMNS, dpToPx(50));
    }

    private int dpToPx(int dp) {
        DisplayMetrics displayMetrics = appContext.getResources().getDisplayMetrics();
        return (int) ((dp * displayMetrics.density) + 0.5);
    }

//    public Future<Bitmap> retrieveBitmap(DownloadableImage dimage, ImageSize size) {
//        return executorService.submit(new DownloadImageCallable(dimage, size));
//    }

    public Bitmap retrieveBitmap(DownloadableImage dimage, ImageSize size) {
        Bitmap bmp = getBitmapFromDisk(dimage, size);
        if (bmp != null) {
            return bmp;
        } else {
            downloadBitmap(dimage);
            return getBitmapFromDisk(dimage, size);
        }
    }

    public void downloadThumbnail(DownloadableImage dimage) {
        DownloadableImage thumbImage = ImageUtil.toThumb(dimage);
        long time = System.currentTimeMillis();
        Bitmap bmp = downloadHttpBitmapRetrying(thumbImage.getUrl());
        analyticsHelper.logTime("Image Download", System.currentTimeMillis() - time);
        if (bmp != null) {
            writeToDisk(thumbImage, bmp, ImageSize.THUMB);
        } else {
            downloadBitmap(dimage);
        }
    }

    public void downloadBitmap(DownloadableImage dimage) {
        long time = System.currentTimeMillis();
        if (!downloadHttpBitmapToDisk(dimage)) {
            return;
        }
        long downloadTime = System.currentTimeMillis() - time;
        if (SpaceScoopApp.DEBUG_TIMES) Timber.d("Image downloaded in: %s millis", downloadTime);

        analyticsHelper.logEvent(ACTION_PICTURE_LOAD, downloadTime);

        time = System.currentTimeMillis();

        Bitmap bmp = getBitmapFromDisk(dimage, ImageSize.FULL);

        if (bmp == null) {
            Timber.e("Could not load image from disk: %s", dimage.getUrl());
            return;
        }

//        Bitmap scaledBitmap = bmp.getWidth() * bmp.getHeight() < MAX_RESOLUTION
//                ? bmp
//                : Bitmap.createScaledBitmap(bmp, (int) SCREEN_WIDTH, (int) (bmp.getHeight() * SCREEN_WIDTH / bmp.getWidth()), false);
//        writeToDisk(dimage, scaledBitmap, ImageSize.FULL);

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bmp, (int) THUMBNAIL_WIDTH, (int) (bmp.getHeight() * THUMBNAIL_WIDTH / bmp.getWidth()), false);
        writeToDisk(dimage, scaledBitmap, ImageSize.THUMB);

        downloadTime = System.currentTimeMillis() - time;
        if (SpaceScoopApp.DEBUG_TIMES) Timber.d("Image scaled in: %s millis", downloadTime);
        analyticsHelper.logEvent(ACTION_PICTURE_PROCESSING, downloadTime);

    }

    public void downloadLargeBitmap(DownloadableImage dimage) {
        long time = System.currentTimeMillis();
        Bitmap bmp = null;
        try {
            bmp = downloadHttpBitmap(dimage.getUrl());
        } catch (Exception e) {
            return;
        }
        long downloadTime = System.currentTimeMillis() - time;
        if (SpaceScoopApp.DEBUG_TIMES)
            Timber.d("Large image downloaded in: %s millis", downloadTime);
        analyticsHelper.logEvent(ACTION_PICTURE_LOAD, downloadTime);

        time = System.currentTimeMillis();

        Bitmap scaledBitmap = bmp.getWidth() * bmp.getHeight() < MAX_RESOLUTION
                ? bmp
                : Bitmap.createScaledBitmap(bmp, (int) SCREEN_WIDTH, (int) (bmp.getHeight() * SCREEN_WIDTH / bmp.getWidth()), false);

        writeToDisk(dimage, scaledBitmap, ImageSize.FULL);
        downloadTime = System.currentTimeMillis() - time;
        if (SpaceScoopApp.DEBUG_TIMES) Timber.d("Large image scaled in: %s millis", downloadTime);
        analyticsHelper.logEvent(ACTION_PICTURE_LARGE_PROCESSING, downloadTime);

    }

    private void writeToDisk(DownloadableImage dimg, Bitmap bmp, ImageSize size) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 90, stream);
        byte[] byteArray = stream.toByteArray();
        diskAdapter.write(byteArray, createCachedFilename(dimg, size));
    }

    @SneakyThrows
    private Bitmap getBitmapFromDisk(DownloadableImage dimage, ImageSize size) {
        long time = System.currentTimeMillis();
        String fileName = createCachedFilename(dimage, size);
        if (diskAdapter.contains(fileName)) {
            byte[] data = diskAdapter.read(fileName);
            if (SpaceScoopApp.DEBUG_TIMES) {
                Timber.d("Image %s served from disk in: %s", size.name(), (System.currentTimeMillis() - time));
            }
            return BitmapFactory.decodeByteArray(data, 0, data.length);
        } else {
            return null;
        }
    }

    private String createCachedFilename(DownloadableImage dimg, ImageSize size) {
        String[] split = dimg.getUrl().split("/");
        String imageName = split[split.length - 1];
        return String.format("%s_%s", size.name(), imageName);
    }

    private Bitmap downloadHttpBitmapRetrying(String url) {
        int count = 0;
        Bitmap bmp = null;
        do {
            try {
                bmp = downloadHttpBitmap(url);
            } catch (Exception e) {
            }
            count++;
        } while (bmp == null && count <= 3);
        return bmp;

    }

    @SneakyThrows
    private Bitmap downloadHttpBitmap(String strurl) {
        if (strurl == null || strurl.trim().isEmpty()) {
            return null;
        }
        URL url = new URL(strurl);
//        URLConnection urlConnection = url.openConnection();
//        urlConnection.setReadTimeout(SOCKET_OPERATION_TIMEOUT);
//        InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
        try {
            BitmapFactory.Options opts = getBitmapOptions();
            byte[] data = IOUtils.toByteArray(url);
            return BitmapFactory.decodeByteArray(data, 0, data.length, opts);
        } finally {
//            IOUtils.closeQuietly(inputStream);
        }
    }

    @SneakyThrows
    private boolean downloadHttpBitmapToDisk(DownloadableImage dimg) {
        if (dimg.getUrl() == null || dimg.getUrl().trim().isEmpty()) {
            return false;
        }

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(dimg.getUrl())
                .build();

        try (Response response = client.newCall(request).execute()) {
            byte[] data = response.body().bytes();
            diskAdapter.write(data, createCachedFilename(dimg, ImageSize.FULL));
            return true;
        }
    }

    private BitmapFactory.Options getBitmapOptions() {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inPreferQualityOverSpeed = false;
        opts.inPurgeable = true;
        opts.inInputShareable = true;
        opts.inPreferredConfig = Bitmap.Config.RGB_565;
        return opts;
    }

    public boolean hasInCache(DisplayingImage img) {
        String path = createCachedFilename(img.getImage(), img.getSize());
        return diskAdapter.contains(path);
    }

    class DownloadImageCallable implements Callable<Bitmap> {

        private DownloadableImage dimage;
        private ImageSize size;

        DownloadImageCallable(DownloadableImage dimage, ImageSize size) {
            this.dimage = dimage;
            this.size = size;
        }

        @Override
        public Bitmap call() {
            Bitmap bmp = getBitmapFromDisk(dimage, size);
            if (bmp != null) {
                return bmp;
            } else {
                downloadBitmap(dimage);
                return getBitmapFromDisk(dimage, size);
            }
        }
    }
}
