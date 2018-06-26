package com.carbonmade.corybsa.kwadspots.di;

import com.carbonmade.corybsa.kwadspots.services.SpotService;
import com.google.firebase.storage.FirebaseStorage;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public abstract class ServicesModule {
    @Provides
    @Singleton
    SpotService provideSpotService() {
        return new SpotService();
    }
}
