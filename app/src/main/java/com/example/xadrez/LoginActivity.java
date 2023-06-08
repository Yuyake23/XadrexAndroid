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
    private Button btEntrar;
    private ProgressBar espere;

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
        this.espere = findViewById(R.id.progress);

        Button btCadastro = findViewById(R.id.bt_cadastrar);
        ImageView voltar = findViewById(R.id.voltar);


        this.btEntrar.setOnClickListener(this::onClickEntrar);

        btCadastro.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, CadastroActivity.class);
            startActivity(intent);
        });

        voltar.setOnClickListener(v -> onBackPressed());
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.btEntrar.setClickable(true);
    }

    private void onClickEntrar(View v) {
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
                    espere.setVisibility(View.GONE);
                    tvAvisos.setText("Falha ao logar: " + e.getMessage());
                    v.setClickable(true);
                });
        espere.setVisibility(View.VISIBLE);
        v.setClickable(false);
    }


    private void abrirJogo(AuthResult authResult) {
        Intent intent = new Intent(this, HomeActivity.class);
        espere.setVisibility(View.GONE);
        startActivity(intent);
    }

}