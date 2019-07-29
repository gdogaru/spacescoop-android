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

