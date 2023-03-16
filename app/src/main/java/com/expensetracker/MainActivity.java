package com.expensetracker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button btnLogin;
    private TextView forgetPassword;
    private TextView signUpFromLogin;
    private ProgressDialog dialog;
    // Firebase
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();

        dialog = new ProgressDialog(this);

        loginInfo();
    }

    private void loginInfo() {
        email = findViewById(R.id.email_login);
        password = findViewById(R.id.password_login);
        btnLogin = findViewById(R.id.login_button);
        forgetPassword = findViewById(R.id.forget_password);
        signUpFromLogin = findViewById(R.id.sign_up_login);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailVal = email.getText().toString().trim().toLowerCase();
                String passwordVal = password.getText().toString().trim();

                if (TextUtils.isEmpty(emailVal)) {
                    email.setError("Email ID required!");
                    return;
                }

                if (TextUtils.isEmpty(passwordVal)) {
                    password.setError("Password required!");
                    return;
                }

                dialog.setMessage("Processing...");

                auth.signInWithEmailAndPassword(emailVal, passwordVal).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            dialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Login successful.", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                            intent.putExtra("welcomeMessage", "Welcome ".concat(emailVal));
                            startActivity(intent);
                        } else {
                            dialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Login failed. Please check the username or the password.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        signUpFromLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
            }
        });

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ResetPasswordActivity.class));
            }
        });


    }
}