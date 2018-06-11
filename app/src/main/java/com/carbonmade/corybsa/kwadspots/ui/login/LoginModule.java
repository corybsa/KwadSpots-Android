package com.carbonmade.corybsa.kwadspots.ui.login;

import dagger.Module;
import dagger.Provides;

@Module
public class LoginModule {
    @Provides
    LoginPresenter provideLoginPresenter(LoginContract.View view) {
        return new LoginPresenter(view);
    }
}
