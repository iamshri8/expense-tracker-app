package com.expensetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {

    private TextView welcomeMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        extractWelcomeMessage();

    }

    private void extractWelcomeMessage() {
        welcomeMessage = findViewById(R.id.welcome_message);

        Intent intent = getIntent();
        welcomeMessage.setText(intent.getStringExtra("welcomeMessage"));

    }
}