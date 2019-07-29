package com.gdogaru.spacescoop.cache;

import android.graphics.Bitmap;

import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MemoryCache {
    private Map<DownloadableImage, SoftReference<Bitmap>> cache = Collections.synchronizedMap(new HashMap<DownloadableImage, SoftReference<Bitmap>>());

    public Bitmap get(DownloadableImage img) {
        if (!cache.containsKey(img)) {
            return null;
        }
        SoftReference<Bitmap> ref = cache.get(img);
        return ref.get();
    }

    public void put(DownloadableImage img, Bitmap bitmap) {
        cache.put(img, new SoftReference<Bitmap>(bitmap));
    }

    public void clear() {
        cache.clear();
    }
}