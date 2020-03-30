package com.project.roadsideassistant.ui.fragments.home;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.roadsideassistant.R;
import com.project.roadsideassistant.ui.HomeActivity;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {

    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        HomeViewModel mViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        //Initialize firebase instances
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        assert currentUser != null;

        Button getStaredButton = view.findViewById(R.id.get_started_button);

        getStaredButton.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_carModelFragment);
        });

        CircleImageView avatarCiv = view.findViewById(R.id.avatar_civ);

        Glide.with(this)
                .load(currentUser.getPhotoUrl())
                .centerCrop()
                .placeholder(R.drawable.ic_account_circle)
                .into(avatarCiv);

        TextView nameTv = view.findViewById(R.id.name_tv);

        if (!TextUtils.isEmpty(currentUser.getDisplayName()))
            nameTv.setText(currentUser.getDisplayName());
        TextView emailTv = view.findViewById(R.id.email_tv);
        emailTv.setText(currentUser.getEmail());

        CardView servicesCardView = view.findViewById(R.id.services_card_view);
        servicesCardView.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.servicesFragment));

        CardView productsCardView = view.findViewById(R.id.products_card_view);
        productsCardView.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.productsFragment));

        CardView notificationsCardView = view.findViewById(R.id.notifications_card_view);
        notificationsCardView.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.notificationsFragment));

        CardView aboutUsCardView = view.findViewById(R.id.about_us_card_view);
        aboutUsCardView.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.aboutFragment));

        CardView contactCardView = view.findViewById(R.id.contact_card_view);
        contactCardView.setOnClickListener(v -> initiateCall());

        CardView profileCardView = view.findViewById(R.id.profile_card_view);
        profileCardView.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.profileFragment));

    }

    private void initiateCall() {

        assert getContext() != null;
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:+254700521163"));
            startActivity(intent);
        } else {
            assert getActivity() != null;
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, HomeActivity.CALL_PHONE_RC);
        }
    }
}
