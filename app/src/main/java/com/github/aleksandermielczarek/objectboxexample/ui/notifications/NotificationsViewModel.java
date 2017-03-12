package com.github.aleksandermielczarek.objectboxexample.ui.notifications;

import android.databinding.ObservableInt;

import com.github.aleksandermielczarek.objectboxexample.BR;
import com.github.aleksandermielczarek.objectboxexample.R;
import com.github.aleksandermielczarek.objectboxexample.domain.data.Notification;
import com.github.aleksandermielczarek.objectboxexample.domain.model.NotificationModel;
import com.github.aleksandermielczarek.objectboxexample.ui.util.ViewModelDiff;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import me.tatarka.bindingcollectionadapter2.ItemBinding;
import me.tatarka.bindingcollectionadapter2.collections.DiffObservableList;

/**
 * Created by Aleksander Mielczarek on 10.03.2017.
 */

public class NotificationsViewModel {

    public final DiffObservableList<NotificationViewModel> notifications = new DiffObservableList<>(ViewModelDiff.viewModelIdEqualsCallback(Notification::getId, notificationViewModel -> notificationViewModel.notification.get()));
    public final ItemBinding<NotificationViewModel> notificationItemBinding = ItemBinding.of(BR.viewModel, R.layout.item_notification);
    public final ObservableInt unreadNotifications = new ObservableInt();

    private final NotificationModel notificationModel;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final NotificationViewModelFactory viewModelFactory;

    private NotificationsViewModelListener viewModelListener;

    @Inject
    public NotificationsViewModel(NotificationModel notificationModel, NotificationViewModelFactory viewModelFactory) {
        this.notificationModel = notificationModel;
        this.viewModelFactory = viewModelFactory;
    }

    public void setViewModelListener(NotificationsViewModelListener viewModelListener) {
        this.viewModelListener = viewModelListener;
    }

    public void loadNotifications() {
        disposables.add(notificationModel.getNotifications()
                .flatMapSingle(ViewModelDiff.modelsToViewModels(notification -> viewModelFactory.create(notification, disposables, viewModelListener)))
                .map(ViewModelDiff.calculateViewModelsDiff(notifications))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ViewModelDiff::updateList, viewModelListener::showError));
    }

    public void countUnreadNotifications() {
        disposables.add(notificationModel.countUnreadNotifications()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(unreadNotifications::set, viewModelListener::showError));
    }

    public void registerForNotifications() {
        disposables.add(notificationModel.registerForNotifications()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                }, viewModelListener::showError));
    }

    public void undoNotificationRemoved(Notification notification) {
        disposables.add(notificationModel.addNotification(notification)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                }, viewModelListener::showError));
    }

    public void removeNotification(int position) {
        notifications.get(position).remove();
    }

    public void dispose() {
        disposables.clear();
    }

    public interface NotificationsViewModelListener {

        void showError(Throwable throwable);

        void showNotificationRemoved(Notification notification);
    }
}
