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
