package com.project.roadsideassistant.ui.fragments.profile;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.project.roadsideassistant.R;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetupAccountFragment extends Fragment {

    private static final String TAG = "SetupAccountFragment";

    private SetupAccountViewModel mViewModel;

    private CircleImageView avatarCIV;
    private TextInputEditText displayNameTXT;
    private MaterialButton updateAccountButton;
    private ProgressBar updateProgressBar;

    private final int IMAGE_CAPTURE_RC = 22;

    //Required firebase instances
    private FirebaseAuth mAuth;
    private FirebaseStorage mStorage;
    private FirebaseUser currentUser;

    private boolean avatarHasChanged = false;
    private Bitmap avatarBitmap = null;

    public static SetupAccountFragment newInstance() {
        return new SetupAccountFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.setup_account_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Register Views
        avatarCIV = view.findViewById(R.id.avatar_civ);
        displayNameTXT = view.findViewById(R.id.display_name_txt);
        updateAccountButton = view.findViewById(R.id.update_account_button);
        updateProgressBar = view.findViewById(R.id.update_progress_bar);

        avatarCIV.setOnClickListener(v -> {
            takePicture();
        });

        //Populate the previous display name firs
        displayNameTXT.setText(currentUser.getDisplayName());


        //@todo Display the previous image using glide if it exists


        //Setup the button click listener to update the account details where necessary
        updateAccountButton.setOnClickListener(v -> {
            updateAccountDetails();
        });

    }


    /**
     * Gets user input from the views, both image and the display name ready for uploading
     */
    private void updateAccountDetails() {

        String displayName = displayNameTXT.getText().toString().trim();

        if (TextUtils.isEmpty(displayNameTXT.getText())) {
            displayNameTXT.setError("Display Name is Required");
            displayNameTXT.requestFocus();
            return;
        }

        updateProgressBar.setVisibility(View.VISIBLE);

        if (avatarHasChanged) {
            //Get byte array fo=ro bitmap
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            avatarBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] avatarArrayBytes = baos.toByteArray();

            StorageReference avatarReference = mStorage.getReference("avatars/" + currentUser.getUid());

            UploadTask avatarUploadTask = avatarReference
                    .putBytes(avatarArrayBytes);

            avatarUploadTask
                    .addOnSuccessListener(taskSnapshot -> {
                        avatarReference.getDownloadUrl()
                                .addOnSuccessListener(uri -> {
                                    uploadTheUpdates(uri, displayName);
                                })
                                .addOnFailureListener(e -> {
                                    Log.e(TAG, "updateAccountDetails: getdowmloadUrl => Failed", e);
                                });
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "updateAccountDetails: Failed", e);
                    })
                    .addOnPausedListener(taskSnapshot -> {
                        //Show progress on a progress bar
                    });
        } else {
            uploadTheUpdates(null, displayName);
        }

    }

    /**
     * Uploads the changed name or the uri or both
     *
     * @param uri
     * @param displayName
     */
    private void uploadTheUpdates(Uri uri, String displayName) {

        UserProfileChangeRequest updates = null;

        if (avatarHasChanged && uri != null) {
            updates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName)
                    .setPhotoUri(uri)
                    .build();
        } else {
            updates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName)
                    .build();
        }

        currentUser.updateProfile(updates)
                .addOnCompleteListener(task -> {
                    updateProgressBar.setVisibility(View.INVISIBLE);
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "User Profile Updated", Toast.LENGTH_LONG).show();
                    } else {
                        Log.e(TAG, "updateAccountDetails: updating User details: failed", task.getException());
                    }
                });
    }

    /**
     * Opens the camera intent for taking picture
     */
    private void takePicture() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        ComponentName componentName = intent.resolveActivity(getActivity().getPackageManager());

        if (componentName != null) {
            startActivityForResult(intent, IMAGE_CAPTURE_RC);
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SetupAccountViewModel.class);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == IMAGE_CAPTURE_RC) {

                //Getting the image from extras sets it to a global bitmap and to the avatar image view
                avatarBitmap = (Bitmap) data.getExtras().get("data");
                avatarCIV.setImageBitmap(avatarBitmap);

                //Changed the flag of whether the image has changed or not
                avatarHasChanged = true;

            }
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        //Initializes firebase instances
        mAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance();

        currentUser = mAuth.getCurrentUser();
    }
}
