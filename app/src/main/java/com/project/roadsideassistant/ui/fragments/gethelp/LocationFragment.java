package com.project.roadsideassistant.ui.fragments.gethelp;


import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.material.button.MaterialButton;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.project.roadsideassistant.R;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class LocationFragment extends Fragment implements OnMapReadyCallback {
    private static final String TAG = "LocationFragment";

    private GoogleMap mGoogleMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    private PlacesClient mPlacesClient;
    private List<Autocomplete> mPredictionList;

    private Location mLastKnownLocation;
    private LocationCallback mLocationCallback;

    private MaterialSearchBar mMaterialSearchBar;
    private View mMap;

    private final float DEFAULT_ZOOM = 18;
    private final int RESOLVABLE_API_EXCEPTION_RC = 78;


    public LocationFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_location, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        if ((ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
        } else {
            Navigation.findNavController(view).navigate(R.id.action_locationFragment_to_requestPermisionFragment);
            return;
        }

        //Register views
        mMaterialSearchBar = view.findViewById(R.id.map_search_bar);

        MaterialButton nextButton = view.findViewById(R.id.next_button);

        nextButton.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_locationFragment_to_infoFragment);
        });

        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);

        mMap = supportMapFragment.getView();

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        Places.initialize(getContext(), getString(R.string.google_maps_key));

        mPlacesClient = Places.createClient(getContext());

        AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        //Enabling your location google maps
        mGoogleMap = googleMap;
        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);

        //Moving the current location button down
        if ((mMap != null) && (mMap.findViewById(Integer.parseInt("1")) != null)) {
            View currentLocationButton = ((View) mMap.findViewById(Integer.parseInt("1")).getParent())
                    .findViewById(Integer.parseInt("2"));

            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) currentLocationButton.getLayoutParams();

            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 48, 96);
        }

        //Check GPS and Enable it
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

        SettingsClient settingsClient = LocationServices.getSettingsClient(getActivity());

        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());

        task
                .addOnSuccessListener(locationSettingsResponse -> {
                    Log.d(TAG, "onMapReady: LocationSettingsResponse => successful");
                    getDeviceLocation();

                })
                .addOnFailureListener(e -> {
                    if (e instanceof ResolvableApiException) {
                        ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                        try {
                            Log.d(TAG, "onMapReady: Trying to resolve a ResolvableApiException situation");
                            resolvableApiException.startResolutionForResult(getActivity(), RESOLVABLE_API_EXCEPTION_RC);
                        } catch (IntentSender.SendIntentException ex) {
                            ex.printStackTrace();
                            Log.e(TAG, "onMapReady: resolving an api exception", ex);
                        }
                    }
                });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESOLVABLE_API_EXCEPTION_RC) {
            Log.d(TAG, "onActivityResult: Called");
            if (resultCode == getActivity().RESULT_OK) {
                getDeviceLocation();
            }
        }
    }

    private void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: Called");

        mFusedLocationProviderClient.getLastLocation()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        mLastKnownLocation = task.getResult();

                        if (mLastKnownLocation != null) {
                            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                        } else {
                            Log.d(TAG, "getDeviceLocation: mLastKnownLocation is null");
                            LocationRequest locationRequest = LocationRequest.create();
                            locationRequest.setInterval(10000);
                            locationRequest.setFastestInterval(5000);
                            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

                            mLocationCallback = new LocationCallback() {
                                @Override
                                public void onLocationResult(LocationResult locationResult) {
                                    super.onLocationResult(locationResult);

                                    if (locationResult == null) {
                                        return;
                                    }

                                    mLastKnownLocation = locationResult.getLastLocation();
                                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                                    mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
                                }
                            };

                            mFusedLocationProviderClient.requestLocationUpdates(locationRequest, mLocationCallback, null);
                        }
                    } else {
                        Log.e(TAG, "getDeviceLocation: taskFailed", task.getException());
                    }
                });
    }
}

