package com.example.blessfuture;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnEmpresa, btnCliente;
    TextView tvTitulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);  // Referência ao layout XML

        // Inicializando os elementos
        btnEmpresa = findViewById(R.id.btnEmpresa);
        btnCliente = findViewById(R.id.btnCliente);
        tvTitulo = findViewById(R.id.tvTitulo);

        // Configuração dos eventos de clique
        btnEmpresa.setOnClickListener(this);
        btnCliente.setOnClickListener(this);

        // Personalizando o título
        tvTitulo.setText("Bem-vindo ao\nBless Future");
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnEmpresa) {
            // Intent para a tela de Empresa
            Intent tela = new Intent(this, EmpresaActivity.class);
            startActivity(tela);
        }

        if (v.getId() == R.id.btnCliente) {
            // Intent para a tela de Cliente
            Intent tela = new Intent(this, ClienteActivity.class);
            startActivity(tela);
        }
    }
}
