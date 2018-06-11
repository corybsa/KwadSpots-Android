package com.carbonmade.corybsa.kwadspots;

import android.app.Application;

import com.carbonmade.corybsa.kwadspots.components.AppComponent;
import com.carbonmade.corybsa.kwadspots.components.DaggerAppComponent;
import com.carbonmade.corybsa.kwadspots.modules.AppModule;

public class App extends Application {
    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }
}
