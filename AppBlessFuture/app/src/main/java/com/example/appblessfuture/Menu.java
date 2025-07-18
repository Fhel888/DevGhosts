package com.example.appblessfuture;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class Menu extends AppCompatActivity implements View.OnClickListener {
    SwipeRefreshLayout swipeMenu;
    ImageButton btMenuVoltar, btMenuConfig, btMenuCriarProcesso, btMenuConsultaProcesso;
    TextView txtMenuUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.menu), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        swipeMenu = findViewById(R.id.swipeMenu);
        btMenuVoltar = findViewById(R.id.btMenuVoltar);
        btMenuConfig = findViewById(R.id.btMenuConfig);
        btMenuCriarProcesso = findViewById(R.id.btMenuCriarProcesso);
        btMenuConsultaProcesso = findViewById(R.id.btMenuConsultaProcesso);
        txtMenuUsuario = findViewById(R.id.txtMenuUsuario);

        btMenuVoltar.setOnClickListener(this);
        btMenuConfig.setOnClickListener(this);
        btMenuCriarProcesso.setOnClickListener(this);
        btMenuConsultaProcesso.setOnClickListener(this);

        swipeMenu.setOnRefreshListener(() -> {
            trazerUser();
            Toast.makeText(this, "Atualizando dados...", Toast.LENGTH_SHORT).show();
            swipeMenu.setRefreshing(false);
        });

        trazerUser();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btMenuVoltar){
            finish();
        }
        if (v.getId() == R.id.btMenuConfig){
            Intent tela = new Intent(this, Configuracao.class);
            startActivity(tela);
        }
        if (v.getId() == R.id.btMenuCriarProcesso){
            Intent tela = new Intent(this, CriarProcesso.class);
            startActivity(tela);
        }
        if (v.getId() == R.id.btMenuConsultaProcesso){
            Intent tela = new Intent(this, ConsultarProcesso.class);
            startActivity(tela);
        }
    }
    public void trazerUser() {
        // Recupera dados do usuário para preencher os campos
        SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);

        int codigo = prefs.getInt("codigo", -1);
        if (codigo != -1) {
            BancoControllerUsuarios bd = new BancoControllerUsuarios(this);
            Cursor dados = bd.trazerUsuario(codigo);

            if (dados != null && dados.moveToFirst()) {
                txtMenuUsuario.setText(dados.getString(2));
            }
        }
    }
}