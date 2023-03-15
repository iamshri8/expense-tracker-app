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
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText emailResetPassword;
    private Button resetPasswordBtn;
    private ProgressDialog dialog;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        auth = FirebaseAuth.getInstance();

        dialog = new ProgressDialog(this);

        sendResetPasswordToEmail();
    }

    private void sendResetPasswordToEmail() {
        emailResetPassword = findViewById(R.id.email_reset_password);
        resetPasswordBtn = findViewById(R.id.reset_password_btn);

        resetPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailResetPasswordVal = emailResetPassword.getText().toString().trim();

                if (TextUtils.isEmpty(emailResetPasswordVal)) {
                    emailResetPassword.setError("Email ID required!");
                    return;
                }

                dialog.setMessage("Processing...");

                auth.sendPasswordResetEmail(emailResetPasswordVal).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            dialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Reset password instruction sent to your email id.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }

                        else {
                            System.out.println("Failed");
                            System.out.println(task.getException());
                            dialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Reset password failed. Please try again later!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });


    }
}