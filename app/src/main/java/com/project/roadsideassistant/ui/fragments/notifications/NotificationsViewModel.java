package com.project.roadsideassistant.ui.fragments.notifications;

import androidx.lifecycle.ViewModel;

import com.project.roadsideassistant.data.repositories.UserRepository;

public class NotificationsViewModel extends ViewModel {

    String displayName, email, phone;

    private UserRepository userRepository;


}
