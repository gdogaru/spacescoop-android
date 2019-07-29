package com.gdogaru.spacescoop.di;


import android.content.Context;
import android.os.Environment;

import com.gdogaru.spacescoop.cache.DiskAdapter;
import com.gdogaru.spacescoop.cache.DiskImageDownloader;
import com.gdogaru.spacescoop.cache.ExtStorageDiskAdapter;
import com.gdogaru.spacescoop.cache.IntStorageDiskAdapter;
import com.gdogaru.spacescoop.controllers.ImageDownloader;
import com.gdogaru.spacescoop.events.MainThreadBus;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class CoreModule {

//    @Provides
//    @Singleton
//    public Gson gson() {
//        return new Gson();
//    }

    @Provides
    @Singleton
    public FirebaseAnalytics analytics(Context context) {
        return FirebaseAnalytics.getInstance(context);
    }

    @Provides
    @Singleton
    public Bus bus() {
        return new MainThreadBus();
    }

    @Provides
    @Singleton
    public DiskAdapter diskAdapter(Context context) {
        return isExternalStorageWritable() ? new ExtStorageDiskAdapter(context) : new IntStorageDiskAdapter(context);
    }

    @Provides
    @Singleton
    public ImageDownloader provideImageDownloader(DiskImageDownloader loader) {
        return loader;
    }

    /* Checks if external storage is available for read and write */
    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }
}
