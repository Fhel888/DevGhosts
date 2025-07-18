package com.example.appblessfuture;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class Configuracao extends AppCompatActivity implements View.OnClickListener {
    SwipeRefreshLayout swipeConfiguracao;
    ImageButton btConfVoltar;
    Button btConfSair, btConfAlterar, btConfExcluir;
    EditText txtConfNome, txtConfUser, txtConfCel, txtConfEmail, txtConfSenha, txtConfConfSenha;
    Spinner spConfSetor;
    TextView helpConfNome, helpConfUser, helpConfCel, helpConfEmail, helpConfigSenha, helpConfConfSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_configuracao);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.configuracao), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        swipeConfiguracao = findViewById(R.id.swipeConfiguracao);
        btConfVoltar = findViewById(R.id.btConfVoltar);
        btConfSair = findViewById(R.id.btConfSair);
        btConfAlterar = findViewById(R.id.btConfAlterar);
        btConfExcluir = findViewById(R.id.btConfExcluir);
        txtConfNome = findViewById(R.id.txtConfNome);
        txtConfUser = findViewById(R.id.txtConfUser);
        spConfSetor = findViewById(R.id.spConfSetor);
        txtConfCel = findViewById(R.id.txtConfCel);
        txtConfEmail = findViewById(R.id.txtConfEmail);
        txtConfSenha = findViewById(R.id.txtConfSenha);
        txtConfConfSenha = findViewById(R.id.txtConfConfSenha);
        helpConfNome = findViewById(R.id.helpConfNome);
        helpConfUser = findViewById(R.id.helpConfUser);
        helpConfCel = findViewById(R.id.helpConfCel);
        helpConfEmail = findViewById(R.id.helpConfEmail);
        helpConfigSenha = findViewById(R.id.helpConfSenha);
        helpConfConfSenha = findViewById(R.id.helpConfConfSenha);

        btConfVoltar.setOnClickListener(this);
        btConfSair.setOnClickListener(this);
        btConfAlterar.setOnClickListener(this);
        btConfExcluir.setOnClickListener(this);

        swipeConfiguracao.setOnRefreshListener(() -> {
            trazerDados();
            Toast.makeText(this, "Atualizando dados...", Toast.LENGTH_SHORT).show();
            swipeConfiguracao.setRefreshing(false);
        });

        //Funções de Ajuda com mensagem flutuante (xml + java)
        txtConfNome.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                helpConfNome.setVisibility(View.VISIBLE);
            } else {
                helpConfNome.setVisibility(View.GONE);
            }
        });
        txtConfUser.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                helpConfUser.setVisibility(View.VISIBLE);
            } else {
                helpConfUser.setVisibility(View.GONE);
            }
        });
        txtConfCel.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                helpConfCel.setVisibility(View.VISIBLE);
            } else {
                helpConfCel.setVisibility(View.GONE);
            }
        });
        txtConfEmail.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                helpConfEmail.setVisibility(View.VISIBLE);
            } else {
                helpConfEmail.setVisibility(View.GONE);
            }
        });
        txtConfSenha.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                helpConfigSenha.setVisibility(View.VISIBLE);
            } else {
                helpConfigSenha.setVisibility(View.GONE);
            }
        });
        txtConfConfSenha.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                helpConfConfSenha.setVisibility(View.VISIBLE);
            } else {
                helpConfConfSenha.setVisibility(View.GONE);
            }
        });

        //Função para mascara de número de celular
        txtConfCel.addTextChangedListener(new TextWatcher() {
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
                            if (length > 11) length = 11; // Trava total em 11 dígitos
                            formatted.append(str.substring(7, length));
                        } else if (length > 2) {
                            formatted.append(str.substring(2));
                        }
                    } else {
                        formatted.append(str);
                    }
                }
                isUpdating = true;
                txtConfCel.setText(formatted.toString());
                txtConfCel.setSelection(txtConfCel.getText().length());
            }
            @Override
            public void afterTextChanged(Editable s) {}

        });
        //Adapter pro Spinner
        ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(
                this,
                R.array.setores_array, // já criado no strings.xml
                R.layout.spinner_item_selected // layout do item selecionado
        );
        adapterSpinner.setDropDownViewResource(R.layout.spinner_item_dropdown); // layout do dropdown
        spConfSetor.setAdapter(adapterSpinner);

        trazerDados();
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btConfVoltar) {
            finish();
        }
        if (v.getId() == R.id.btConfSair) {
            logout();
        }
        if (v.getId() == R.id.btConfAlterar) {
            alterar();
        }
        if (v.getId() == R.id.btConfExcluir) {
            excluirConta();
        }
    }
    //metodo universal para remover o foco de qualquer EditText e esconder o teclado
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (view instanceof EditText) {
                Rect outRect = new Rect();
                view.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) ev.getRawX(), (int) ev.getRawY())) {
                    view.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }
    public void trazerDados() {
        // Recupera dados do usuário para preencher os campos
        SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);
        int codigo = prefs.getInt("codigo", -1);
        if (codigo != -1) {
            BancoControllerUsuarios bd = new BancoControllerUsuarios(this);
            Cursor dados = bd.trazerUsuario(codigo); // não esquecer de criar isso no BancoController

            if (dados != null && dados.moveToFirst()) {

                txtConfNome.setText(dados.getString(1));
                txtConfUser.setText(dados.getString(2));
                String setorSalvo = dados.getString(3);
                ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) spConfSetor.getAdapter();
                int posicao = adapter.getPosition(setorSalvo);
                if (posicao >= 0) {
                    spConfSetor.setSelection(posicao);
                }
                txtConfCel.setText(dados.getString(4));
                txtConfEmail.setText(dados.getString(5));
                txtConfSenha.setText(dados.getString(6));
            }
        }
    }
    public void alterar() {
        String msg = "";
        String nome = txtConfNome.getText().toString();
        String user = txtConfUser.getText().toString();
        String setor = spConfSetor.getSelectedItem().toString();
        String cel = txtConfCel.getText().toString();
        String email = txtConfEmail.getText().toString();
        String senha = txtConfSenha.getText().toString();
        String confSenha = txtConfConfSenha.getText().toString();

        if (nome.isEmpty() || nome.length() < 8) {
            msg = "O campo NOME deve ser preenchido!";
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        } else {
            if (user.isEmpty() || user.length() < 3) {
                msg = "O campo APELIDO deve ser preenchido!";
                Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
            } else {
                if (setor.equals("Selecione um Setor")) {
                    msg = "Por favor, selecione um SETOR válido!";
                    Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
                } else {
                    if (cel.isEmpty() || cel.length() < 15) {
                        msg = "Digite um número de celular válido com DDD!";
                        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
                    } else {
                        if (email.isEmpty() || email.length() < 10 || !email.contains("@") || !email.contains(".")) {
                            msg = "Digite um e-mail válido!";
                            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
                        } else {
                            if (senha.isEmpty() || senha.length() < 6) {
                                msg = "Digite uma senha válida com mínimo de 6 dígitos!";
                                Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
                            } else {
                                if (confSenha.isEmpty()) {
                                    msg = "O campo CONFIRMAR SENHA deve ser preenchido!";
                                    Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
                                } else {
                                    if (!senha.equals(confSenha)) {
                                        msg = "Os campos SENHA e CONFIRMAR SENHA devem ser iguais!";
                                        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
                                    } else {
                                        SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);
                                        int codigo = prefs.getInt("codigo", -1);

                                        if (codigo != -1) {
                                            BancoControllerUsuarios bd = new BancoControllerUsuarios(this);
                                            String resultado = bd.alterarDados(codigo, nome, user, setor, cel, email, senha);
                                            Toast.makeText(this, resultado, Toast.LENGTH_LONG).show();
                                            // Esconde o teclado e tira o foco do campo atual
                                            View view = this.getCurrentFocus();
                                            if (view != null) {
                                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                                                view.clearFocus();
                                            }
                                            txtConfConfSenha.setText("");
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    public void limpar(){
        txtConfNome.setText("");
        txtConfUser.setText("");
        spConfSetor.setSelection(0);
        txtConfCel.setText("");
        txtConfEmail.setText("");
        txtConfSenha.setText("");
        txtConfConfSenha.setText("");
    }
    public void logout(){
        // Limpa o SharedPreferences
        SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear(); // Remove todos os dados
        editor.apply();

        // Volta pra tela de login, limpando a pilha de atividades
        Intent tela = new Intent(getBaseContext(), Login.class);
        tela.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(tela);
        finish(); // Finaliza a tela atual
    }
    public void excluirConta(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View telaexcluir = getLayoutInflater().inflate(R.layout.excluir_conta, null);
        builder.setView(telaexcluir);

        AlertDialog mostrarTela = builder.create();
        mostrarTela.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        mostrarTela.show();

        EditText senha = telaexcluir.findViewById(R.id.txtSenhaExcluir);
        Button botaoExcluir = telaexcluir.findViewById(R.id.btExcluirConta);

        botaoExcluir.setOnClickListener(v -> {
            String senhaDigitada = senha.getText().toString();
            String senhaAtual = txtConfSenha.getText().toString();

            if (senhaDigitada.equals(senhaAtual)){
                SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);
                int codigo = prefs.getInt("codigo", -1);

                if (codigo != -1) {
                    BancoControllerUsuarios bd = new BancoControllerUsuarios(this);
                    String resultado = bd.excluirDados(codigo);
                    Toast.makeText(this, resultado, Toast.LENGTH_LONG).show();

                    logout();
                }
                mostrarTela.dismiss();
            } else {
                Toast.makeText(this, "Senha incorreta!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}