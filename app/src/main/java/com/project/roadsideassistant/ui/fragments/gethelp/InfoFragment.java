package com.project.roadsideassistant.ui.fragments.gethelp;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.project.roadsideassistant.R;
import com.project.roadsideassistant.data.models.Message;

public class InfoFragment extends Fragment {
    private static final String TAG = "InfoFragment";

    public InfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Get all the views
        TextInputEditText carPlateTxt = view.findViewById(R.id.car_plate_txt);
        TextInputEditText descriptionTxt = view.findViewById(R.id.description_txt);

        //Get the message from the location fragment
        assert getArguments() != null;

        Message message = InfoFragmentArgs.fromBundle(getArguments()).getMessage();


        MaterialButton nextButton = view.findViewById(R.id.next_button);

        nextButton.setOnClickListener(v -> {

            String carPlate = String.valueOf(carPlateTxt.getText());
            String description = String.valueOf(descriptionTxt.getText());

            if (TextUtils.isEmpty(carPlate)) {
                carPlateTxt.setError("Car Plate is Required");
                carPlateTxt.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(description)) {
                descriptionTxt.setError("Description is Required");
                descriptionTxt.requestFocus();
                return;
            }

            assert message != null;
            message.setCarPlateNumber(carPlate);
            message.setDescription(description);

            InfoFragmentDirections.ActionInfoFragmentToGarageFragment action = InfoFragmentDirections.actionInfoFragmentToGarageFragment();
            action.setMessage(message);
            Navigation.findNavController(v).navigate(action);

        });
        assert message != null;
        Log.d(TAG, "onViewCreated: location name: " + message.getLocationName());

    }
}
