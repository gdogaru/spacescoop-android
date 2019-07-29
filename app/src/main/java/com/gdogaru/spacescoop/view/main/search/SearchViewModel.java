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
