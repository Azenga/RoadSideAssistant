package com.project.roadsideassistant.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.roadsideassistant.R;
import com.project.roadsideassistant.utils.UIHelpers;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private FirebaseAuth mAuth;
    private ProgressBar loginProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Getting firebase auth instance
        mAuth = FirebaseAuth.getInstance();

        EditText emailTxt = findViewById(R.id.email_txt);
        EditText passwordTxt = findViewById(R.id.password_txt);

        Button loginButton = findViewById(R.id.login_button);
        TextView gotoRegisterTv = findViewById(R.id.goto_register_tv);

        loginProgressBar = findViewById(R.id.loginProgressBar);

        gotoRegisterTv.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        loginButton.setOnClickListener(v -> {

            String email = String.valueOf(emailTxt.getText());
            String password = String.valueOf(passwordTxt.getText());

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

                emailTxt.setError("Email is invalid");
                emailTxt.requestFocus();
                return;
            }

            if (password.length() < 6) {
                passwordTxt.setError("Password should be at least 6 chars");
                passwordTxt.requestFocus();
                return;
            }

            loginUser(email, password);
        });
    }

    private void loginUser(String email, String password) {
        loginProgressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    loginProgressBar.setVisibility(View.INVISIBLE);
                    if (task.isSuccessful()) {
                        sendHome();
                    } else {

                        assert task.getException() != null;

                        UIHelpers.toast("Login failed " + task.getException().getLocalizedMessage());
                        Log.e(TAG, "Authentication Failed: ", task.getException());
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) sendHome();
    }

    private void sendHome() {

        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();

    }

}
