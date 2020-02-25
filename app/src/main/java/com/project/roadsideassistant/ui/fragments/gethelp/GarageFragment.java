package com.project.roadsideassistant.ui.fragments.gethelp;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.project.roadsideassistant.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class GarageFragment extends Fragment {


    public GarageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_garage, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Grages Dropdown
        String[] garages = getActivity().getResources().getStringArray(R.array.garares);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.dropdown_menu_popup_item, garages);
        AutoCompleteTextView carModelsTV = view.findViewById(R.id.garages_atv);
        carModelsTV.setAdapter(adapter);

    }
}
