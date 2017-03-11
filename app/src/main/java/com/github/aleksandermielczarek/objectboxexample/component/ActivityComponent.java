package com.github.aleksandermielczarek.objectboxexample.component;

import com.github.aleksandermielczarek.napkin.module.NapkinActivityModule;
import com.github.aleksandermielczarek.napkin.scope.ActivityScope;
import com.github.aleksandermielczarek.objectboxexample.ui.notifications.NotificationsActivity;

import dagger.Subcomponent;

/**
 * Created by Aleksander Mielczarek on 10.03.2017.
 */
@ActivityScope
@Subcomponent(modules = NapkinActivityModule.class)
public interface ActivityComponent {

    void inject(NotificationsActivity notificationsActivity);
}
