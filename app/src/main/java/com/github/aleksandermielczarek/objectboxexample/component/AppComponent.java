package com.github.aleksandermielczarek.objectboxexample.component;

import com.github.aleksandermielczarek.napkin.module.NapkinActivityModule;
import com.github.aleksandermielczarek.napkin.module.NapkinServiceModule;
import com.github.aleksandermielczarek.napkin.scope.AppScope;
import com.github.aleksandermielczarek.objectboxexample.module.AppModule;

import dagger.Component;

/**
 * Created by Aleksander Mielczarek on 10.03.2017.
 */
@Component(modules = AppModule.class)
@AppScope
public interface AppComponent {

    ActivityComponent with(NapkinActivityModule activityModule);

    ServiceComponent with(NapkinServiceModule serviceModule);
}
