package com.github.aleksandermielczarek.objectboxexample.domain.repository;

import com.github.aleksandermielczarek.objectboxexample.domain.data.Notification;
import com.github.aleksandermielczarek.objectboxexample.domain.data.Notification_;

import java.util.List;

import javax.inject.Inject;

import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.rx.RxQuery;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by Aleksander Mielczarek on 10.03.2017.
 */

public class NotificationRepository {

    private final Box<Notification> notificationBox;

    @Inject
    public NotificationRepository(BoxStore boxStore) {
        notificationBox = boxStore.boxFor(Notification.class);
    }

    public Observable<List<Notification>> findAll() {
        return RxQuery.observable(notificationBox.query()
                .build());
    }

    public Observable<List<Notification>> findAllSortedByDateDesc() {
        return RxQuery.observable(notificationBox.query()
                .orderDesc(Notification_.date)
                .build());
    }

    public Single<Notification> save(Notification entity) {
        return Single.fromCallable(() -> {
            notificationBox.put(entity);
            return entity;
        });
    }

    public Single<List<Notification>> save(List<Notification> entities) {
        return Single.fromCallable(() -> {
            notificationBox.put(entities);
            return entities;
        });
    }

    public Single<Notification> delete(Notification entity) {
        return Completable.fromAction(() -> notificationBox.remove(entity))
                .andThen(Single.just(entity));
    }

    public Single<List<Notification>> delete(List<Notification> entities) {
        return Completable.fromAction(() -> notificationBox.remove(entities))
                .andThen(Single.just(entities));
    }

    public Single<List<Notification>> deleteAll() {
        return Single.fromCallable(notificationBox::getAll)
                .flatMap(this::delete);
    }

    public Observable<Integer> count() {
        return RxQuery.observable(notificationBox.query()
                .build())
                .map(List::size);
    }

    public Observable<Integer> countNotReadNotifications() {
        return RxQuery.observable(notificationBox.query()
                .equal(Notification_.read, false)
                .build())
                .map(List::size);
    }
}
