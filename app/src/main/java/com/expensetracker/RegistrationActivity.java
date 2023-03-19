package com.expensetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private EditText confirmPassword;
    private Button btnReg;
    private TextView signInFromReg;
    private ProgressDialog dialog;


    // Firebase
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        auth = FirebaseAuth.getInstance();

        dialog = new ProgressDialog(this);

        registrationInfo();
    }

    private void registrationInfo() {
        email = findViewById(R.id.email_reg);
        password = findViewById(R.id.password_reg);
        confirmPassword = findViewById(R.id.repeat_password_reg);
        btnReg = findViewById(R.id.sign_up_reg_button);
        signInFromReg = findViewById(R.id.sign_in_reg);

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailVal = email.getText().toString().trim().toLowerCase();
                String passwordVal = password.getText().toString().trim();
                String confirmPasswordVal = confirmPassword.getText().toString();

                if (TextUtils.isEmpty(emailVal)) {
                    email.setError("Email ID required!");
                    return;
                }

                if (!validateEmail(emailVal)) {
                    email.setError("Email ID is invalid!");
                }

                if (TextUtils.isEmpty(passwordVal)) {
                    password.setError("Password required!");
                    return;
                }

                if (passwordVal.length() < 6) {
                    email.setError("Password length should be atleast 6 characters!");
                    return;
                }

                if (TextUtils.isEmpty(confirmPasswordVal)) {
                    confirmPassword.setError("Please repeat the password once again!");
                    return;
                }

                if (!passwordVal.equals(confirmPasswordVal)) {
                    password.setError("Password and confirm password should match!");
                    confirmPassword.setError("Password and confirm password should match!");
                    return;
                }

                dialog.setMessage("Processing...");


                auth.createUserWithEmailAndPassword(emailVal, passwordVal).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            dialog.dismiss();
                            Toast.makeText(getApplicationContext(), "User successfully registered", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } else {
                            dialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Registration failed. Please try again later!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        signInFromReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }

    private boolean validateEmail(String emailVal) {
        String regexPattern = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        return Pattern.compile(regexPattern)
                .matcher(emailVal)
                .matches();
    }
}