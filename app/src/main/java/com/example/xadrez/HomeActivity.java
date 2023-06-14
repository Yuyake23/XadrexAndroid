package com.example.xadrez;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.xadrez.chess.Move;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import kotlin.Unit;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    private RelativeLayout usuario, jogar, creditos;
    private Animation subir;
    private Button btSalvar;
    private Button btReiniciar;

    private BoardFragment board;
    private TextView samuelgit, yuyake, user, emailuser, partida;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Objects.requireNonNull(getSupportActionBar()).hide();

        this.db = FirebaseFirestore.getInstance();
        this.auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        this.subir = AnimationUtils.loadAnimation(this, R.anim.subir);
        this.usuario = findViewById(R.id.usuario);
        this.jogar = findViewById(R.id.jogar);
        this.creditos = findViewById(R.id.creditos);
        this.btSalvar = jogar.findViewById(R.id.salvar_jogo);
        this.btReiniciar = jogar.findViewById(R.id.reiniciar_jogo);
        this.samuelgit = findViewById(R.id.samuelpdr);
        this.yuyake = findViewById(R.id.yuyake);
        this.user = findViewById(R.id.usuario_atual);
        this.emailuser = findViewById(R.id.emailuser);
        this.partida = findViewById(R.id.partida);

        db.collection("users").whereEqualTo("id", auth.getCurrentUser().getUid())
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            user.setText((CharSequence) document.getData().get("username"));
                        }
                    } else {
                        Log.d("database", "Error getting documents: ", task.getException());
                    }
                });

        emailuser.setText(auth.getCurrentUser().getEmail());
        samuelgit.setOnClickListener(v -> {
            Uri uri = Uri.parse("https://github.com/SamuelSilvaPDR");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });


        yuyake.setOnClickListener(v -> {
            Uri uri = Uri.parse("https://github.com/Yuyake23");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });

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

        btSalvar.setOnClickListener(v -> saveMatch());
        btReiniciar.setOnClickListener(v -> configureFragment());
    }

    private void saveMatch() {
        List<Move> moves = board.getMoves();
        List<Map<String, Object>> moveList = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();

        for (int i = 0; i < moves.size(); i++) {
            moveList.add(moveToMap(moves.get(i)));
        }

        map.put("playerId", auth.getCurrentUser().getUid());
        map.put("moveList", moveList);
        map.put("timestamp", new Timestamp(new Date()));
        map.put("winner", board.getChessMatch().matchIsOver() ? board.getChessMatch().getWinner() : null);

        CollectionReference matches = db.collection("matches");
        matches.add(map).addOnCompleteListener(task -> {
            Toast.makeText(this, task.isSuccessful() ? "Partida salva" :
                    "Não foi possível salvar a partida", Toast.LENGTH_SHORT).show();
        });
        teste();
    }


    private Map<String, Object> moveToMap(Move move) {
        Map<String, Object> moveMap = new HashMap<>();
        moveMap.put("sourceRowPosition", move.sourcePosition().toPosition().getRow());
        moveMap.put("sourceColumnPosition", move.sourcePosition().toPosition().getColumn());
        moveMap.put("targetRowPosition", move.targetPosition().toPosition().getRow());
        moveMap.put("targetColumnPosition", move.targetPosition().toPosition().getColumn());
        moveMap.put("promotedPiece", move.promotedPiece());
        return moveMap;
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
        this.board = new BoardFragment(this::saveMatch);
        fragmentTransaction.add(R.id.boardFragment, this.board);
        fragmentTransaction.commit();
    }

    public void teste() {
        db.collection("matches").whereEqualTo("playerId", auth.getUid())
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        StringBuilder stringBuilder = new StringBuilder();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Map<String,Object> map = document.getData();
                            stringBuilder.append(map.get("winner")).append("\n");
                            stringBuilder.append(map.get("timestamp")).append("\n");
                            stringBuilder.append(map.get("playerId")).append("\n");
                            stringBuilder.append(map.get("moveList")).append("\n");
                        }
                        String result = stringBuilder.toString();
                        partida.setText(result);
                        Log.d("Teste", result);
                    } else {
                        Log.d("database", "Error getting documents: ", task.getException());
                    }
                });
    }
}
