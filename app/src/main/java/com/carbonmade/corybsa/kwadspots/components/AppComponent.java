package com.carbonmade.corybsa.kwadspots.components;

import com.carbonmade.corybsa.kwadspots.modules.AppModule;
import com.carbonmade.corybsa.kwadspots.ui.main.home.HomeFragment;
import com.carbonmade.corybsa.kwadspots.ui.main.MainActivity;
import com.carbonmade.corybsa.kwadspots.ui.main.search.SearchFragment;
import com.carbonmade.corybsa.kwadspots.ui.main.spots.SpotsFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(
        modules = {
                AppModule.class
        }
)
public interface AppComponent {
    void inject(MainActivity activity);
    void inject(HomeFragment homeFragment);
    void inject(SpotsFragment spotsFragment);
    void inject(SearchFragment searchFragment);
}
