package com.carbonmade.corybsa.kwadspots.di;

import android.content.Context;

import com.carbonmade.corybsa.kwadspots.App;


import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    @Provides
    Context provideContext(App application) {
        return application.getApplicationContext();
    }
}
