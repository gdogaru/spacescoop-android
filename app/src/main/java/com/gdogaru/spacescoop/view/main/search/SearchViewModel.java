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

package com.gdogaru.spacescoop.view.main.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.gdogaru.spacescoop.controllers.AppSettingsController;
import com.gdogaru.spacescoop.db.NewsDao;
import com.gdogaru.spacescoop.db.model.ArticlePreview;

import javax.inject.Inject;

public class SearchViewModel extends ViewModel {

    private final LiveData<PagedList<ArticlePreview>> listLiveData;
    private final MutableLiveData<String> searchTerm = new MutableLiveData<>();

    @Inject
    public SearchViewModel(NewsDao newsDao, AppSettingsController settingsController) {
        listLiveData = Transformations.switchMap(
                searchTerm,
                input -> new LivePagedListBuilder<>(newsDao.newsSearchCursor("%" + input + "%", settingsController.getLanguage()), 50)
                        .build());

    }

    public LiveData<PagedList<ArticlePreview>> getListLiveData() {
        return listLiveData;
    }

    public void setSearchTerm(String term) {
        searchTerm.setValue(term);
    }
}
