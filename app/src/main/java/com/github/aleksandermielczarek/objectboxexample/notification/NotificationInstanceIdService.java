package com.github.aleksandermielczarek.objectboxexample.notification;

import com.github.aleksandermielczarek.napkin.Napkin;
import com.github.aleksandermielczarek.napkin.module.NapkinServiceModule;
import com.github.aleksandermielczarek.objectboxexample.component.AppComponent;
import com.github.aleksandermielczarek.objectboxexample.domain.model.NotificationModel;
import com.google.firebase.iid.FirebaseInstanceIdService;

import javax.inject.Inject;

/**
 * Created by Aleksander Mielczarek on 11.03.2017.
 */

public class NotificationInstanceIdService extends FirebaseInstanceIdService {

    @Inject
    protected NotificationModel notificationModel;

    @Override
    public void onCreate() {
        Napkin.<AppComponent>provideAppComponent(this)
                .with(new NapkinServiceModule(this))
                .inject(this);
        super.onCreate();
    }

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        notificationModel.registerForNotifications().blockingAwait();
    }
}

