package com.gdogaru.spacescoop.db;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DatabaseModule {

    private final Application application;

    public DatabaseModule(Application application) {
        this.application = application;
    }

    @Singleton
    @Provides
    public AppDatabase providesRoomDatabase() {
        return AppDatabase.getInstance(application);
    }

    @Singleton
    @Provides
    NewsDao providesNewsDao(AppDatabase appDatabase) {
        return appDatabase.newsDao();
    }

}
