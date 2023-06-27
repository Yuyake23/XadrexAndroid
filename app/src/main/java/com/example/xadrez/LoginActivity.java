package com.example.xadrez;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    private EditText etEmail;
    private EditText etSenha;
    private TextView tvAvisos;
    private Button btEntrar, btCadastro;
    private ProgressBar progressBar;
    private ImageView btVoltar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Objects.requireNonNull(getSupportActionBar()).hide();

        this.auth = FirebaseAuth.getInstance();

        this.etEmail = findViewById(R.id.input_email);
        this.etSenha = findViewById(R.id.input_senha);
        this.tvAvisos = findViewById(R.id.text_avisos);
        this.btEntrar = findViewById(R.id.bt_entrar);
        this.progressBar = findViewById(R.id.progress);

        this.btCadastro = findViewById(R.id.bt_cadastrar);
        this.btVoltar = findViewById(R.id.voltar);
    }

    @Override
    protected void onStart() {
        super.onStart();

        this.btVoltar.setOnClickListener(v -> onBackPressed());
        this.btEntrar.setOnClickListener(this::onClickEntrar);

        this.btCadastro.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, CadastroActivity.class);
            startActivity(intent);
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        this.btEntrar.setClickable(true);
    }

    private void onClickEntrar(View btEntrar) {
        String email = this.etEmail.getText().toString();
        String senha = this.etSenha.getText().toString();
        if (email.isEmpty()) {
            this.tvAvisos.setText("Digite o e-mail!");
            this.tvAvisos.setVisibility(View.VISIBLE);
            return;
        } else if (senha.isEmpty()) {
            this.tvAvisos.setText("Digite a senha!");
            this.tvAvisos.setVisibility(View.VISIBLE);
            return;
        }

        this.tvAvisos.setText("");

        this.auth.signInWithEmailAndPassword(email, senha)
                .addOnSuccessListener(this::abrirJogo)
                .addOnFailureListener(e -> {
                    this.progressBar.setVisibility(View.GONE);
                    this.tvAvisos.setText("Falha ao logar: " + e.getMessage());
                    btEntrar.setClickable(true);
                });
        this.progressBar.setVisibility(View.VISIBLE);
        btEntrar.setClickable(false);
    }


    private void abrirJogo(AuthResult authResult) {
        Intent intent = new Intent(this, HomeActivity.class);
        progressBar.setVisibility(View.GONE);
        startActivity(intent);
    }

}