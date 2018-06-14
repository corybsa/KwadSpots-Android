package com.carbonmade.corybsa.kwadspots.ui.signup;

import com.carbonmade.corybsa.kwadspots.di.ActivityScoped;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class SignUpModule {
    @ActivityScoped
    @Binds
    abstract SignUpContract.Presenter signUpPresenter(SignUpPresenter presenter);
}
