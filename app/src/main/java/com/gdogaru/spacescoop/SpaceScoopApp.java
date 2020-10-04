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

package com.gdogaru.spacescoop;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Configuration;
import androidx.work.WorkManager;

import com.evernote.android.state.StateSaver;
import com.gdogaru.spacescoop.di.AppInjector;
import com.gdogaru.spacescoop.work.di.DaggerWorkerFactory;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasAndroidInjector;
import timber.log.Timber;

/**
 * @author Gabriel Dogaru (gdogaru@gmail.com)
 */
public class SpaceScoopApp extends Application implements HasAndroidInjector {
    public static final boolean DEBUG_TIMES = false;
    @Inject
    DaggerWorkerFactory workerFactory;

    @Inject
    DispatchingAndroidInjector<Object> DispatchingAndroidInjector;

    @Override
    public void onCreate() {
        super.onCreate();
        initTimber();
        Timber.d("Application.onCreate - Initializing application...");

        StateSaver.setEnabledForAllActivitiesAndSupportFragments(this, true);
        AppInjector.init(this);

        WorkManager.initialize(this, new Configuration.Builder().setWorkerFactory(workerFactory).build());

        Timber.d("Application.onCreate - Application initialized OK");
    }

    private void initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new CrashlyticsTree());
        }
    }

    @Override
    public AndroidInjector<Object> androidInjector() {
        return DispatchingAndroidInjector;
    }

}


class CrashlyticsTree extends Timber.Tree {

    @Override
    protected void log(int priority, String tag, @NonNull String message, Throwable t) {
        if (priority >= Log.ERROR) {
            FirebaseCrashlytics crashlytics = FirebaseCrashlytics.getInstance();
            if (t == null) {
                crashlytics.log(message);
            } else {
                crashlytics.recordException(t);
            }
        }
    }
}

