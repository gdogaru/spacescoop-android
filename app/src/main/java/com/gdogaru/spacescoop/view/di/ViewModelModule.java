package com.gdogaru.spacescoop.view.di;

import android.annotation.SuppressLint;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.gdogaru.spacescoop.view.main.MainActivityViewModel;
import com.gdogaru.spacescoop.view.main.scoops.ScoopsViewModel;
import com.gdogaru.spacescoop.view.main.search.SearchViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@SuppressLint("unused")
@Module
public interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel.class)
    ViewModel bindMainActivityViewModel(MainActivityViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ScoopsViewModel.class)
    ViewModel bindScoopsViewModel(ScoopsViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel.class)
    ViewModel bindSearchViewModel(SearchViewModel viewModel);

    @Binds
    ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);
}
