package com.github.aleksandermielczarek.objectboxexample.ui.notifications;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.github.aleksandermielczarek.napkin.Napkin;
import com.github.aleksandermielczarek.napkin.module.NapkinActivityModule;
import com.github.aleksandermielczarek.objectboxexample.R;
import com.github.aleksandermielczarek.objectboxexample.component.AppComponent;
import com.github.aleksandermielczarek.objectboxexample.databinding.ActivityNotificationsBinding;
import com.github.aleksandermielczarek.objectboxexample.domain.data.Notification;

import javax.inject.Inject;

/**
 * Created by Aleksander Mielczarek on 10.03.2017.
 */

public class NotificationsActivity extends AppCompatActivity implements NotificationsViewModel.NotificationsViewModelListener {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Inject
    protected NotificationsViewModel notificationsViewModel;

    private ActivityNotificationsBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Napkin.<AppComponent>provideAppComponent(this)
                .with(new NapkinActivityModule(this))
                .inject(this);

        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_notifications);
        binding.setViewModel(notificationsViewModel);

        setSupportActionBar(binding.toolbar);
        ItemTouchHelper notificationSwipeDelete = new ItemTouchHelper(new NotificationSwipeDelete(notificationsViewModel));
        notificationSwipeDelete.attachToRecyclerView(binding.notifications);

        notificationsViewModel.setViewModelListener(this);
        notificationsViewModel.registerForNotifications();
        notificationsViewModel.loadNotifications();
        notificationsViewModel.countUnreadNotifications();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        notificationsViewModel.dispose();
    }

    @Override
    public void showError(Throwable throwable) {
        Snackbar.make(binding.notifications, R.string.error, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showNotificationRemoved(Notification notification) {
        Snackbar.make(binding.notifications, R.string.notification_removed, Snackbar.LENGTH_LONG)
                .setAction(R.string.undo_notification_removed, view -> notificationsViewModel.undoNotificationRemoved(notification))
                .show();
    }

    private static final class NotificationSwipeDelete extends ItemTouchHelper.SimpleCallback {

        private final NotificationsViewModel notificationsViewModel;

        NotificationSwipeDelete(NotificationsViewModel notificationsViewModel) {
            super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
            this.notificationsViewModel = notificationsViewModel;
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            notificationsViewModel.removeNotification(position);
        }
    }

}
