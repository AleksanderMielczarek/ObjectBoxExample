package com.github.aleksandermielczarek.objectboxexample.notification;

import android.text.TextUtils;

import com.github.aleksandermielczarek.napkin.Napkin;
import com.github.aleksandermielczarek.napkin.module.NapkinServiceModule;
import com.github.aleksandermielczarek.objectboxexample.R;
import com.github.aleksandermielczarek.objectboxexample.component.AppComponent;
import com.github.aleksandermielczarek.objectboxexample.domain.data.Notification;
import com.github.aleksandermielczarek.objectboxexample.domain.model.NotificationModel;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.threeten.bp.LocalDateTime;

import java.util.Map;

import javax.inject.Inject;

/**
 * Created by Aleksander Mielczarek on 11.03.2017.
 */

public class NotificationService extends FirebaseMessagingService {

    public static final String DATA_TITLE = "title";
    public static final String DATA_BODY = "body";

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
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Map<String, String> data = remoteMessage.getData();
        String title = data.get(DATA_TITLE);
        if (TextUtils.isEmpty(title)) {
            title = getString(R.string.default_title);
        }
        String body = data.get(DATA_BODY);
        if (TextUtils.isEmpty(body)) {
            body = getString(R.string.default_body);
        }
        LocalDateTime date = LocalDateTime.now();
        Notification notification = new Notification(title, body, date, false);
        notificationModel.addNotification(notification).blockingAwait();
    }
}
