package com.project.roadsideassistant.ui.fragments.notifications;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.roadsideassistant.data.models.Notification;
import com.project.roadsideassistant.data.repositories.NotificationRepository;

import java.util.List;

public class NotificationsViewModel extends ViewModel implements NotificationRepository.NotificationTaskListener {

    private MutableLiveData<List<Notification>> _notifications = new MutableLiveData<>();
    private MutableLiveData<Exception> _e = new MutableLiveData<>();

    public NotificationsViewModel(String userId) {
        NotificationRepository notificationRepository = new NotificationRepository(this);

        notificationRepository.getByUserId(userId);
    }

    @Override
    public void onGetAll(List<Notification> notifications) {
        _notifications.setValue(notifications);
    }

    @Override
    public void onError(Exception e) {
        _e.setValue(e);
    }

    public LiveData<List<Notification>> getNotifications() {
        return _notifications;
    }

    public LiveData<Exception> getE() {
        return _e;
    }
}
