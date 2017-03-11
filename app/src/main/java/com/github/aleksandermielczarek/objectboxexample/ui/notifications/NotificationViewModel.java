package com.github.aleksandermielczarek.objectboxexample.ui.notifications;

import android.databinding.ObservableField;

import com.github.aleksandermielczarek.objectboxexample.domain.data.Notification;
import com.github.aleksandermielczarek.objectboxexample.domain.model.NotificationModel;
import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Aleksander Mielczarek on 10.03.2017.
 */
@AutoFactory
public class NotificationViewModel {

    public final ObservableField<Notification> notification = new ObservableField<>();

    private final NotificationModel notificationModel;
    private final CompositeDisposable disposables;
    private final NotificationsViewModel.NotificationsViewModelListener viewModelListener;

    protected NotificationViewModel(Notification notification, CompositeDisposable disposables, NotificationsViewModel.NotificationsViewModelListener viewModelListener, @Provided NotificationModel notificationModel) {
        this.viewModelListener = viewModelListener;
        this.notification.set(notification);
        this.notificationModel = notificationModel;
        this.disposables = disposables;
    }

    public void remove() {
        disposables.add(notificationModel.deleteNotification(notification.get())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                }, viewModelListener::showError));
    }

    public void read() {
        disposables.add(notificationModel.readNotification(notification.get())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess(notification::set)
                .toCompletable()
                .subscribe(notification::notifyChange, viewModelListener::showError));
    }
}
