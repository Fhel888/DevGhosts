package com.example.blessfuture;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btEntrar;
    TextView tvSaudacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);  // Referência ao layout XML

        // Inicializando os elementos
        btEntrar = findViewById(R.id.btEntrar);
        tvSaudacao = findViewById(R.id.tvSaudacao);

        // Configuração do evento de clique
        btEntrar.setOnClickListener(this);

        // Pegando o apelido do usuário salvo nas preferências
        SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);
        String apelido = prefs.getString("apelido", "Visitante");

        // Modificando a saudação do usuário
        tvSaudacao.setText("Olá, " + apelido + "!");
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btEntrar) {
            // Intent para a próxima tela
            Intent tela = new Intent(this, Configuracao.class);
            startActivity(tela);
        }
    }
}
