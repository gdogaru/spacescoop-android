package com.gdogaru.spacescoop.work;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.gdogaru.spacescoop.controllers.AppSettingsController;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WorkerController {

    private final AppSettingsController settingsController;

    @Inject
    public WorkerController(AppSettingsController settingsController) {
        this.settingsController = settingsController;
    }

    public LiveData<StaleState> updateArticles() {
        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(GetArticlesWorker.class)
                .setInputData(new Data.Builder()
                        .putString(GetArticlesWorker.PARAM_LANG, settingsController.getLanguage())
                        .putBoolean(GetArticlesWorker.PARAM_UPDATE_NEW, true)
                        .build())
                .build();
        WorkManager.getInstance().enqueueUniqueWork("update_articles", ExistingWorkPolicy.REPLACE, request);
        return Transformations.map(
                WorkManager.getInstance().getWorkInfoByIdLiveData(request.getId()),
                this::toStaleState);
    }

    public LiveData<StaleState> loadMoreArticles() {
        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(GetArticlesWorker.class)
                .setInputData(new Data.Builder()
                        .putString(GetArticlesWorker.PARAM_LANG, settingsController.getLanguage())
                        .putBoolean(GetArticlesWorker.PARAM_UPDATE_NEW, false)
                        .build())
                .build();
        WorkManager.getInstance().enqueueUniqueWork("update_articles", ExistingWorkPolicy.APPEND, request);
        return Transformations.map(
                WorkManager.getInstance().getWorkInfoByIdLiveData(request.getId()),
                this::toStaleState);
    }

    public LiveData<StaleState> setNewArticlesBgJob() {
        settingsController.getUpdateWifiOnly();

        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(GetArticlesWorker.class)
                .setInputData(new Data.Builder()
                        .putString(GetArticlesWorker.PARAM_LANG, settingsController.getLanguage())
                        .putBoolean(GetArticlesWorker.PARAM_UPDATE_NEW, false)
                        .putBoolean(GetArticlesWorker.PARAM_UPDATE_NEW, true)
                        .build())
                .setInitialDelay(getScheduleDelayMillis(), TimeUnit.MILLISECONDS)
                .setConstraints(new Constraints.Builder()
                        .setRequiredNetworkType(settingsController.getUpdateWifiOnly() ? NetworkType.UNMETERED : NetworkType.CONNECTED)
                        .build())
                .build();
        WorkManager.getInstance().enqueueUniqueWork("CHECK_FOR_NEW", ExistingWorkPolicy.REPLACE, request);
        return Transformations.map(
                WorkManager.getInstance().getWorkInfoByIdLiveData(request.getId()),
                this::toStaleState);
    }

    /**
     * @return millis until 10:00am 2 days later
     */
    private long getScheduleDelayMillis() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, 2);
        c.set(Calendar.HOUR, 10);
        c.set(Calendar.MINUTE, 10);
        return c.getTimeInMillis() - System.currentTimeMillis();
    }

    private StaleState toStaleState(WorkInfo input) {
        return input == null ? StaleState.SUCCESS : StaleState.from(input.getState());
    }
}
