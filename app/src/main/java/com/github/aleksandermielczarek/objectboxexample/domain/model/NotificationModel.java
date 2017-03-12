package com.github.aleksandermielczarek.objectboxexample.domain.model;

import com.github.aleksandermielczarek.objectboxexample.domain.data.Notification;
import com.github.aleksandermielczarek.objectboxexample.domain.repository.NotificationRepository;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by Aleksander Mielczarek on 11.03.2017.
 */

public class NotificationModel {

    public static final String TOPIC_NOTIFICATIONS = "notifications";

    private final NotificationRepository notificationRepository;

    @Inject
    public NotificationModel(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public Observable<List<Notification>> getNotifications() {
        return notificationRepository.findAllSortedByDateDesc();
    }

    public Observable<Integer> countUnreadNotifications() {
        return notificationRepository.countNotReadNotifications();
    }

    public Observable<Integer> countNotifications() {
        return notificationRepository.count();
    }

    public Single<Notification> readNotification(Notification notification) {
        return Single.just(notification)
                .doOnSuccess(notification_ -> notification_.setRead(true))
                .flatMap(notificationRepository::save);
    }

    public Completable deleteNotification(Notification notification) {
        return notificationRepository.delete(notification);
    }

    public Completable addNotification(Notification notification) {
        return notificationRepository.save(notification)
                .toCompletable();
    }

    public Completable addNotifications(List<Notification> notifications) {
        return notificationRepository.save(notifications)
                .toCompletable();
    }

    public Completable registerForNotifications() {
        return Completable.fromAction(() -> FirebaseMessaging.getInstance().subscribeToTopic(TOPIC_NOTIFICATIONS));
    }

    public Completable readAll() {
        return getNotifications()
                .flatMapSingle(notifications -> Observable.fromIterable(notifications)
                        .doOnNext(notification -> notification.setRead(true))
                        .toList())
                .flatMapSingle(notificationRepository::save)
                .ignoreElements();
    }

    public Single<List<Notification>> removeAll() {
        return notificationRepository.removeAll();
    }

}
