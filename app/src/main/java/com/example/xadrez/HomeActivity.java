package com.example.xadrez;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
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

    private Animation subir;

     private Button iniciar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        subir = AnimationUtils.loadAnimation(this, R.anim.subir);

        getSupportActionBar().hide();

        bottomNavigation = findViewById(R.id.bottomNavigation);
        usuario = findViewById(R.id.usuario);
        jogar = findViewById(R.id.jogar);
        creditos = findViewById(R.id.creditos);
        iniciar = findViewById(R.id.iniciar_jogo);

        iniciar.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, GameActivity.class);
            startActivity(intent);
        });

        bottomNavigation.add(new MeowBottomNavigation.Model(1,R.drawable.custom_usuario_ic));
        bottomNavigation.add(new MeowBottomNavigation.Model(2,R.drawable.play));
        bottomNavigation.add(new MeowBottomNavigation.Model(3,R.drawable.equipe));

        bottomNavigation.show(2,true);
        bottomNavigation.setOnClickMenuListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                switch (model.getId()) {
                    case 1:
                        usuario.setVisibility(View.VISIBLE);
                        jogar.setVisibility(View.GONE);
                        creditos.setVisibility(View.GONE);
                        usuario.startAnimation(subir);

                        break;
                    case 2:
                        usuario.setVisibility(View.GONE);
                        jogar.setVisibility(View.VISIBLE);
                        creditos.setVisibility(View.GONE);
                        jogar.startAnimation(subir);
                        break;
                    case 3:
                        usuario.setVisibility(View.GONE);
                        jogar.setVisibility(View.GONE);
                        creditos.setVisibility(View.VISIBLE);
                        creditos.startAnimation(subir);
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

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ImageView logout = findViewById(R.id.logout);
        logout.setOnClickListener(v -> {
            this.auth.signOut();
            finish();
       });

    }
}
