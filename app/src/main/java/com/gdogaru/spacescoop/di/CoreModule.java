/*
 * Copyright (c) 2019 Gabriel Dogaru - gdogaru@gmail.com
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

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
