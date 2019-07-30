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

package com.gdogaru.spacescoop.view.main.scoops;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.gdogaru.spacescoop.controllers.AppSettingsController;
import com.gdogaru.spacescoop.db.NewsDao;
import com.gdogaru.spacescoop.db.model.ArticlePreview;
import com.gdogaru.spacescoop.db.model.ArticleThumb;
import com.gdogaru.spacescoop.work.StaleState;
import com.gdogaru.spacescoop.work.WorkerController;

import javax.inject.Inject;

public class ScoopsViewModel extends ViewModel {

    private final LiveData<PagedList<ArticlePreview>> listLiveData;
    private final LiveData<PagedList<ArticleThumb>> gridLiveData;
    private final WorkerController workerController;
    private final MediatorLiveData<StaleState> stateMediator = new MediatorLiveData<>();

    @Inject
    public ScoopsViewModel(NewsDao newsDao, WorkerController workerController, AppSettingsController settingsController) {
        this.workerController = workerController;
        String lang = settingsController.getLanguage();

        listLiveData = new LivePagedListBuilder<>(newsDao.articlePreviews(lang), 20)
                .build();
        gridLiveData = new LivePagedListBuilder<>(newsDao.thumbsCursor(lang), 20)
                .build();
        stateMediator.setValue(StaleState.SUCCESS);
    }

    public LiveData<PagedList<ArticlePreview>> getListLiveData() {
        return listLiveData;
    }

    public LiveData<PagedList<ArticleThumb>> getGridLiveData() {
        return gridLiveData;
    }

    public LiveData<StaleState> getNewScoops() {
        LiveData<StaleState> liveData = workerController.updateArticles();
        addToMergeState(liveData);
        return liveData;
    }

    public LiveData<StaleState> loadMore() {
        LiveData<StaleState> stateLiveData = workerController.loadMoreArticles();
        addToMergeState(stateLiveData);
        return stateLiveData;
    }

    private void addToMergeState(LiveData<StaleState> stateLiveData) {
        stateMediator.addSource(stateLiveData, staleState -> {
            stateMediator.setValue(staleState);
            if (staleState.isEndState()) {
                stateMediator.removeSource(stateLiveData);
            }
        });
    }

    public LiveData<Boolean> loadMoreActive() {
        return Transformations.map(stateMediator, input -> !input.isEndState());
    }
}
