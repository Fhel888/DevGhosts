package com.example.appblessfuture;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Login extends AppCompatActivity implements View.OnClickListener {

    Button btLoginEntrar, btLoginRecuperar, btLoginCad;
    EditText txtLoginEmail, txtLoginSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // VERIFICA SE JÁ ESTÁ LOGADO
        SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);
        int codigo = prefs.getInt("codigo", -1); // -1 se não houver código

        if (codigo != -1) {
            // Usuário já está logado, vai direto pra tela principal
            Intent telaPrincipal = new Intent(this, Menu.class);
            startActivity(telaPrincipal);
            finish(); // Finaliza tela de login
            return; // Evita continuar executando o onCreate
        }

        // Se não está logado, carrega a tela de login normalmente
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // INICIA COM ANIMAÇÃO (loginRoot começa invisível e pequeno)
        View loginRoot = findViewById(R.id.login);
        loginRoot.setScaleX(0f);
        loginRoot.setScaleY(0f);
        loginRoot.setAlpha(0f);

        ObjectAnimator scaleX = ObjectAnimator.ofFloat(loginRoot, "scaleX", 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(loginRoot, "scaleY", 1f);
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(loginRoot, "alpha", 1f);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(800);
        animatorSet.playTogether(scaleX, scaleY, fadeIn);
        animatorSet.start();

        btLoginEntrar = findViewById(R.id.btLoginEntrar);
        btLoginRecuperar = findViewById(R.id.btLoginRecuperar);
        btLoginCad = findViewById(R.id.btLoginCad);
        txtLoginEmail = findViewById(R.id.txtLoginNome);
        txtLoginSenha = findViewById(R.id.txtLoginSenha);

        btLoginEntrar.setOnClickListener(this);
        btLoginRecuperar.setOnClickListener(this);
        btLoginCad.setOnClickListener(this);

        txtLoginEmail.requestFocus();
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btLoginCad) {
            Intent telaCadastro = new Intent(this, Cadastro.class);
            startActivity(telaCadastro);
        }
        if (v.getId() == R.id.btLoginRecuperar) {
            Intent telaEsqueci = new Intent(this, RecuperarSenha.class);
            startActivity(telaEsqueci);
        }
        if (v.getId() == R.id.btLoginEntrar) {
            if (validarDados()) {
                Intent telaEntrar = new Intent(this, Menu.class);
                startActivity(telaEntrar);
                finish();
            }
        }
    }
    private boolean validarDados() {
        String msg = "";
        String email = txtLoginEmail.getText().toString();
        String senha = txtLoginSenha.getText().toString();
        boolean retorno = true;

        if (email.isEmpty()){
            msg = "Preencha o campo E-mail!";
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
            retorno = false;
        }else {
            if (senha.isEmpty()) {
                msg = "Preencha sua senha!";
                Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
                retorno = false;
            } else {
                BancoControllerUsuarios bd = new BancoControllerUsuarios(getBaseContext());
                Cursor dados = bd.consultarLogin(email, senha);
                if (!dados.moveToFirst()) {
                    msg = "E-mail ou Senha inválidos!";
                    Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
                    retorno = false;
                }else {
                    // Login válido! Salvando código do usuário no SharedPreferences
                    int codigoUsuario = dados.getInt(dados.getColumnIndexOrThrow("codigo"));
                    String User = dados.getString(dados.getColumnIndexOrThrow("user"));
                    String emailUsuario = dados.getString(dados.getColumnIndexOrThrow("email"));

                    //Salvando o codigo do usuario logado no sharedPreferences
                    SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt("codigo", codigoUsuario);
                    editor.putString("user", User);
                    editor.putString("email", emailUsuario);
                    editor.apply();
                }
            }
        }
        return retorno;
    }
}