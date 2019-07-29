package com.gdogaru.spacescoop.view.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.gdogaru.spacescoop.api.ApiResponse;
import com.gdogaru.spacescoop.api.SpacescoopClient;
import com.gdogaru.spacescoop.api.model.Language;
import com.gdogaru.spacescoop.controllers.AppSettingsController;
import com.gdogaru.spacescoop.work.WorkerController;

import java.util.List;

import javax.inject.Inject;

public class MainActivityViewModel extends ViewModel {

    private final SpacescoopClient client;
    private final WorkerController workerController;
    private final AppSettingsController settingsController;

    @Inject
    public MainActivityViewModel(SpacescoopClient client, WorkerController workerController, AppSettingsController settingsController) {
        this.client = client;
        this.workerController = workerController;
        this.settingsController = settingsController;
    }


    public void init() {
        workerController.updateArticles();
        workerController.setNewArticlesBgJob();
    }


    public LiveData<ApiResponse<List<Language>>> getLanguages() {
        return client.getLanguages();
    }


    public void changeLanguage(Language language) {
        settingsController.setLanguage(language.getShortName());
        settingsController.setEndReached(false);
    }
}
