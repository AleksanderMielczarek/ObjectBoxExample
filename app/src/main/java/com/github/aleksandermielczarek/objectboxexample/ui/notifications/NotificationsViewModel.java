package com.github.aleksandermielczarek.objectboxexample.ui.notifications;

import android.databinding.ObservableInt;
import android.support.v4.util.Pair;
import android.support.v7.util.DiffUtil;

import com.github.aleksandermielczarek.objectboxexample.BR;
import com.github.aleksandermielczarek.objectboxexample.R;
import com.github.aleksandermielczarek.objectboxexample.domain.model.NotificationModel;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import me.tatarka.bindingcollectionadapter2.ItemBinding;
import me.tatarka.bindingcollectionadapter2.collections.DiffObservableList;

/**
 * Created by Aleksander Mielczarek on 10.03.2017.
 */

public class NotificationsViewModel {

    public final DiffObservableList<NotificationViewModel> notifications = new DiffObservableList<>(new DiffObservableList.Callback<NotificationViewModel>() {
        @Override
        public boolean areItemsTheSame(NotificationViewModel notificationViewModel, NotificationViewModel notificationViewModel2) {
            return notificationViewModel.notification.get().getId() == notificationViewModel2.notification.get().getId();
        }

        @Override
        public boolean areContentsTheSame(NotificationViewModel notificationViewModel, NotificationViewModel notificationViewModel2) {
            return notificationViewModel.notification.get().isRead() == notificationViewModel2.notification.get().isRead();
        }
    });
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
                .flatMapSingle(notifications -> Observable.fromIterable(notifications)
                        .map(notification -> viewModelFactory.create(notification, disposables, viewModelListener))
                        .toList())
                .map(newNotifications -> {
                    DiffUtil.DiffResult diffResult = notifications.calculateDiff(newNotifications);
                    return Pair.create(newNotifications, diffResult);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> notifications.update(result.first, result.second), viewModelListener::showError));
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

    public void removeNotification(int position) {
        notifications.get(position).remove();
    }

    public void dispose() {
        disposables.clear();
    }

    public interface NotificationsViewModelListener {

        void showError(Throwable throwable);
    }
}
