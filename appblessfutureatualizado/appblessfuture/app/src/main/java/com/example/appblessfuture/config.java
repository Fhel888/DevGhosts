package com.example.appblessfuture;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class Config extends AppCompatActivity implements View.OnClickListener {

    // Declaração dos componentes
    ImageButton btnVoltarConfig;
    Button btnSair, btnEditarDados, btnExcluirConta;
    EditText editNomeConfig, editEmailConfig, editTelefoneConfig;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracao);

        // Inicialização dos componentes com os nomes dos IDs
        btnVoltarConfig = findViewById(R.id.btnVoltarConfig);
        btnSair = findViewById(R.id.btnSair);
        btnEditarDados = findViewById(R.id.btnEditarDados);
        btnExcluirConta = findViewById(R.id.btnExcluirConta);
        editNomeConfig = findViewById(R.id.editNomeConfig);
        editEmailConfig = findViewById(R.id.editEmailConfig);
        editTelefoneConfig = findViewById(R.id.editTelefoneConfig);

        // Listeners para os botões
        btnVoltarConfig.setOnClickListener(this);
        btnSair.setOnClickListener(this);
        btnEditarDados.setOnClickListener(this);
        btnExcluirConta.setOnClickListener(this);

        trazerDados();

        // Função para máscara de celular
        editTelefoneConfig.addTextChangedListener(new TextWatcher() {
            boolean isUpdating;
            String oldText = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = s.toString().replaceAll("[^\\d]", ""); // remove tudo que não for número

                if (isUpdating) {
                    oldText = str;
                    isUpdating = false;
                    return;
                }

                StringBuilder formatted = new StringBuilder();
                int length = str.length();

                if (length > 0) {
                    formatted.append("(");
                    if (length >= 2) {
                        formatted.append(str.substring(0, 2)).append(") ");
                        if (length >= 7) {
                            formatted.append(str.substring(2, 7)).append("-");
                            if (length > 11) length = 11; // Limita em 11 dígitos
                            formatted.append(str.substring(7, length));
                        } else if (length > 2) {
                            formatted.append(str.substring(2));
                        }
                    } else {
                        formatted.append(str);
                    }
                }
                isUpdating = true;
                editTelefoneConfig.setText(formatted.toString());
                editTelefoneConfig.setSelection(editTelefoneConfig.getText().length());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnVoltarConfig) {
            finish();
        }
        if (v.getId() == R.id.btnSair) {
            logout();
        }
        if (v.getId() == R.id.btnEditarDados) {
            alterar();
        }
        if (v.getId() == R.id.btnExcluirConta) {
            excluirConta();
        }
    }

    // Função para trazer dados do usuário
    public void trazerDados() {
        SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);
        int codigo = prefs.getInt("codigo", -1);
        if (codigo != -1) {
            BancoControllerUsuarios bd = new BancoControllerUsuarios(this);
            Cursor dados = bd.trazerUsuario(codigo);

            if (dados != null && dados.moveToFirst()) {
                editNomeConfig.setText(dados.getString(1)); // Nome
                editEmailConfig.setText(dados.getString(4)); // E-mail
                editTelefoneConfig.setText(dados.getString(3)); // Telefone
            }
        }
    }

    // Função para alterar dados do usuário
    public void alterar() {
        String msg = "";
        String nome = editNomeConfig.getText().toString();
        String email = editEmailConfig.getText().toString();
        String cel = editTelefoneConfig.getText().toString();

        if (nome.isEmpty() || nome.length() < 8) {
            msg = "O campo NOME deve ser preenchido!";
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        } else if (cel.isEmpty() || cel.length() < 15) {
            msg = "Digite um número de celular válido com DDD!";
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        } else if (email.isEmpty() || !email.contains("@") || !email.contains(".")) {
            msg = "Digite um e-mail válido!";
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        } else {
            SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);
            int codigo = prefs.getInt("codigo", -1);

            if (codigo != -1) {
                BancoControllerUsuarios bd = new BancoControllerUsuarios(this);
                String resultado = bd.alterarDados(codigo, nome, cel, email);
                Toast.makeText(this, resultado, Toast.LENGTH_LONG).show();
            }
        }
    }

    // Função para fazer logout
    public void logout() {
        SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();

        Intent tela = new Intent(getBaseContext(), Login.class);
        tela.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(tela);
        finish();
    }

    // Função para excluir conta
    public void excluirConta() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View telaexcluir = getLayoutInflater().inflate(R.layout.excluir_conta, null);
        builder.setView(telaexcluir);

        AlertDialog mostrarTela = builder.create();
        mostrarTela.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        mostrarTela.show();

        Button botaoExcluir = telaexcluir.findViewById(R.id.btExcluirConta);
        botaoExcluir.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);
            int codigo = prefs.getInt("codigo", -1);

            if (codigo != -1) {
                BancoControllerUsuarios bd = new BancoControllerUsuarios(this);
                String resultado = bd.excluirDados(codigo);
                Toast.makeText(this, resultado, Toast.LENGTH_LONG).show();

                logout();
            }
            mostrarTela.dismiss();
        });
    }
}
