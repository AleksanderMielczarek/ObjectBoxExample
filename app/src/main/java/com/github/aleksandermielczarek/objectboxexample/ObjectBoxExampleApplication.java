package com.github.aleksandermielczarek.objectboxexample;

import android.app.Application;

import com.github.aleksandermielczarek.napkin.ComponentProvider;
import com.github.aleksandermielczarek.objectboxexample.component.AppComponent;
import com.github.aleksandermielczarek.objectboxexample.component.DaggerAppComponent;
import com.github.aleksandermielczarek.objectboxexample.module.AppModule;
import com.jakewharton.threetenabp.AndroidThreeTen;

/**
 * Created by Aleksander Mielczarek on 10.03.2017.
 */

public class ObjectBoxExampleApplication extends Application implements ComponentProvider<AppComponent> {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidThreeTen.init(this);
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    @Override
    public AppComponent provideComponent() {
        return appComponent;
    }
}
