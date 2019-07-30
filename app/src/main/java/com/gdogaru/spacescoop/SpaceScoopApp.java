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

import android.app.Activity;
import android.app.Fragment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.multidex.MultiDexApplication;
import androidx.work.Configuration;
import androidx.work.WorkManager;

import com.crashlytics.android.Crashlytics;
import com.evernote.android.state.StateSaver;
import com.gdogaru.spacescoop.di.AppInjector;
import com.gdogaru.spacescoop.work.di.DaggerWorkerFactory;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.HasFragmentInjector;
import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

/**
 * @author Gabriel Dogaru (gdogaru@gmail.com)
 */
public class SpaceScoopApp extends MultiDexApplication implements HasActivityInjector, HasFragmentInjector {
    public static final boolean DEBUG_TIMES = false;
    @Inject
    DaggerWorkerFactory workerFactory;
    @Inject
    DispatchingAndroidInjector<Activity> activityInjector;
    @Inject
    DispatchingAndroidInjector<Fragment> fragmentInjector;

    @Override
    public void onCreate() {
        super.onCreate();
        initTimber();
        Timber.d("Application.onCreate - Initializing application...");

        if (!BuildConfig.DEBUG && BuildConfig.USE_CRASHLYTICS) {
            Fabric.with(this, new Crashlytics());
        }

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
    public AndroidInjector<Activity> activityInjector() {
        return activityInjector;
    }

    @Override
    public AndroidInjector<Fragment> fragmentInjector() {
        return fragmentInjector;
    }
}


class CrashlyticsTree extends Timber.Tree {
    private static final String CRASHLYTICS_KEY_MESSAGE = "message";
    private static final String CRASHLYTICS_KEY_PRIORITY = "priority";
    private static final String CRASHLYTICS_KEY_TAG = "ibv";

    @Override
    protected void log(int priority, String tag, @NonNull String message, Throwable t) {
        if (priority >= Log.ERROR) {
            if (t == null) {
                Crashlytics.log(priority, CRASHLYTICS_KEY_TAG, message);
            } else {
                Crashlytics.setInt(CRASHLYTICS_KEY_PRIORITY, priority);
                Crashlytics.setString(CRASHLYTICS_KEY_TAG, tag);
                Crashlytics.setString(CRASHLYTICS_KEY_MESSAGE, message);
                Crashlytics.logException(t);
            }
        }
    }
}

