package com.carbonmade.corybsa.kwadspots.di;

import com.carbonmade.corybsa.kwadspots.ui.login.LoginActivity;
import com.carbonmade.corybsa.kwadspots.ui.login.LoginModule;
import com.carbonmade.corybsa.kwadspots.ui.login.LoginViewModule;
import com.carbonmade.corybsa.kwadspots.ui.main.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class BuildersModule {
    @ContributesAndroidInjector(modules = {LoginModule.class, LoginViewModule.class})
    abstract LoginActivity bindLoginActivity();

    @ContributesAndroidInjector
    abstract MainActivity bindMainActivity();
}
