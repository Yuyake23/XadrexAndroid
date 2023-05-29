package com.example.xadrez;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();

        this.auth = FirebaseAuth.getInstance();

        Button btLogout = findViewById(R.id.button_logout);
        btLogout.setOnClickListener(v -> {
            this.auth.signOut();
            finish();
        });
    }
}
