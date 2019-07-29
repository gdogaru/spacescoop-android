package com.gdogaru.spacescoop.cache;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import com.gdogaru.spacescoop.controllers.ImageDownloader;
import com.google.android.gms.common.util.concurrent.NamedThreadFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DiskImageDownloader implements ImageDownloader {

    private CachingDownloader cachingDownloader;
    private MemoryCache memoryCache = new MemoryCache();
    private ExecutorService executorService;
    private Handler uiHandler = new Handler(Looper.getMainLooper());

    @Inject
    public DiskImageDownloader(CachingDownloader cachingDownloader) {
        this.cachingDownloader = cachingDownloader;
        executorService = Executors.newCachedThreadPool(new NamedThreadFactory("sio"));
    }

    @Override
    public void display(String url, ImageView newsImage) {
        display(new DisplayingImage(new DownloadableImage(url), ImageSize.FULL, newsImage));
    }

    @Override
    public void displayThumb(String url, ImageView newsImage) {
        display(new DisplayingImage(new DownloadableImage(url), ImageSize.THUMB, newsImage));
    }

    @Override
    public void downloadAndPrepare(String url) {
        cachingDownloader.downloadBitmap(new DownloadableImage(url));
    }

    public void display(final DisplayingImage img) {
        String url = img.getImage().getUrl();
        if (url.equals(img.getImageView().getTag())) {
            img.getImageView().post(() -> img.getImageView().invalidate());
            return;
        }
        img.getImageView().setTag(url);
        img.getImageView().setImageDrawable(null);

        Bitmap bitmap = img.isMemoryCacheable() ? memoryCache.get(img.getImage()) : null;
        if (bitmap != null) {
            uiHandler.post(new BitmapDisplayer(bitmap, img));
        } else {
            queuePhoto(img);
        }
    }

    private void setThumbImage(DisplayingImage img) {
        DownloadableImage thumbImg = ImageUtil.toThumb(img.getImage());
        Bitmap bmp = memoryCache.get(thumbImg);
        if (bmp == null) {
            bmp = cachingDownloader.retrieveBitmap(thumbImg, ImageSize.THUMB);
        }
        if (bmp != null) {
            uiHandler.post(new BitmapDisplayer(bmp, img));
        }
    }

    private void queuePhoto(DisplayingImage dimg) {
        executorService.submit(new PhotosLoader(dimg));
    }

    boolean imageViewReused(DisplayingImage photoToLoad) {
        String tag = photoToLoad != null ? photoToLoad.getImage().getUrl() : null;
        return tag == null || photoToLoad.getImageView() == null || !tag.equals((photoToLoad.getImageView().getTag().toString()));
    }

    class PhotosLoader extends Thread {
        DisplayingImage photoToLoad;

        public PhotosLoader(DisplayingImage photoToLoad) {
            this.photoToLoad = photoToLoad;
        }

        @Override
        public void run() {
            if (imageViewReused(photoToLoad))
                return;
            Bitmap bitmap = null;
            if (photoToLoad.isMemoryCacheable()) {
                bitmap = memoryCache.get(photoToLoad.getImage());
            }
            if (bitmap == null) {
                bitmap = cachingDownloader.retrieveBitmap(photoToLoad.getImage(), photoToLoad.getSize());
            }
            if (bitmap != null) {
                if (photoToLoad.isMemoryCacheable()) {
                    memoryCache.put(photoToLoad.getImage(), bitmap);
                }
            }
            if (imageViewReused(photoToLoad)) {
                return;
            }
            uiHandler.post(new BitmapDisplayer(bitmap, photoToLoad));
        }
    }

    /**
     * Used to display bitmap in the UI thread
     */
    class BitmapDisplayer extends Thread {
        private final Bitmap bitmap;
        private final DisplayingImage photoToLoad;

        private BitmapDisplayer(Bitmap b, DisplayingImage p) {
            bitmap = b;
            photoToLoad = p;
        }

        public void run() {
            if (imageViewReused(photoToLoad)) return;

            ImageView imageView = photoToLoad.getImageView();
            if (bitmap != null && !imageViewReused(photoToLoad)) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }

}
