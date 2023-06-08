package com.example.xadrez;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.xadrez.util.Utils;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import kotlin.Unit;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    private RelativeLayout usuario, jogar, creditos;

    private Animation subir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Objects.requireNonNull(getSupportActionBar()).hide();

        this.auth = Utils.confirmAuth(this);
        this.subir = AnimationUtils.loadAnimation(this, R.anim.subir);

        this.usuario = findViewById(R.id.usuario);
        this.jogar = findViewById(R.id.jogar);
        this.creditos = findViewById(R.id.creditos);

        this.configureFragment();

        ImageView logout = findViewById(R.id.logout);
        MeowBottomNavigation bottomNavigation = findViewById(R.id.bottomNavigation);

        logout.setOnClickListener(v -> {
            this.auth.signOut();
            finish();
        });

        bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.custom_usuario_ic));
        bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.play));
        bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.equipe));
        bottomNavigation.show(2, true);
        bottomNavigation.setOnClickMenuListener(this::mudarConteudo);

    }

    @Nullable
    private Unit mudarConteudo(@NonNull MeowBottomNavigation.Model model) {
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

    private void configureFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment board = new BoardFragment();
        fragmentTransaction.add(R.id.boardFragment, board);
        fragmentTransaction.commit();
    }
}
