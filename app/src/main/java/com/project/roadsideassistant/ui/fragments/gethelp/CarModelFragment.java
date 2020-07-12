package com.project.roadsideassistant.ui.fragments.gethelp;


import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.roadsideassistant.R;
import com.project.roadsideassistant.data.models.Message;

/**
 * A simple {@link Fragment} subclass.
 */
public class CarModelFragment extends Fragment {

    private FirebaseAuth mAuth;
    private AutoCompleteTextView carModelsATV;

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
        carModelsATV = view.findViewById(R.id.car_model_tv);
        carModelsATV.setAdapter(adapter);

        carModelsATV.setOnItemClickListener((parent, view1, position, id) -> closeKeyboard());

        Button nextButton = view.findViewById(R.id.next_button);

        nextButton.setOnClickListener(v -> {

            String model = String.valueOf(carModelsATV.getText());

            if (TextUtils.isEmpty(model)) {
                carModelsATV.requestFocus();
                carModelsATV.setError("Car model is required");
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

    private void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        if (imm != null)
            imm.hideSoftInputFromWindow(carModelsATV.getApplicationWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
    }
}
