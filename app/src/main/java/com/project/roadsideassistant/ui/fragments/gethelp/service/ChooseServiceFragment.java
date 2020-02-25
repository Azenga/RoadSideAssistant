package com.project.roadsideassistant.ui.fragments.gethelp.service;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.project.roadsideassistant.R;

public class ChooseServiceFragment extends Fragment {

    private RecyclerView chooseServiceRecyclerView;

    private ChooseServiceAdapter chooseServiceAdapter;

    public ChooseServiceFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_choose, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Get and dod presets on the recycler view
        chooseServiceRecyclerView = view.findViewById(R.id.choose_service_recycler_view);
        chooseServiceRecyclerView.setHasFixedSize(true);
        chooseServiceRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //Register the button and it's click listener
        Button nextButton = view.findViewById(R.id.next_button);

        nextButton.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_chooseServiceFragment_to_mapActivity);
        });


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ChooseServiceFragmentViewModel viewModel = new ViewModelProvider(this).get(ChooseServiceFragmentViewModel.class);

        viewModel.getServices().observe(getViewLifecycleOwner(), services -> {

            chooseServiceAdapter = new ChooseServiceAdapter(services);

            chooseServiceRecyclerView.setAdapter(chooseServiceAdapter);

        });

    }
}
