package com.example.xadrez;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xadrez.dto.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CadastroActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    private EditText etUsuario;
    private EditText etEmail;
    private EditText etSenha;
    private EditText etSenhaConfirma;
    private TextView tvAvisos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        Objects.requireNonNull(getSupportActionBar()).hide();

        this.auth = FirebaseAuth.getInstance();
        this.db = FirebaseFirestore.getInstance();

        this.etUsuario = findViewById(R.id.input_usuario);
        this.etEmail = findViewById(R.id.input_email);
        this.etSenha = findViewById(R.id.input_senha);
        this.etSenhaConfirma = findViewById(R.id.input_rep_senha);
        this.tvAvisos = findViewById(R.id.text_avisos);

        ImageView voltar = findViewById(R.id.voltar);
        Button btConfirma = findViewById(R.id.btn_cadastrar);

        btConfirma.setOnClickListener(this::onClickCadastrar);

        voltar.setOnClickListener(v -> onBackPressed());
    }

    private void onClickCadastrar(View v) {
        String username = this.etUsuario.getText().toString();
        String email = this.etEmail.getText().toString();
        String senha = this.etSenha.getText().toString();
        String senhaConfirma = this.etSenhaConfirma.getText().toString();
        if (username.isEmpty()) {
            this.tvAvisos.setText("Digite o nome de usuário");
            return;
        } else if (email.isEmpty()) {
            this.tvAvisos.setText("Digite o email");
            return;
        } else if (senha.isEmpty()) {
            this.tvAvisos.setText("Digite a senha");
            return;
        } else if (senhaConfirma.isEmpty()) {
            this.tvAvisos.setText("Confirme a senha");
            return;
        } else if (!senha.equals(senhaConfirma)) {
            this.tvAvisos.setText("As senhas não conferem");
            return;
        }

        this.tvAvisos.setText("");

        this.auth.createUserWithEmailAndPassword(email, senha)
                .addOnSuccessListener(authResult -> {
                    Log.d("fireauth", "Usuario cadastrado");

                    CollectionReference userCollection = db.collection("users");

                    Map<String, Object> user = new HashMap<>();
                    user.put("id", authResult.getUser().getUid());
                    user.put("email", email);
                    user.put("username", username);

                    userCollection.add(user);

                    Intent intent = new Intent(CadastroActivity.this, HomeActivity.class);
                    startActivity(intent);
                })
                .addOnFailureListener(e -> {
                    Log.e("fireauth", "Falha ao cadastra usuario", e);
                });


    }
}