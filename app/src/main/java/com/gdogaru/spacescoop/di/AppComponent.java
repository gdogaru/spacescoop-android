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

package com.gdogaru.spacescoop.di;

import com.gdogaru.spacescoop.SpaceScoopApp;
import com.gdogaru.spacescoop.db.DatabaseModule;
import com.gdogaru.spacescoop.view.di.ActivitiesModule;
import com.gdogaru.spacescoop.view.di.AssistedInjectModule;
import com.gdogaru.spacescoop.view.di.ViewModelModule;
import com.gdogaru.spacescoop.work.di.WorkerBindingModule;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {AndroidInjectionModule.class, AndroidSupportInjectionModule.class,
        ApplicationModule.class, CoreModule.class, DatabaseModule.class,
        ActivitiesModule.class, ViewModelModule.class, WorkerBindingModule.class, AssistedInjectModule.class
})
public interface AppComponent {

    @Component.Builder
    interface Builder {

        Builder applicationModule(ApplicationModule applicationModule);

        Builder roomModule(DatabaseModule roomModule);

        AppComponent build();
    }

    void inject(SpaceScoopApp app);

//    void inject(UpdateErc20RatesWorker updateErc20RatesWorker);
//
//    void inject(UpdateEthBalanceWorker updateEthBalanceWorker);

}

