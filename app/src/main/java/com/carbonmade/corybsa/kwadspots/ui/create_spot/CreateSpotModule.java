package com.carbonmade.corybsa.kwadspots.ui.create_spot;

import com.carbonmade.corybsa.kwadspots.di.ActivityScoped;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class CreateSpotModule {
    @ActivityScoped
    @Binds
    abstract CreateSpotContract.Presenter createSpotPresenter(CreateSpotPresenter presenter);
}
