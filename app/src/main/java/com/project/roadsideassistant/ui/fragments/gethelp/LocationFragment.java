package com.project.roadsideassistant.ui.fragments.gethelp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.gms.common.api.ApiException;
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
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.button.MaterialButton;
import com.project.roadsideassistant.R;
import com.project.roadsideassistant.data.models.Message;
import com.project.roadsideassistant.utils.UIHelpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class LocationFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = "LocationFragment";

    /*
     * Google map variables
     */
    private GoogleMap mGoogleMap;
    private Marker mLocationMarker;
    private LatLng mLocationLatLng;

    /*
     * Registered views
     */
    private MapView mMapView;
    private AutoCompleteTextView locationAutocompleteTextView;

    private Message message;

    /*
     * Location Variables
     */
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location lastKnowLocation;
    private LocationCallback locationCallback;

    private static final int RESOLVABLE_API_RC = 78;
    private static final float DEFAULT_ZOOM = 18F;

    /*
     * Places API variables
     */
    private PlacesClient placesClient;
    private List<AutocompletePrediction> mAutocompletePredictions;

    @Override
    public void onResume() {
        super.onResume();

        startLocationUpdates();
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        fusedLocationProviderClient.requestLocationUpdates(
                getLocationRequest(),
                locationCallback,
                Looper.getMainLooper()
        );
    }

    private LocationRequest getLocationRequest() {

        return LocationRequest.create()
                .setInterval(10000)
                .setFastestInterval(5000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get the message from the previous fragment
        message = LocationFragmentArgs.fromBundle(requireArguments()).getMessage();

        //Maps stuff
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                if (locationResult == null) return;

                lastKnowLocation = locationResult.getLastLocation();

                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(lastKnowLocation.getLatitude(), lastKnowLocation.getLongitude()),
                        DEFAULT_ZOOM
                ));

                if (mLocationMarker == null) {
                    mLocationMarker = mGoogleMap.addMarker(
                            new MarkerOptions().position(
                                    new LatLng(
                                            lastKnowLocation.getLatitude(),
                                            lastKnowLocation.getLongitude()
                                    )
                            ).title("Current Location")
                    );
                } else {
                    mLocationMarker.setPosition(
                            new LatLng(
                                    lastKnowLocation.getLatitude(),
                                    lastKnowLocation.getLongitude()
                            )
                    );
                }

                fusedLocationProviderClient.removeLocationUpdates(locationCallback);
            }
        };

        Places.initialize(requireContext(), getString(R.string.google_maps_key));

        placesClient = Places.createClient(requireContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_location, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapView = view.findViewById(R.id.map);
        locationAutocompleteTextView = view.findViewById(R.id.location_name_atv);

        AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

        locationAutocompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                FindAutocompletePredictionsRequest autocompletePredictionsRequest = FindAutocompletePredictionsRequest.builder()
                        .setCountries("ke")
                        .setTypeFilter(TypeFilter.ADDRESS)
                        .setSessionToken(token)
                        .setQuery(s.toString())
                        .build();

                placesClient.findAutocompletePredictions(autocompletePredictionsRequest)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                FindAutocompletePredictionsResponse autocompletePredictionsResponse = task.getResult();

                                if (autocompletePredictionsResponse == null) return;

                                mAutocompletePredictions = autocompletePredictionsResponse.getAutocompletePredictions();

                                List<String> autocompletePredictions = new ArrayList<>();

                                for (AutocompletePrediction autocompletePrediction : mAutocompletePredictions) {
                                    autocompletePredictions.add(autocompletePrediction.getFullText(null).toString());
                                }

                                ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), R.layout.dropdown_menu_popup_item, autocompletePredictions);

                                locationAutocompleteTextView.setAdapter(adapter);
                            } else {
                                Log.e(TAG, "onTextChanged: ", task.getException());
                            }

                        });
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        locationAutocompleteTextView.setOnItemClickListener((parent, view1, position, id) -> {
            if (position >= mAutocompletePredictions.size()) return;

            AutocompletePrediction selectedAutocompletePrediction = mAutocompletePredictions.get(position);

            String selectedSuggestion = String.valueOf(locationAutocompleteTextView.getAdapter().getItem(position));
            locationAutocompleteTextView.setText(selectedSuggestion);

            closeInputManager();

            List<Place.Field> requiredDetails = Collections.singletonList(Place.Field.LAT_LNG);

            FetchPlaceRequest fetchPlaceRequest = FetchPlaceRequest
                    .builder(selectedAutocompletePrediction.getPlaceId(), requiredDetails)
                    .build();

            placesClient.fetchPlace(fetchPlaceRequest)
                    .addOnSuccessListener(fetchPlaceResponse -> {

                        Place fetchedPlace = fetchPlaceResponse.getPlace();

                        mGoogleMap.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                        fetchedPlace.getLatLng(),
                                        DEFAULT_ZOOM
                                )
                        );

                    })
                    .addOnFailureListener(exception -> {

                        if (exception instanceof ApiException) {
                            Log.e(TAG, "onItemClick: ", exception);
                        }
                    });
        });

        MaterialButton nextButton = view.findViewById(R.id.next_button);

        nextButton.setOnClickListener(v -> {

            if (mLocationLatLng == null) UIHelpers.toast("Please select a location");

            message.setLocation(mLocationLatLng.latitude, mLocationLatLng.longitude);

            message.setLocationName(locationAutocompleteTextView.getText().toString());

            LocationFragmentDirections.ActionLocationFragmentToInfoFragment action = LocationFragmentDirections.actionLocationFragmentToInfoFragment();
            action.setMessage(message);

            Navigation.findNavController(v).navigate(action);
        });
    }

    private void closeInputManager() {
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        if (imm == null) return;

        imm.hideSoftInputFromWindow(locationAutocompleteTextView.getApplicationWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mMapView.onCreate(null);
        mMapView.onResume();
        mMapView.getMapAsync(this);

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {

        MapsInitializer.initialize(requireContext());

        mGoogleMap = googleMap;

        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mGoogleMap.setMyLocationEnabled(true);

        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);

        //Add Marker
        mGoogleMap.addMarker(new MarkerOptions()
                .position(new LatLng(0.6773, 34.7796)).title("Kisii Town").snippet("Welcome to the Matoke land and weird developers"));

        CameraPosition kisii = CameraPosition.builder().target(new LatLng(0.6773, 34.7796)).zoom(18).bearing(0).tilt(45).build();

        mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(kisii));

        mGoogleMap.setOnMapClickListener(latLng -> {
            mLocationMarker.setPosition(latLng);

            mLocationLatLng = latLng;
        });

        LocationSettingsRequest locationSettingsRequest = new LocationSettingsRequest.Builder()
                .addLocationRequest(getLocationRequest())
                .build();

        SettingsClient settingsClient = LocationServices.getSettingsClient(requireContext());

        Task<LocationSettingsResponse> locationSettingsResponseTask = settingsClient.checkLocationSettings(locationSettingsRequest);

        locationSettingsResponseTask.addOnSuccessListener(locationSettingsResponse -> getDeviceLocation())
                .addOnFailureListener(e -> {
                    if (e instanceof ResolvableApiException) {
                        try {
                            ((ResolvableApiException) e).startResolutionForResult(requireActivity(), RESOLVABLE_API_RC);
                        } catch (IntentSender.SendIntentException ex) {
                            ex.printStackTrace();
                            Log.e(TAG, "onMapReady: ", ex);
                        }
                    }
                });

    }

    @SuppressLint("MissingPermission")
    private void getDeviceLocation() {
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    lastKnowLocation = location;

                    if (lastKnowLocation != null) {
                        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                new LatLng(lastKnowLocation.getLatitude(), lastKnowLocation.getLongitude()),
                                DEFAULT_ZOOM
                        ));

                        mLocationMarker = mGoogleMap.addMarker(new MarkerOptions().position(
                                new LatLng(lastKnowLocation.getLatitude(), lastKnowLocation.getLongitude())
                        ).title("Current Location"));
                    }
                })
                .addOnFailureListener(e -> Log.e(TAG, "getDeviceLocation: ", e));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == RESOLVABLE_API_RC) getDeviceLocation();
            else super.onActivityResult(requestCode, resultCode, data);
        } else Log.w(TAG, "onActivityResult: ", new Exception("Result not Okay"));

    }

    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    private void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

}
