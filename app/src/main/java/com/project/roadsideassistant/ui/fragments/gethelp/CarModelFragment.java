package com.project.roadsideassistant.ui.fragments.gethelp;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.roadsideassistant.R;
import com.project.roadsideassistant.data.models.Message;
import com.project.roadsideassistant.ui.fragments.gethelp.service.ChooseServiceFragmentDirections;

/**
 * A simple {@link Fragment} subclass.
 */
public class CarModelFragment extends Fragment {

    private FirebaseAuth mAuth;

    public CarModelFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Initialize fire-base instances
        mAuth = FirebaseAuth.getInstance();

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_car_model, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        //Car Models Dropdown
        assert getActivity() != null;

        String[] carModels = getActivity().getResources().getStringArray(R.array.car_models);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.dropdown_menu_popup_item, carModels);
        AutoCompleteTextView carModelsTV = view.findViewById(R.id.car_model_tv);
        carModelsTV.setAdapter(adapter);

        Button nextButton = view.findViewById(R.id.next_button);

        nextButton.setOnClickListener(v -> {

            String model = String.valueOf(carModelsTV.getText());

            if (TextUtils.isEmpty(model)) {
                carModelsTV.requestFocus();
                carModelsTV.setError("Car model is required");
                return;
            }

            FirebaseUser currentUser = mAuth.getCurrentUser();

            if (currentUser == null) return;

            //Instantiate the message to be sent
            Message message = new Message(model, currentUser.getUid(), false);

            //Set the message to the action
            CarModelFragmentDirections.ActionCarModelFragmentToChooseServiceFragment action = CarModelFragmentDirections.actionCarModelFragmentToChooseServiceFragment();
            action.setMessage(message);

            Navigation.findNavController(v).navigate(action);

        });
    }
}
