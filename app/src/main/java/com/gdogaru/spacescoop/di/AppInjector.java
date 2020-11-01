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

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.gdogaru.spacescoop.SpaceScoopApp;
import com.gdogaru.spacescoop.db.DatabaseModule;

import org.jetbrains.annotations.NotNull;

import dagger.android.AndroidInjection;
import dagger.android.support.AndroidSupportInjection;

/**
 * Helper class to automatically inject fragments if they implement [Injectable].
 */
public class AppInjector {

    private AppInjector() {
    }

    public static void init(SpaceScoopApp app) {
        AppComponent appComponent = DaggerAppComponent.builder()
                .applicationModule(new ApplicationModule(app))
                .roomModule(new DatabaseModule(app))
                .build();
        appComponent.inject(app);

        app.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NotNull Activity activity, Bundle savedInstanceState) {
                handleActivity(activity);
            }

            @Override
            public void onActivityStarted(@NotNull Activity activity) {
                //empty
            }

            @Override
            public void onActivityResumed(@NotNull Activity activity) {
                //empty
            }

            @Override
            public void onActivityPaused(@NotNull Activity activity) {
                //empty
            }

            @Override
            public void onActivityStopped(@NotNull Activity activity) {
                //empty
            }

            @Override
            public void onActivitySaveInstanceState(@NotNull Activity activity, @NotNull Bundle outState) {
                //empty
            }

            @Override
            public void onActivityDestroyed(@NotNull Activity activity) {
                //empty
            }
        });
    }

    private static void handleActivity(Activity activity) {
        AndroidInjection.inject(activity);

        if (activity instanceof AppCompatActivity) {
            ((FragmentActivity) activity).getSupportFragmentManager()
                    .registerFragmentLifecycleCallbacks(
                            new FragmentManager.FragmentLifecycleCallbacks() {
                                @Override
                                public void onFragmentPreCreated(@NotNull FragmentManager fm, @NotNull Fragment fragment,
                                                                 Bundle savedInstanceState) {
                                    if (fragment instanceof Injectable) {
                                        AndroidSupportInjection.inject(fragment);
                                    }
                                }
                            },
                            true
                    );
        }
    }
}
