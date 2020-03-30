package com.project.roadsideassistant.ui.fragments.services;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.roadsideassistant.R;

public class ServicesFragment extends Fragment {
    private static final String TAG = "ServicesFragment";

    private RecyclerView servicesRecyclerView;

    private ServicesAdapter servicesAdapter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.services_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        servicesRecyclerView = view.findViewById(R.id.services_recycler_view);
        servicesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        assert getContext() != null;
        servicesRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        servicesRecyclerView.setHasFixedSize(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        assert getActivity() != null;
        ServicesViewModel servicesViewModel = new ViewModelProvider(getActivity()).get(ServicesViewModel.class);

        servicesViewModel.getServices().observe(getViewLifecycleOwner(), services -> {

            servicesAdapter = new ServicesAdapter(services);

            servicesRecyclerView.setAdapter(servicesAdapter);

        });
    }
}
