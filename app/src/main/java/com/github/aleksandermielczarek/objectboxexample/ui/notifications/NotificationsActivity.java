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
        ItemTouchHelper notificationSwipeDelete = getNotificationSwipeDelete();
        notificationSwipeDelete.attachToRecyclerView(binding.notifications);

        notificationsViewModel.setViewModelListener(this);
        notificationsViewModel.registerForNotifications();
        notificationsViewModel.loadNotifications();
        notificationsViewModel.countUnreadNotifications();
    }

    private ItemTouchHelper getNotificationSwipeDelete() {
        return new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                notificationsViewModel.removeNotification(position);
            }
        });
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
}
