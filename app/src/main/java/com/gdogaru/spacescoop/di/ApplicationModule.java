package com.gdogaru.spacescoop.di;

import android.app.Application;
import android.content.Context;

import com.gdogaru.spacescoop.SpaceScoopApp;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {
    private final SpaceScoopApp application;

    @Inject
    public ApplicationModule(SpaceScoopApp application) {
        this.application = application;
    }

    @Singleton
    @Provides
    public SpaceScoopApp app() {
        return application;
    }

    @Singleton
    @Provides
    public Application application() {
        return application;
    }

    @Singleton
    @Provides
    public Context context() {
        return application;
    }

}
