package com.gdogaru.spacescoop.controllers;

import android.os.Bundle;

import com.gdogaru.spacescoop.BuildConfig;
import com.google.firebase.analytics.FirebaseAnalytics;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 *
 */
@Singleton
public class AnalyticsHelper {
    private final FirebaseAnalytics firebaseAnalytics;

    @Inject
    public AnalyticsHelper(FirebaseAnalytics firebaseAnalytics) {
        this.firebaseAnalytics = firebaseAnalytics;
    }

    public void logEvent(Event event, long value) {
        if (isDevelopmentBuild()) return;

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, event.value);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, event.value);
        bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "Events");
        bundle.putString(FirebaseAnalytics.Param.VALUE, String.valueOf(value));

        firebaseAnalytics.logEvent(event.value, bundle);
    }

    @SuppressWarnings("ConstantConditions")
    private static boolean isDevelopmentBuild() {
        return BuildConfig.DEBUG || BuildConfig.FLAVOR.equals("dev");
    }

    public void logTime(String event, long downTime) {
        if (isDevelopmentBuild()) return;

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, event);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, event);
        bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "Times");
        bundle.putString(FirebaseAnalytics.Param.VALUE, String.valueOf(downTime));

        firebaseAnalytics.logEvent(event, bundle);
    }

    public enum Event {
        RATE_LATER("rate_later"),
        RATE_NOW("rate_now"),
        RATE_NEVER("rate_never"),
        NEW_ITEMS_NOTIFY("Notification New Items"),
        UPDATES_CHECK("Updates check"),
        CATEG_LOAD_TIMES("LOADING_TIME"),
        ACTION_ARTICLE_LOAD("Article load"),
        ACTION_ARTICLE_PROCESSING("Article processing"),
        ACTION_PICTURE_LOAD("Picture load"),
        ACTION_THUMB_LOAD("Thumb load"),
        ACTION_PICTURE_PROCESSING("Picture processing"),
        ACTION_PICTURE_LARGE_PROCESSING("Picture processing large"),
        FULL_IMAGE_VIEW("Full image view"),
        CHANGE_LANGUAGE("Change Language"),
        LIST_END_REACHED("List end reached"),
        LIST_UPDATE_START("List update start"),
        LIST_UPDATE_END("List update end");

        private final String value;

        Event(String value) {
            this.value = value;
        }
    }
}
