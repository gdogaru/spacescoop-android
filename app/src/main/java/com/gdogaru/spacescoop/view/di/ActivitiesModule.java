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

package com.gdogaru.spacescoop.view.di;

import com.gdogaru.spacescoop.view.SplashActivity;
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

    @ContributesAndroidInjector
    SplashActivity contributeSplashActivity();

    @ContributesAndroidInjector(modules = {MainActivityBuildersModule.class})
    MainActivity contributeMainActivity();

    @ContributesAndroidInjector(modules = {ArticlesActivityBuildersModule.class})
    ArticlesActivity contributeItemViewActivity();

    @ContributesAndroidInjector
    FullImageActivity contributeFullImageActivity();

    @Module
    interface ArticlesActivityBuildersModule {
        @ContributesAndroidInjector
        ArticleItemFragment contributeArticleItemFragment();
    }

    @Module
    interface MainActivityBuildersModule {

        @ContributesAndroidInjector
        ScoopsFragment contributeScoopsFragment();

        @ContributesAndroidInjector
        SearchFragment contributeSearchFragment();

        @ContributesAndroidInjector
        CreditsFragment contributeCreditsFragment();

        @ContributesAndroidInjector
        SettingsFragment contributeSettingsFragment();

        @Binds
        ArticleDisplayer displayer(MainActivity activity);
    }
}
