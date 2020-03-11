package com.project.roadsideassistant.ui.fragments.review;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.project.roadsideassistant.R;
import com.project.roadsideassistant.data.models.Message;
import com.project.roadsideassistant.ui.fragments.gethelp.GarageFragmentArgs;

public class ReviewFragment extends Fragment {

    private ReviewViewModel mViewModel;
    private Message message;

    public ReviewFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.review_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //Get the message
        assert getArguments() != null;
        message = GarageFragmentArgs.fromBundle(getArguments()).getMessage();

        //Register the views
        TextView carModelTv = view.findViewById(R.id.car_model_tv);
        TextView locationTv = view.findViewById(R.id.location_tv);
        TextView carPlateTv = view.findViewById(R.id.car_plate_tv);
        TextView descriptionTv = view.findViewById(R.id.description_tv);
        TextView garageTv = view.findViewById(R.id.garage_tv);

        assert message != null;
        carModelTv.setText(message.getCarModel());
        locationTv.setText(message.getLocationName());
        carPlateTv.setText(message.getCarPlateNumber());
        descriptionTv.setText(message.getDescription());
        garageTv.setText(message.getGarage());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ReviewViewModel.class);
    }

}
