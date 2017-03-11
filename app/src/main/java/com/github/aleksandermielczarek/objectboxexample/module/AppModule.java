package com.github.aleksandermielczarek.objectboxexample.module;

import android.app.Application;
import android.content.Context;

import com.github.aleksandermielczarek.napkin.module.NapkinAppModule;
import com.github.aleksandermielczarek.napkin.qualifier.AppContext;
import com.github.aleksandermielczarek.napkin.scope.AppScope;
import com.github.aleksandermielczarek.objectboxexample.domain.data.MyObjectBox;

import dagger.Module;
import dagger.Provides;
import io.objectbox.BoxStore;

/**
 * Created by Aleksander Mielczarek on 10.03.2017.
 */
@AppScope
@Module
public class AppModule extends NapkinAppModule {

    public AppModule(Application application) {
        super(application);
    }

    @AppScope
    @Provides
    BoxStore provideBoxStore(@AppContext Context context) {
        return MyObjectBox.builder()
                .androidContext(context)
                .build();
    }
}
