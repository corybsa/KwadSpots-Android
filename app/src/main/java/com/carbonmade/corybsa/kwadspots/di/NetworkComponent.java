package com.carbonmade.corybsa.kwadspots.di;

import com.carbonmade.corybsa.kwadspots.ui.login.LoginActivity;
import com.carbonmade.corybsa.kwadspots.ui.main.MainActivity;
import com.carbonmade.corybsa.kwadspots.ui.main.home.HomeFragment;
import com.carbonmade.corybsa.kwadspots.ui.main.search.SearchFragment;
import com.carbonmade.corybsa.kwadspots.ui.main.spots.SpotsFragment;
import com.carbonmade.corybsa.kwadspots.ui.signup.SignUpActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, NetworkModule.class})
public interface NetworkComponent {
    // Activities
    void inject(LoginActivity activity);
    void inject(MainActivity activity);
    void inject(SignUpActivity activity);

    // Fragments
    void inject(HomeFragment fragment);
    void inject(SpotsFragment fragment);
    void inject(SearchFragment fragment);
}
