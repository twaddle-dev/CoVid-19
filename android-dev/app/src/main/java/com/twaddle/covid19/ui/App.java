package com.twaddle.covid19.ui;

import android.app.Application;

import com.twaddle.covid19.dagger.DaggerDataComponent;
import com.twaddle.covid19.dagger.DataComponent;
import com.twaddle.covid19.dagger.DataModule;

public class App extends Application {
    private DataComponent dataComponent;
    public static App app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        dataComponent = DaggerDataComponent.builder()
                .dataModule(new DataModule(this))
                .build();

    }

    public com.twaddle.covid19.dagger.DataComponent getDataComponent() {
        return dataComponent;
    }
    public static App getApp() {
        return app;
    }

}

