package com.project.roadsideassistant.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.roadsideassistant.R;
import com.project.roadsideassistant.utils.UIHelpers;

import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";

    private FirebaseAuth mAuth;
    private AppBarConfiguration appBarConfiguration;

    //Drawer Header Views
    private CircleImageView userAvatarCIV;
    private TextView emailTV;
    public static final int CALL_PHONE_RC = 22;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Instantiating database auth
        mAuth = FirebaseAuth.getInstance();

        //Register the custom toolbar as the action bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //NavController Setup with action bar
        NavController navController = Navigation.findNavController(this, R.id.fragment);

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).setOpenableLayout(drawerLayout).build();
        appBarConfiguration.getTopLevelDestinations().addAll(
                Arrays.asList(
                        R.id.homeFragment,
                        R.id.profileFragment,
                        R.id.servicesFragment,
                        R.id.productsFragment,
                        R.id.notificationsFragment,
                        R.id.aboutFragment
                )
        );


        //Navigation View
        NavigationView navView = findViewById(R.id.nav_view);

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        NavigationUI.setupWithNavController(navView, navController);

        //Register the drawer header view
        View headerLayout = navView.getHeaderView(0);
        userAvatarCIV = headerLayout.findViewById(R.id.avatar_civ);
        emailTV = headerLayout.findViewById(R.id.email_tv);

    }

    @Override
    public boolean onSupportNavigateUp() {

        NavController navController = Navigation.findNavController(this, R.id.fragment);

        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.logout_option:

                AlertDialog alertDialog = new AlertDialog.Builder(this).create();

                alertDialog.setTitle("Road Assistant");
                alertDialog.setMessage("Are you sure you want to exit?");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", (dialog, which) -> {
                    Log.d(TAG, "onOptionsItemSelected: Positive Button");
                    logoutUser();
                });

                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", ((dialog, which) -> {
                    Log.d(TAG, "onOptionsItemSelected: Negative Button");
                    UIHelpers.toast("Operation cancelled");
                }));

                alertDialog.show();
                return true;
            case R.id.call_agent:

                initiateCall();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void initiateCall() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:+254700521163"));
            startActivity(intent);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, CALL_PHONE_RC);
        }
    }

    private void logoutUser() {

        mAuth.signOut();
        sendToLogin();
    }

    /**
     * Sends the user to the authentication activity
     */
    private void sendToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Getting the current authenticated user
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            sendToLogin();
        } else {
            updateUI(currentUser);
        }
    }

    private void updateUI(FirebaseUser currentUser) {

        emailTV.setText(currentUser.getEmail());

        Glide.with(this)
                .load(currentUser.getPhotoUrl())
                .centerCrop()
                .placeholder(R.drawable.ic_account_circle_white)
                .into(userAvatarCIV);
    }
}
