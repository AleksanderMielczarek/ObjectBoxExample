package com.github.aleksandermielczarek.objectboxexample.component;

import com.github.aleksandermielczarek.napkin.module.NapkinServiceModule;
import com.github.aleksandermielczarek.napkin.scope.ServiceScope;
import com.github.aleksandermielczarek.objectboxexample.notification.NotificationInstanceIdService;
import com.github.aleksandermielczarek.objectboxexample.notification.NotificationService;

import dagger.Subcomponent;

/**
 * Created by Aleksander Mielczarek on 10.03.2017.
 */
@ServiceScope
@Subcomponent(modules = NapkinServiceModule.class)
public interface ServiceComponent {

    void inject(NotificationService notificationService);

    void inject(NotificationInstanceIdService notificationInstanceIdService);
}
