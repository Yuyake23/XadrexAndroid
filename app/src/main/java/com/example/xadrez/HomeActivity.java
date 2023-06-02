package com.example.xadrez;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.firebase.auth.FirebaseAuth;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    private MeowBottomNavigation bottomNavigation;

    RelativeLayout usuario, jogar, creditos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();

        bottomNavigation = findViewById(R.id.bottomNavigation);
        usuario = findViewById(R.id.usuario);
        jogar = findViewById(R.id.jogar);
        creditos = findViewById(R.id.creditos);



        bottomNavigation.add(new MeowBottomNavigation.Model(1,R.drawable.email));
        bottomNavigation.add(new MeowBottomNavigation.Model(2,R.drawable.email));
        bottomNavigation.add(new MeowBottomNavigation.Model(3,R.drawable.email));

        bottomNavigation.show(2,true);
        bottomNavigation.setOnClickMenuListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                switch (model.getId()) {
                    case 1:
                        usuario.setVisibility(View.VISIBLE);
                        jogar.setVisibility(View.GONE);
                        creditos.setVisibility(View.GONE);
                        break;
                    case 2:
                        usuario.setVisibility(View.GONE);
                        jogar.setVisibility(View.VISIBLE);
                        creditos.setVisibility(View.GONE);
                        break;
                    case 3:
                        usuario.setVisibility(View.GONE);
                        jogar.setVisibility(View.GONE);
                        creditos.setVisibility(View.VISIBLE);
                        break;
                }
                return null;
            }
        });

        bottomNavigation.setOnShowListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                switch (model.getId()) {
                    case 1:
                        usuario.setVisibility(View.VISIBLE);
                        jogar.setVisibility(View.GONE);
                        creditos.setVisibility(View.GONE);
                        break;
                }
                return null;
            }
        });
        bottomNavigation.setOnShowListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                switch (model.getId()) {
                    case 2:
                        usuario.setVisibility(View.GONE);
                        jogar.setVisibility(View.VISIBLE);
                        creditos.setVisibility(View.GONE);
                        break;
                }
                return null;
            }
        });
        bottomNavigation.setOnShowListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                switch (model.getId()) {
                    case 3:
                        usuario.setVisibility(View.GONE);
                        jogar.setVisibility(View.GONE);
                        creditos.setVisibility(View.VISIBLE);
                        break;
                }
                return null;
            }
        });


        this.auth = FirebaseAuth.getInstance();

        Button btLogout = findViewById(R.id.btLogout);
        btLogout.setOnClickListener(v -> {
            this.auth.signOut();
            finish();
       });
    }
}
