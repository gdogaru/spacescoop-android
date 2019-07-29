package com.gdogaru.spacescoop.work.di;

import com.gdogaru.spacescoop.work.GetArticlesWorker;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public interface WorkerBindingModule {

    @Binds
    @IntoMap
    @WorkerKey(GetArticlesWorker.class)
    ChildWorkerFactory bindGetArticlesWorker(GetArticlesWorker.Factory factory);

//    @Binds
//    @IntoMap
//    @WorkerKey(ChangeLanguageWorker.class)
//    ChildWorkerFactory bindChangeLanguageWorker(ChangeLanguageWorker.Factory factory);
}
