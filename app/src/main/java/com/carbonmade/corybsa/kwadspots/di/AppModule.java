package com.carbonmade.corybsa.kwadspots.di;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private Application mApplication;
    private static final String PREFS_FILE = "com.carbonmade.corybsa.kwadspots";

    public AppModule(Application application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    Application providesApplication() {
        return mApplication;
    }

    @Provides
    @Singleton
    SharedPreferences providesSharedPreferences() {
        return mApplication.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
    }
}
