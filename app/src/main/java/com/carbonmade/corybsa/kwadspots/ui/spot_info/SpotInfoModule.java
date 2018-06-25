package com.carbonmade.corybsa.kwadspots.ui.spot_info;

import com.carbonmade.corybsa.kwadspots.di.ActivityScoped;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class SpotInfoModule {
    @ActivityScoped
    @Binds
    abstract SpotInfoContract.Presenter spotInfoPresenter(SpotInfoPresenter presenter);
}
