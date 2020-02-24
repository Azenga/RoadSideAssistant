package com.project.roadsideassistant.ui.fragments.profile;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.roadsideassistant.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    private ProfileViewModel mViewModel;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private CircleImageView avatarCIV;
    private TextView displayNameTV, emailTV, phoneTV, emailNotVerifiedTV;
    private MaterialButton setupAccountButton;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.profile_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Register the vies associated
        displayNameTV = view.findViewById(R.id.display_name_tv);
        emailTV = view.findViewById(R.id.email_tv);
        phoneTV = view.findViewById(R.id.phone_tv);
        emailNotVerifiedTV = view.findViewById(R.id.email_not_verified_tv);
        setupAccountButton = view.findViewById(R.id.setup_account_button);
        avatarCIV = view.findViewById(R.id.avatar_civ);

        setupAccountButton.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_profileFragment_to_setupAccountFragment);
        });

        //Load the image to the image view
        Glide.with(this)
                .load(currentUser.getPhotoUrl())
                .centerCrop()
                .placeholder(R.drawable.ic_account_circle_white)
                .into(avatarCIV);

        //Set display Name
        if (currentUser.getDisplayName() != null) {
            displayNameTV.setText(currentUser.getDisplayName());
        }

        //Setting the authenticated email address
        emailTV.setText(currentUser.getEmail());

        //Check user phone number
        if (currentUser.getPhoneNumber() != null) {
            phoneTV.setText(currentUser.getPhoneNumber());
        }

        //Checking whether whether the authenticated user email is verified and display or hiding the barge if necessary
        if (currentUser.isEmailVerified()) {
            emailNotVerifiedTV.setVisibility(View.INVISIBLE);
        } else {
            emailNotVerifiedTV.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        mAuth = FirebaseAuth.getInstance();

        currentUser = mAuth.getCurrentUser();
    }
}
