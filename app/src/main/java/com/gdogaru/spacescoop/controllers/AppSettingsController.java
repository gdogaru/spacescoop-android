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

package com.gdogaru.spacescoop.controllers;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import java.util.Date;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author Gabriel Dogaru (gdogaru@gmail.com)
 */
@Singleton
public class AppSettingsController {

    private final String PREFS_NAME = "spacescoop_updates";
    private final String LANGUAGE = "language";
    private final String LAST_UPDATED = "last_update";
    private final String IS_IN_LIST_MODE = "list_mode";
    private final String DOWNLOADED_PAGES = "downloaded_pages";
    private final String END_REACHED = "end_reached";
    private final String SEEN_DRAWER = "seen_drawer";
    private final String END_UPDATE_JOB_ID = "end_update_job_id_2";
    private final String UPDATE_WIFI_ONLY = "update_wifi_only";
    private final String NOTIFY_UPDATES = "notify_updates";
    private final String APP_VIEWS = "app_views";
    private final Context context;

    @Inject
    public AppSettingsController(@NonNull Context context) {
        this.context = context.getApplicationContext();
    }

    public Date getLastUpdated() {
        SharedPreferences sharedPreferences = getSharedPreferences();
        return new Date(sharedPreferences.getLong(LAST_UPDATED, 0));
    }

    public void setLastUpdated(Date date) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        sharedPreferences.edit().putLong(LAST_UPDATED, date.getTime()).apply();
    }

    public String getLanguage() {
        SharedPreferences sharedPreferences = getSharedPreferences();
        return sharedPreferences.getString(LANGUAGE, "en");
    }

    public void setLanguage(String language) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        sharedPreferences.edit().putString(LANGUAGE, language).apply();
    }

    public int getPagesDownloadedNo() {
        SharedPreferences sharedPreferences = getSharedPreferences();
        return sharedPreferences.getInt(DOWNLOADED_PAGES, -1);
    }

    public void setPagesDownloadedNo(int no) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        sharedPreferences.edit().putInt(DOWNLOADED_PAGES, no).apply();
    }

    public boolean isInListMode() {
        SharedPreferences sharedPreferences = getSharedPreferences();
        return sharedPreferences.getBoolean(IS_IN_LIST_MODE, true);
    }

    public void setIsInListMode(boolean inListMode) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        sharedPreferences.edit().putBoolean(IS_IN_LIST_MODE, inListMode).apply();
    }

    private SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences(PREFS_NAME, 0);
    }

    public boolean isEndReached() {
        SharedPreferences sharedPreferences = getSharedPreferences();
        return sharedPreferences.getBoolean(END_REACHED, false);
    }

    public void setEndReached(boolean b) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        sharedPreferences.edit().putBoolean(END_REACHED, b).apply();
    }

    public boolean seenDrawer() {
        SharedPreferences sharedPreferences = getSharedPreferences();
        return sharedPreferences.getBoolean(SEEN_DRAWER, false);
    }

    public void setSeenDrawer(boolean seen) {
        getSharedPreferences()
                .edit()
                .putBoolean(SEEN_DRAWER, seen)
                .apply();
    }

    @Nullable
    public String getEndUpdateJobId() {
        SharedPreferences sharedPreferences = getSharedPreferences();
        return sharedPreferences.getString(END_UPDATE_JOB_ID, null);
    }

    public void setEndUpdateJobId(String id) {
        getSharedPreferences()
                .edit()
                .putString(END_UPDATE_JOB_ID, id)
                .apply();
    }

    public boolean getUpdateWifiOnly() {
        return getSharedPreferences()
                .getBoolean(UPDATE_WIFI_ONLY, true);
    }

    public void setUpdateWifiOnly(boolean value) {
        getSharedPreferences()
                .edit()
                .putBoolean(UPDATE_WIFI_ONLY, value)
                .apply();
    }

    public boolean getNotifyUpdates() {
        return getSharedPreferences()
                .getBoolean(NOTIFY_UPDATES, true);
    }

    public void setNotifyUpdates(boolean value) {
        getSharedPreferences()
                .edit()
                .putBoolean(NOTIFY_UPDATES, value)
                .apply();
    }

    public int getAppViews() {
        return getSharedPreferences().getInt(APP_VIEWS, 0);
    }

    public void setAppViews(int no) {
        getSharedPreferences().edit()
                .putInt(APP_VIEWS, no)
                .apply();
    }

    public LiveData<Boolean> isInListModeLiveData() {
        return new LiveData<Boolean>() {
            private SharedPreferences.OnSharedPreferenceChangeListener listener;

            @Override
            protected void onActive() {
                super.onActive();
                listener = (prefs, key) -> {
                    if (key.equals(IS_IN_LIST_MODE)) {
                        setValue(isInListMode());
                    }
                };
                setValue(isInListMode());
                getSharedPreferences().registerOnSharedPreferenceChangeListener(listener);
            }

            @Override
            protected void onInactive() {
                if (listener != null)
                    getSharedPreferences().unregisterOnSharedPreferenceChangeListener(listener);
                super.onInactive();
            }
        };
    }
}
