package com.example.xadrez;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Objects.requireNonNull(getSupportActionBar()).hide();

        this.auth = FirebaseAuth.getInstance();
//        Utils.setSize(getWindowManager());

        new Handler().postDelayed(() -> {
            Class<?> otherClass = auth.getCurrentUser() == null ? LoginActivity.class : HomeActivity.class;

            Intent intent = new Intent(MainActivity.this, otherClass);
            startActivity(intent);
            finish();
        }, 2000);
    }
}