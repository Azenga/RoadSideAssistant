package com.project.roadsideassistant.ui.fragments.notifications;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.project.roadsideassistant.R;
import com.project.roadsideassistant.utils.UIHelpers;

public class NotificationsFragment extends Fragment {

    private FirebaseAuth mAuth;
    private RecyclerView notificationRecyclerView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.notifications_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        notificationRecyclerView = view.findViewById(R.id.notifications_recycler_view);

        notificationRecyclerView.setHasFixedSize(true);
        notificationRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        notificationRecyclerView.addItemDecoration(
                new DividerItemDecoration(
                        requireContext(),
                        LinearLayoutManager.VERTICAL
                )
        );

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        assert mAuth.getCurrentUser() != null;

        NotificationViewModelFactory factory = new NotificationViewModelFactory(mAuth.getCurrentUser().getUid());

        NotificationsViewModel mViewModel = new ViewModelProvider(this, factory).get(NotificationsViewModel.class);

        mViewModel.getNotifications().observe(getViewLifecycleOwner(), notifications -> {
            NotificationsAdapter adapter = new NotificationsAdapter(notifications);
            notificationRecyclerView.setAdapter(adapter);

        });

        mViewModel.getE().observe(getViewLifecycleOwner(), e -> UIHelpers.toast(e.getLocalizedMessage()));

    }

}
