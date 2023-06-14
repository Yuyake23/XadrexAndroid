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

    private ImageView logout;
    private MeowBottomNavigation bottomNavigation;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Objects.requireNonNull(getSupportActionBar()).hide();

        this.auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        this.db = FirebaseFirestore.getInstance();

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
        this.logout = findViewById(R.id.logout);
        this.bottomNavigation = findViewById(R.id.bottomNavigation);

        this.bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.custom_usuario_ic));
        this.bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.play));
        this.bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.equipe));


        this.configureFragment();
    }

    @Override
    protected void onStart() {
        super.onStart();

        this.samuelgit.setOnClickListener(v -> {
            Uri uri = Uri.parse("https://github.com/SamuelSilvaPDR");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });

        this.yuyake.setOnClickListener(v -> {
            Uri uri = Uri.parse("https://github.com/Yuyake23");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });

        this.logout.setOnClickListener(v -> {
            this.auth.signOut();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        this.bottomNavigation.show(2, true);
        this.bottomNavigation.setOnClickMenuListener(this::mudarConteudo);

        this.btSalvar.setOnClickListener(v -> saveMatch());
        this.btReiniciar.setOnClickListener(v -> configureFragment());
    }

    @Override
    public void onResume() {
        super.onResume();

        this.emailuser.setText(Objects.requireNonNull(this.auth.getCurrentUser()).getEmail());

        this.db.collection("users").whereEqualTo("id", auth.getCurrentUser().getUid())
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            user.setText((CharSequence) document.getData().get("username"));
                        }
                    } else {
                        Log.d("database", "Error getting documents: ", task.getException());
                    }
                });

        this.getLog();
    }

    private void saveMatch() {
        List<Move> moves = board.getMoves();
        List<Map<String, Object>> moveList = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();

        for (int i = 0; i < moves.size(); i++) {
            moveList.add(moveToMap(moves.get(i)));
        }

        map.put("playerId", auth.getUid());
        map.put("moveList", moveList);
        map.put("timestamp", new Timestamp(new Date()));
        map.put("winner", board.getChessMatch().matchIsOver() ? board.getChessMatch().getWinner() : null);

        CollectionReference matches = db.collection("matches");
        matches.add(map).addOnCompleteListener(task -> Toast.makeText(this, task.isSuccessful() ? "Partida salva" :
                "Não foi possível salvar a partida", Toast.LENGTH_SHORT).show());
        this.getLog();
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
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        this.board = new LocalBoard(this::saveMatch);
        fragmentTransaction.add(R.id.boardFragment, this.board);
        fragmentTransaction.commit();
    }

    public void getLog() {
        this.db.collection("matches").whereEqualTo("playerId", auth.getUid())
                .get().addOnSuccessListener(documentSnapshots -> {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (QueryDocumentSnapshot document : documentSnapshots) {
                        Map<String, Object> map = document.getData();
                        stringBuilder.append(map.get("winner")).append("\n");
                        stringBuilder.append(map.get("timestamp")).append("\n");
                        stringBuilder.append(map.get("playerId")).append("\n");
                        stringBuilder.append(map.get("moveList")).append("\n");
                    }
                    String result = stringBuilder.toString();
                    partida.setText(result);
                });
    }
}
