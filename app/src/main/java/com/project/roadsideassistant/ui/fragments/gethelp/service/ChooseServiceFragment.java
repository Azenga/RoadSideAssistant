package com.project.roadsideassistant.ui.fragments.gethelp.service;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.project.roadsideassistant.R;
import com.project.roadsideassistant.data.models.Message;
import com.project.roadsideassistant.data.models.Service;

public class ChooseServiceFragment extends Fragment {
    private static final String TAG = "ChooseServiceFragment";

    private RecyclerView chooseServiceRecyclerView;

    private ChooseServiceAdapter chooseServiceAdapter;
    private Message message;

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
        assert getContext() != null;
        chooseServiceRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        //Register the button and it's click listener
        Button nextButton = view.findViewById(R.id.next_button);

        assert getArguments() != null;

        //Getting the passed variables
        message = ChooseServiceFragmentArgs.fromBundle(getArguments()).getMessage();

        assert message != null;
        Log.d(TAG, "onViewCreated: car model : " + message.getCarModel());

        nextButton.setOnClickListener(v -> {

            for (Service service : chooseServiceAdapter.getCheckedServices()) {
                message.addProduct(service.getId());
            }

            Log.d(TAG, "onViewCreated: message services count: " + message.getServicesList().size());

            ChooseServiceFragmentDirections.ActionChooseServiceFragmentToChooseProductFragment action = ChooseServiceFragmentDirections.actionChooseServiceFragmentToChooseProductFragment();
            action.setMessage(message);

            Navigation.findNavController(v).navigate(action);
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
