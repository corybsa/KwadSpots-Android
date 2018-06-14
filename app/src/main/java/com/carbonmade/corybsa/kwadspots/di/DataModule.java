package com.carbonmade.corybsa.kwadspots.di;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DataModule {
    private static final String PREFS_FILE = "com.carbonmade.corybsa.kwadspots";

    @Provides
    @Singleton
    SharedPreferences providesSharedPreferences(Application application) {
        return application.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
    }
}
