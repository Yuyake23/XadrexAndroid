package com.example.xadrez;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class CadastroActivity extends AppCompatActivity {

    private FirebaseAuth auth;

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
        String usuario = this.etUsuario.getText().toString();
        String email = this.etEmail.getText().toString();
        String senha = this.etSenha.getText().toString();
        String senhaConfirma = this.etSenhaConfirma.getText().toString();
        if (usuario.isEmpty()) {
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

        this.auth.createUserWithEmailAndPassword(email, senha)
                .addOnSuccessListener(authResult -> {
                    Intent intent = new Intent(CadastroActivity.this, HomeActivity.class);
                    startActivity(intent);
                });
    }
}