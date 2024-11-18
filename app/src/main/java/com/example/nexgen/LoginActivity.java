package com.example.nexgen;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nexgen.HomeActivity; // Make sure to replace this with your actual home activity
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText logEmail, logPassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        logEmail = findViewById(R.id.log_email);
        logPassword = findViewById(R.id.log_password);
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.log_home_nav).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }

    private void loginUser() {
        String email = logEmail.getText().toString().trim();
        String password = logPassword.getText().toString().trim();

        //Check if the email and password are not empty
        if (TextUtils.isEmpty(email)) {
            logEmail.setError("Email is required.");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            logPassword.setError("Password is required.");
            return;
        }

        //Login the user
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        //if success
                        Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish(); // Close this activity
                    } else {
                        //if fails
                        Toast.makeText(LoginActivity.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
