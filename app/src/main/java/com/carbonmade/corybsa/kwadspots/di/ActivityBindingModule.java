package com.carbonmade.corybsa.kwadspots.di;

import com.carbonmade.corybsa.kwadspots.ui.create_spot.CreateSpotActivity;
import com.carbonmade.corybsa.kwadspots.ui.create_spot.CreateSpotModule;
import com.carbonmade.corybsa.kwadspots.ui.login.LoginActivity;
import com.carbonmade.corybsa.kwadspots.ui.login.LoginModule;
import com.carbonmade.corybsa.kwadspots.ui.main.MainActivity;
import com.carbonmade.corybsa.kwadspots.ui.main.MainModule;
import com.carbonmade.corybsa.kwadspots.ui.password_reset.PasswordResetActivity;
import com.carbonmade.corybsa.kwadspots.ui.password_reset.PasswordResetModule;
import com.carbonmade.corybsa.kwadspots.ui.settings.SettingsActivity;
import com.carbonmade.corybsa.kwadspots.ui.settings.SettingsModule;
import com.carbonmade.corybsa.kwadspots.ui.sign_up.SignUpActivity;
import com.carbonmade.corybsa.kwadspots.ui.sign_up.SignUpModule;

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
    @ContributesAndroidInjector(modules = PasswordResetModule.class)
    abstract PasswordResetActivity passwordResetActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = MainModule.class)
    abstract MainActivity mainActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = CreateSpotModule.class)
    abstract CreateSpotActivity createSpotActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = SettingsModule.class)
    abstract SettingsActivity settingsActivity();
}
