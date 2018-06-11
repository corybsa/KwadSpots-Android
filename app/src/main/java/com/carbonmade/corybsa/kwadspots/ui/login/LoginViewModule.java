package com.carbonmade.corybsa.kwadspots.ui.login;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class LoginViewModule {
    @Binds
    abstract LoginContract.View provideLoginView(LoginActivity activity);
}
