package com.project.roadsideassistant.ui.fragments.gethelp;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.project.roadsideassistant.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CarModelFragment extends Fragment {


    public CarModelFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_car_model, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        //Car Models Dropdown
        String[] carModels = getActivity().getResources().getStringArray(R.array.car_models);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.dropdown_menu_popup_item, carModels);
        AutoCompleteTextView carModelsTV = view.findViewById(R.id.car_model_tv);
        carModelsTV.setAdapter(adapter);

        Button nextButton = view.findViewById(R.id.next_button);

        nextButton.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_carModelFragment_to_chooseServiceFragment);
        });
    }
}
