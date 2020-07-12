package com.project.roadsideassistant.ui.fragments.review;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.project.roadsideassistant.R;
import com.project.roadsideassistant.data.models.Message;
import com.project.roadsideassistant.ui.fragments.gethelp.GarageFragmentArgs;
import com.project.roadsideassistant.utils.UIHelpers;

public class ReviewFragment extends Fragment {
    private static final String TAG = "ReviewFragment";

    private ReviewViewModel mViewModel;
    private Message message;

    private NavController navController;

    public ReviewFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get the message
        message = GarageFragmentArgs.fromBundle(requireArguments()).getMessage();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.review_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        navController = Navigation.findNavController(view);

        //Register the views
        TextView carModelTv = view.findViewById(R.id.car_model_tv);
        TextView locationTv = view.findViewById(R.id.location_tv);
        TextView carPlateTv = view.findViewById(R.id.car_plate_tv);
        TextView descriptionTv = view.findViewById(R.id.description_tv);
        TextView garageTv = view.findViewById(R.id.garage_tv);

        RecyclerView servicesRecyclerView = view.findViewById(R.id.selected_services_recycler_view);
        servicesRecyclerView.setAdapter(new SelectedItemsAdapter(message.getServicesList(), "services"));

        RecyclerView productsRecyclerView = view.findViewById(R.id.selected_products_recycler_view);
        productsRecyclerView.setAdapter(new SelectedItemsAdapter(message.getProductsList(), "products"));


        assert message != null;

        carModelTv.setText(message.getCarModel());
        locationTv.setText(message.getLocationName());
        carPlateTv.setText(message.getCarPlateNumber());
        descriptionTv.setText(message.getDescription());
        if (TextUtils.isEmpty(message.getGarage()))
            garageTv.setText(getResources().getString(R.string.no_garage));
        else
            garageTv.setText(message.getGarage());

        Button sendRequestBtn = view.findViewById(R.id.send_request_btn);

        sendRequestBtn.setOnClickListener(v -> sendMessage());
    }

    private void sendMessage() {
        Log.d(TAG, "sendMessage: started sending message");
        UIHelpers.toast("Sending message...");
        mViewModel.addMessage(message);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(ReviewViewModel.class);

        mViewModel.getSuccessMessage().observe(getViewLifecycleOwner(), successMessage -> {
            UIHelpers.toast(successMessage);
            navController.navigate(R.id.action_message_send);
        });
        mViewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            Log.e(TAG, "onActivityCreated: message not added" + errorMessage);
            UIHelpers.toast(errorMessage);
        });

    }

}
