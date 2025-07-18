package com.example.appblessfuture;

import android.content.SharedPreferences;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Cadastro extends AppCompatActivity implements View.OnClickListener {
    ImageButton btCadVoltar;
    Button btCadCadastrar;
    EditText txtCadNome, txtCadUser, txtCadCel, txtCadEmail, txtCadSenha, txtCadConfSenha;
    Spinner spCadSetor;
    TextView helpCadastroNome, helpCadastroUser, helpCadastroCel, helpCadastroEmail, helpCadastroSenha,
            helpCadastroConfSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cadastro);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cadastro), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btCadVoltar = findViewById(R.id.btCadVoltar);
        btCadCadastrar = findViewById(R.id.btCadCadastrar);
        txtCadNome = findViewById(R.id.txtCadNome);
        txtCadUser = findViewById(R.id.txtCadUser);
        spCadSetor = findViewById(R.id.spCadSetor);
        txtCadCel = findViewById(R.id.txtCadCel);
        txtCadEmail = findViewById(R.id.txtCadEmail);
        txtCadSenha = findViewById(R.id.txtCadSenha);
        txtCadConfSenha = findViewById(R.id.txtCadConfSenha);
        helpCadastroNome = findViewById(R.id.helpCadastroNome);
        helpCadastroUser = findViewById(R.id.helpCadastroUser);
        helpCadastroCel = findViewById(R.id.helpCadastroCel);
        helpCadastroEmail = findViewById(R.id.helpCadastroEmail);
        helpCadastroSenha = findViewById(R.id.helpCadastroSenha);
        helpCadastroConfSenha = findViewById(R.id.helpCadastroConfSenha);

        btCadVoltar.setOnClickListener(this);
        btCadCadastrar.setOnClickListener(this);

        //chama os dados que foram preenchidos antes de sair da tela sem salvar
        SharedPreferences prefs = getSharedPreferences("dadosCadastro", MODE_PRIVATE);
        txtCadNome.setText(prefs.getString("nome", ""));
        txtCadUser.setText(prefs.getString("user", ""));
        String setorSalvo = prefs.getString("setor", "");
        ArrayAdapter<String> adapterSetor = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.setores_array)
        );
        adapterSetor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCadSetor.setAdapter(adapterSetor);
        if (!setorSalvo.isEmpty()) {
            int pos = adapterSetor.getPosition(setorSalvo);
            if (pos >= 0) {
                spCadSetor.setSelection(pos);
            }
        }
        txtCadCel.setText(prefs.getString("celular", ""));
        txtCadEmail.setText(prefs.getString("email", ""));
        txtCadSenha.setText(prefs.getString("senha", ""));

        //Funções de Ajuda com mensagem flutuante (xml + java)
        txtCadNome.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                helpCadastroNome.setVisibility(View.VISIBLE);
            } else {
                helpCadastroNome.setVisibility(View.GONE);
            }
        });

        txtCadUser.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                helpCadastroUser.setVisibility(View.VISIBLE);
            } else {
                helpCadastroUser.setVisibility(View.GONE);
            }
        });

        txtCadCel.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                helpCadastroCel.setVisibility(View.VISIBLE);
            } else {
                helpCadastroCel.setVisibility(View.GONE);
            }
        });

        txtCadEmail.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                helpCadastroEmail.setVisibility(View.VISIBLE);
            } else {
                helpCadastroEmail.setVisibility(View.GONE);
            }
        });

        txtCadSenha.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                helpCadastroSenha.setVisibility(View.VISIBLE);
            } else {
                helpCadastroSenha.setVisibility(View.GONE);
            }
        });

        txtCadConfSenha.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                helpCadastroConfSenha.setVisibility(View.VISIBLE);
            } else {
                helpCadastroConfSenha.setVisibility(View.GONE);
            }
        });

        //Função para mascara de número de celular
        txtCadCel.addTextChangedListener(new TextWatcher() {
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
                txtCadCel.setText(formatted.toString());
                txtCadCel.setSelection(txtCadCel.getText().length());
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
        spCadSetor.setAdapter(adapterSpinner);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btCadVoltar) {
            finish();
        }
        if (v.getId() == R.id.btCadCadastrar) {
            cadastrar();
        }
    }

    //cria o metodo que se sair da tela, quando voltar não perde os dados!
    @Override
    protected void onPause() {
        super.onPause();
        getSharedPreferences("dadosCadastro", MODE_PRIVATE)
                .edit()
                .putString("nome", txtCadNome.getText().toString())
                .putString("user", txtCadUser.getText().toString())
                .putString("setor", spCadSetor.getSelectedItem().toString())
                .putString("celular", txtCadCel.getText().toString())
                .putString("email", txtCadEmail.getText().toString())
                .putString("senha", txtCadSenha.getText().toString())
                .apply();
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
    public void cadastrar() {
        String msg = "";
        String Nome = txtCadNome.getText().toString();
        String User = txtCadUser.getText().toString();
        String Setor = spCadSetor.getSelectedItem().toString();
        String Cel = txtCadCel.getText().toString();
        String Email = txtCadEmail.getText().toString();
        String Senha = txtCadSenha.getText().toString();
        String Confsenha = txtCadConfSenha.getText().toString();

        if (Nome.isEmpty() || Nome.length() < 8) {
            msg = "O campo NOME deve ser preenchido!";
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        } else {
            if (User.isEmpty() || User.length() < 3) {
                msg = "O campo USUÁRIO deve ser preenchido!";
                Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
            } else {
                if (Setor.equals("Selecione um Setor")) {
                    msg = "Por favor, selecione um SETOR válido!";
                    Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
                } else {
                    if (Cel.isEmpty() || Cel.length() < 15) {
                        msg = "Digite um número de celular válido com DDD!";
                        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
                    } else {
                        if (Email.isEmpty() || Email.length() < 10 ||
                                !Email.contains("@") || !Email.contains(".")) {
                            msg = "Digite um e-mail válido!";
                            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
                        } else {
                            if (Senha.isEmpty() || Senha.length() < 6) {
                                msg = "Digite uma senha válida com mínimo de 6 dígitos!";
                                Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
                            } else {
                                if (Confsenha.isEmpty()) {
                                    msg = "O campo CONFIRMAR SENHA deve ser preenchido!";
                                    Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
                                } else {
                                    if (!Senha.equals(Confsenha)) {
                                        msg = "Os campos SENHA e CONFIRMAR SENHA devem ser iguais!";
                                        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
                                    } else { // Verifica se e-mail já está cadastrado
                                        BancoControllerUsuarios bd = new BancoControllerUsuarios(getBaseContext());
                                        if (bd.verificarEmailCadastrado(Email)){
                                            msg = "Este e-mail já está cadastrado!";
                                            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
                                        } else {
                                            String resultado;
                                            resultado = bd.inserirDados(Nome, User, Setor, Cel, Email, Senha);
                                            Toast.makeText(getApplicationContext(), resultado, Toast.LENGTH_LONG).show();
                                            limpar();
                                            finish(); // Finaliza a tela atual
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
    private void limpar() {
        txtCadNome.setText("");
        txtCadUser.setText("");
        spCadSetor.setSelection(0);
        txtCadCel.setText("");
        txtCadEmail.setText("");
        txtCadSenha.setText("");
        txtCadConfSenha.setText("");

        getSharedPreferences("dadosCadastro", MODE_PRIVATE).edit().clear().apply();
    }
}