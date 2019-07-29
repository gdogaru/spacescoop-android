package com.gdogaru.spacescoop.cache;

import android.widget.ImageView;

import java.lang.ref.WeakReference;

/**
 * Created by Gabriel Dogaru (gdogaru@gmail.com)
 */
public class DisplayingImage {
    private final DownloadableImage dimage;
    private final ImageSize size;

    private final WeakReference<ImageView> imageViewWeakReference;

    public DisplayingImage(DownloadableImage dimage, ImageSize size, ImageView imageView) {
        this.dimage = dimage;
        this.size = size;
        this.imageViewWeakReference = new WeakReference<ImageView>(imageView);
    }

    public DownloadableImage getImage() {
        return dimage;
    }

    public ImageView getImageView() {
        return imageViewWeakReference.get();
    }

    public ImageSize getSize() {
        return size;
    }

    public boolean isMemoryCacheable() {
        return size == ImageSize.THUMB;
    }
}
