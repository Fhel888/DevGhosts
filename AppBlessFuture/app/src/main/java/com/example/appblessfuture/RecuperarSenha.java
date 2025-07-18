package com.example.appblessfuture;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RecuperarSenha extends AppCompatActivity implements View.OnClickListener {
    ImageButton btRecVoltar;
    Button btRecCodigo, btRecAltSenha;
    EditText txtRecEmail, txtRecCodigo;
    TextView helpRecEmail, helpRecCodigo;
    String codigoGerado = "";
    String emailValidado = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recuperar_senha);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.recuperarSenha), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btRecVoltar = findViewById(R.id.btRecVoltar);
        btRecCodigo = findViewById(R.id.btRecCodigo);
        btRecAltSenha = findViewById(R.id.btRecAltSenha);
        txtRecEmail = findViewById(R.id.txtRecEmail);
        txtRecCodigo = findViewById(R.id.txtRecCodigo);
        helpRecEmail = findViewById(R.id.helpRecEmail);
        helpRecCodigo = findViewById(R.id.helpRecCodigo);

        btRecVoltar.setOnClickListener(this);
        btRecCodigo.setOnClickListener(this);
        btRecAltSenha.setOnClickListener(this);

        //Funções de Ajuda com mensagem flutuante (xml + java)
        txtRecEmail.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                helpRecEmail.setVisibility(View.VISIBLE);
            } else {
                helpRecEmail.setVisibility(View.GONE);
            }
        });
        txtRecCodigo.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                helpRecCodigo.setVisibility(View.VISIBLE);
            } else {
                helpRecCodigo.setVisibility(View.GONE);
            }
        });
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btRecVoltar) {
            finish();
        }
        if (v.getId() == R.id.btRecCodigo) {
            enviarCodigo();
        }
        if (v.getId() == R.id.btRecAltSenha) {
            verificarCodigo();
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
    public void enviarCodigo(){
        String email = txtRecEmail.getText().toString().trim();

        if (email.isEmpty() || email.length() < 10 || !email.contains("@") || !email.contains(".")) {
            Toast.makeText(this, "Digite um e-mail válido", Toast.LENGTH_SHORT).show();
            return;
        }
        BancoControllerUsuarios bd = new BancoControllerUsuarios(this);
        if (!bd.verificarEmailRecup(email)) {
            Toast.makeText(this, "E-mail não encontrado", Toast.LENGTH_SHORT).show();
            return;
        }
        codigoGerado = gerarCodigo();
        emailValidado = email; //salva para atualizar depois

        //Enviar o código para o e-mail (por enquanto, simular)
        Toast.makeText(this, "Código enviado: " + codigoGerado, Toast.LENGTH_LONG).show();
    }
    private String gerarCodigo() {
        return String.valueOf((int)(Math.random() * 399999 + 100000)); // ex: 6 dígitos
    }
    public void verificarCodigo() {
        String codigoDigitado = txtRecCodigo.getText().toString().trim();

        if (codigoDigitado.equalsIgnoreCase(codigoGerado)) {
            abrirDialogNovaSenha();
        } else {
            Toast.makeText(this, "Código incorreto!", Toast.LENGTH_SHORT).show();
        }
    }
    public void abrirDialogNovaSenha(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View telaAlterar = getLayoutInflater().inflate(R.layout.alterar_senha, null);

        EditText novaSenha = telaAlterar.findViewById(R.id.novaSenha);
        EditText confNovaSenha = telaAlterar.findViewById(R.id.confNovaSenha);
        Button btNewSenha = telaAlterar.findViewById(R.id.btNewSenha);

        builder.setView(telaAlterar);
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        btNewSenha.setOnClickListener(v -> {
            String s1 = novaSenha.getText().toString().trim();
            String s2 = confNovaSenha.getText().toString().trim();

            if (s1.length() < 6){
                Toast.makeText(this, "Senha muito curta!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!s1.equals(s2)) {
                Toast.makeText(this, "As senhas não estão iguais!", Toast.LENGTH_SHORT).show();
                return;
            }
            BancoControllerUsuarios bd = new BancoControllerUsuarios(this);
            String resultado = bd.atualizarSenhaRecup(emailValidado, s1);
            Toast.makeText(this, resultado, Toast.LENGTH_LONG).show();
            dialog.dismiss();
            finish();
        });
    }
}