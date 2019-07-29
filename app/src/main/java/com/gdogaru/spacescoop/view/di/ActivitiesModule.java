package com.gdogaru.spacescoop.view.di;

import com.gdogaru.spacescoop.view.article.ArticleItemFragment;
import com.gdogaru.spacescoop.view.article.ArticlesActivity;
import com.gdogaru.spacescoop.view.article.FullImageActivity;
import com.gdogaru.spacescoop.view.main.ArticleDisplayer;
import com.gdogaru.spacescoop.view.main.MainActivity;
import com.gdogaru.spacescoop.view.main.credits.CreditsFragment;
import com.gdogaru.spacescoop.view.main.scoops.ScoopsFragment;
import com.gdogaru.spacescoop.view.main.search.SearchFragment;
import com.gdogaru.spacescoop.view.main.settings.SettingsFragment;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@SuppressWarnings("unused")
@Module
public interface ActivitiesModule {

    @ContributesAndroidInjector(modules = {MainActivityBuildersModule.class})
    MainActivity contributeMainActivity();

    @ContributesAndroidInjector(modules = {ArticlesActivityBuildersModule.class})
    ArticlesActivity contributeItemViewActivity();

    @ContributesAndroidInjector(modules = {FullImageActivityModule.class})
    FullImageActivity contributeFullImageActivity();

    @Module
    interface MainActivityBuildersModule {

        @ContributesAndroidInjector
        ScoopsFragment contributeMainListViewFragment();

        @ContributesAndroidInjector
        SearchFragment contributeMainSearchFragment();

        @ContributesAndroidInjector
        CreditsFragment contributeCreditsFragment();

        @ContributesAndroidInjector
        SettingsFragment contributeSettingsFragment();

        @Binds
        ArticleDisplayer displayer(MainActivity activity);
    }

    @Module
    interface ArticlesActivityBuildersModule {
        @ContributesAndroidInjector
        ArticleItemFragment contributeArticleItemFragment();
    }

    @Module
    interface FullImageActivityModule {
    }
}
