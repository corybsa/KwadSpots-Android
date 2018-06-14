package com.carbonmade.corybsa.kwadspots.di;

import com.carbonmade.corybsa.kwadspots.ui.login.LoginActivity;
import com.carbonmade.corybsa.kwadspots.ui.login.LoginModule;
import com.carbonmade.corybsa.kwadspots.ui.main.MainActivity;
import com.carbonmade.corybsa.kwadspots.ui.main.MainModule;
import com.carbonmade.corybsa.kwadspots.ui.settings.SettingsActivity;
import com.carbonmade.corybsa.kwadspots.ui.settings.SettingsModule;
import com.carbonmade.corybsa.kwadspots.ui.signup.SignUpActivity;
import com.carbonmade.corybsa.kwadspots.ui.signup.SignUpModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBindingModule {
    @ActivityScoped
    @ContributesAndroidInjector(modules = LoginModule.class)
    abstract LoginActivity loginActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = SignUpModule.class)
    abstract SignUpActivity signUpActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = MainModule.class)
    abstract MainActivity mainActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = SettingsModule.class)
    abstract SettingsActivity settingsActivity();
}
