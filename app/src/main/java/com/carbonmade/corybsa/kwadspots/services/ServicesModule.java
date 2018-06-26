package com.carbonmade.corybsa.kwadspots.services;

import com.google.firebase.storage.FirebaseStorage;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public abstract class ServicesModule {
    @Provides
    @Singleton
    SpotService provideSpotService(FirebaseStorage firebaseStorage) {
        return new SpotService(firebaseStorage);
    }
}
