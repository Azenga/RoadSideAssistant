package com.project.roadsideassistant.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.project.roadsideassistant.R;
import com.project.roadsideassistant.utils.UIHelpers;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    private FirebaseAuth mAuth;

    ProgressBar registerProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Instantiating fire-base instance
        mAuth = FirebaseAuth.getInstance();

        EditText emailTxt = findViewById(R.id.email_txt);
        EditText passwordTxt = findViewById(R.id.password_txt);

        Button registerButton = findViewById(R.id.register_button);
        TextView gotoLoginTv = findViewById(R.id.goto_login_tv);

        registerProgressBar = findViewById(R.id.registerProgressBar);


        //Registration
        registerButton.setOnClickListener(v -> {

            String email = emailTxt.getText().toString().trim();
            String password = passwordTxt.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                emailTxt.setError("Email is Required");
                emailTxt.requestFocus();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

                emailTxt.setError("Email is invalid");
                emailTxt.requestFocus();
                return;
            }

            if (password.length() < 6) {
                passwordTxt.setError("Password should be at least 6 characters");
                passwordTxt.requestFocus();
                return;
            }

            registerProgressBar.setVisibility(View.VISIBLE);
            registerUser(email, password);


        });

        gotoLoginTv.setOnClickListener(v -> finish());

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void registerUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    registerProgressBar.setVisibility(View.INVISIBLE);
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(this, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        finish();
                    } else {
                        assert task.getException() != null;

                        UIHelpers.toast("Registration failed: " + task.getException().getLocalizedMessage());
                        Log.e(TAG, "Authentication Failed: ", task.getException());
                    }
                });
    }
}
