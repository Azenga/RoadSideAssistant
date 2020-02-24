package com.project.roadsideassistant.ui;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.button.MaterialButton;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.project.roadsideassistant.R;

import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "MapActivity";

    private GoogleMap mGoogleMap;
    //Searching current devie location
    private FusedLocationProviderClient mLocationProviderClient;

    //Loading the suggestions
    private PlacesClient mPlacesClient;

    //List to save the predictions
    List<AutocompletePrediction> autocompletePredictionList;

    private Location lastKnownLocation;
    private LocationCallback locationCallback;

    //Views
    private MaterialSearchBar materialSearchBar;
    private View mapView;
    private MaterialButton nextButton;

    private final float DEFAULT_ZOOM = 18;
    private final int RESOLVABLE_API_EXCEPTION_RC = 78;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        //Check Permissions and do the necessary
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            Log.d(TAG, "onCreate: application has sufficient permissions");

        } else {
            startActivity(new Intent(this, RequestPermissionActivity.class));
            return;
        }

        //Register the views
        materialSearchBar = findViewById(R.id.material_search_bar);
        nextButton = findViewById(R.id.next_button);

        //Get the map fragment and set the callback that will run when the map loads
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);

        mapView = supportMapFragment.getView();

        AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

        mLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        Places.initialize(this, getString(R.string.google_api_key));

        mPlacesClient = Places.createClient(this);

        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {

            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                startSearch(text.toString(), true, null, true);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

                if (buttonCode == MaterialSearchBar.BUTTON_NAVIGATION) {
                    //Implement navigation => Opening an closing a navigation drawer
                } else if (buttonCode == MaterialSearchBar.BUTTON_BACK) {
                    materialSearchBar.closeSearch();

                }

            }
        });

        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //For global access
        mGoogleMap = googleMap;

        //Set default
        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);

        //Change the position of my location button
        if ((mapView != null) && (mapView.findViewById(Integer.parseInt("1")) != null)) {
            View myLocationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));

            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) myLocationButton.getLayoutParams();

            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);

            Resources r = getResources();
            int marginEnd = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, r.getDisplayMetrics());
            int marginBottom = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 64, r.getDisplayMetrics());
            layoutParams.setMargins(0, 0, marginEnd, marginBottom);
        }

        //Check if GPS is enabled and enable it if not
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

        SettingsClient settingsClient = LocationServices.getSettingsClient(this);

        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());

        task.
                addOnSuccessListener(locationSettingsResponse -> {
                    getDeviceLocation();
                })
                .addOnFailureListener(e -> {

                    if (e instanceof ResolvableApiException) {
                        ResolvableApiException exception = (ResolvableApiException) e;
                        try {
                            ((ResolvableApiException) e).startResolutionForResult(this, RESOLVABLE_API_EXCEPTION_RC);
                        } catch (IntentSender.SendIntentException ex) {
                            Log.e(TAG, "onMapReady: trying to resolve an exception", e);
                        }
                    }

                });
    }

    private void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: Device Location is requested");
        mLocationProviderClient.getLastLocation()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        lastKnownLocation = task.getResult();
                        if (lastKnownLocation != null) {
                            mGoogleMap.moveCamera(
                                    CameraUpdateFactory.newLatLngZoom(new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()), DEFAULT_ZOOM)
                            );
                        } else {

                            LocationRequest locationRequest = LocationRequest.create();
                            locationRequest.setInterval(10000);
                            locationRequest.setFastestInterval(5000);
                            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

                            locationCallback = new LocationCallback() {
                                @Override
                                public void onLocationResult(LocationResult locationResult) {
                                    super.onLocationResult(locationResult);

                                    if (locationResult == null)
                                        return;
                                    lastKnownLocation = locationResult.getLastLocation();

                                    mGoogleMap.moveCamera(
                                            CameraUpdateFactory.newLatLngZoom(new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()), DEFAULT_ZOOM)
                                    );

                                    mLocationProviderClient.removeLocationUpdates(locationCallback);

                                }
                            };

                            mLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);

                        }

                    } else {
                        Log.e(TAG, "getDeviceLocation: unsuccessful", task.getException());
                        Toast.makeText(this, R.string.location_exception_message, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESOLVABLE_API_EXCEPTION_RC && resultCode == RESULT_OK) {
            getDeviceLocation();
        }
    }
}
