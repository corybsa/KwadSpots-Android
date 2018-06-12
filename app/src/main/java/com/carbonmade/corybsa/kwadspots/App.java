package com.carbonmade.corybsa.kwadspots;

import android.app.Application;

import com.carbonmade.corybsa.kwadspots.di.AppModule;
import com.carbonmade.corybsa.kwadspots.di.DaggerNetworkComponent;
import com.carbonmade.corybsa.kwadspots.di.NetworkComponent;
import com.carbonmade.corybsa.kwadspots.di.NetworkModule;

public class App extends Application {
    private NetworkComponent mNetworkComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mNetworkComponent = DaggerNetworkComponent.builder()
                .appModule(new AppModule(this))
                .networkModule(new NetworkModule(""))
                .build();
    }

    public NetworkComponent getNetworkComponent() {
        return mNetworkComponent;
    }
}
