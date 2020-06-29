package com.project.roadsideassistant.ui.fragments.gethelp;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;
import com.project.roadsideassistant.R;
import com.project.roadsideassistant.data.models.Message;

/**
 * A simple {@link Fragment} subclass.
 */
public class LocationFragment extends Fragment implements OnMapReadyCallback {
    private static final String TAG = "LocationFragment";

    private GoogleMap mGoogleMap;
    private MapView mMapView;
    private View mView;
    private Message message;


    public LocationFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get the message from the previous fragment
        assert getArguments() != null;
        message = LocationFragmentArgs.fromBundle(getArguments()).getMessage();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_location, container, false);

        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapView = mView.findViewById(R.id.map);

        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }

        MaterialButton nextButton = view.findViewById(R.id.next_button);

        nextButton.setOnClickListener(v -> {
            LocationFragmentDirections.ActionLocationFragmentToInfoFragment action = LocationFragmentDirections.actionLocationFragmentToInfoFragment();
            action.setMessage(message);
            Navigation.findNavController(v).navigate(action);
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        assert getActivity() != null;

        MapsInitializer.initialize(getActivity());

        mGoogleMap = googleMap;

        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //Add Marker
        mGoogleMap.addMarker(new MarkerOptions()
                .position(new LatLng(0.6773, 34.7796)).title("Kisii Town").snippet("Welcome to the Matoke land and weird developers"));

        CameraPosition kisii = CameraPosition.builder().target(new LatLng(0.6773, 34.7796)).zoom(18).bearing(0).tilt(45).build();

        mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(kisii));
    }
}
