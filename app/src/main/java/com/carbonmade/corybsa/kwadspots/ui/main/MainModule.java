package com.carbonmade.corybsa.kwadspots.ui.main;

import com.carbonmade.corybsa.kwadspots.di.ActivityScoped;
import com.carbonmade.corybsa.kwadspots.di.FragmentScoped;
import com.carbonmade.corybsa.kwadspots.ui.main.home.HomeFragment;
import com.carbonmade.corybsa.kwadspots.ui.main.search.SearchFragment;
import com.carbonmade.corybsa.kwadspots.ui.main.spots.SpotsFragment;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainModule {
    @ActivityScoped
    @Binds
    abstract MainContract.Presenter mainPresenter(MainPresenter presenter);

    @FragmentScoped
    @ContributesAndroidInjector
    abstract HomeFragment homeFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract SearchFragment searchFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract SpotsFragment spotsFragment();
}
