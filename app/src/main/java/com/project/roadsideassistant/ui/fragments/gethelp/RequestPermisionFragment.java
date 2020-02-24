package com.project.roadsideassistant.ui.fragments.gethelp;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.project.roadsideassistant.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class RequestPermisionFragment extends Fragment {

    private static final String TAG = "RequestPermisionFragmen";
    private static final int LOCATION_PERMISSION_RC = 22;


    public RequestPermisionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_request_permision, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if ((ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            getActivity().onBackPressed();
            return;
        }

        //Register Views
        Button requestPermissionButton = view.findViewById(R.id.permission_request_button);

        requestPermissionButton.setOnClickListener(v -> {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_RC);
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

            switch (requestCode) {
                case LOCATION_PERMISSION_RC:
                    Log.d(TAG, "onRequestPermissionsResult: granted");
                    getActivity().onBackPressed();
                    break;
                default:
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        } else {
            Log.d(TAG, "onRequestPermissionsResult: denied");
            Toast.makeText(getActivity(), R.string.permission_rejected, Toast.LENGTH_SHORT).show();
        }
    }
}
