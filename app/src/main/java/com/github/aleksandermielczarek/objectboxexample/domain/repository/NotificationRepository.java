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

    public Completable delete(Notification entity) {
        return Completable.fromAction(() -> notificationBox.remove(entity));
    }

    public Observable<Integer> countNotReadNotifications() {
        return RxQuery.observable(notificationBox.query()
                .equal(Notification_.read, false)
                .build())
                .map(List::size);
    }
}
