package com.carbonmade.corybsa.kwadspots.ui.password_reset;

import com.carbonmade.corybsa.kwadspots.di.ActivityScoped;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class PasswordResetModule {
    @ActivityScoped
    @Binds
    abstract PasswordResetContract.Presenter passwordResetPresenter(PasswordResetPresenter presenter);
}
