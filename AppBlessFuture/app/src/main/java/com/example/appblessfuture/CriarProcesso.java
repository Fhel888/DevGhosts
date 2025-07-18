package com.example.appblessfuture;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.io.IOException;
import java.io.InputStream;

public class CriarProcesso extends AppCompatActivity implements View.OnClickListener {
    SwipeRefreshLayout swipeCriarProcesso;
    ImageButton btCriaProcessoVoltar , btCriaProcessoConfig;
    EditText txtCriaProcessoNome;
    Spinner spCriaProcessoSetor;
    Button btCriaProcessoPdf, btCriaProcessoEnviar;
    TextView txtCriaProcessoNenhum, txtCriaProcessoErro;
    private static final int PICK_PDF_REQUEST = 1;
    private Uri pdfUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_criar_processo);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.criarProcesso), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        swipeCriarProcesso = findViewById(R.id.swipeCriarProcesso);
        btCriaProcessoVoltar = findViewById(R.id.btCriaProcessoVoltar);
        btCriaProcessoConfig = findViewById(R.id.btCriaProcessoConfig);
        txtCriaProcessoNome = findViewById(R.id.txtCriaProcessoNome);
        spCriaProcessoSetor = findViewById(R.id.spCriaProcessoSetor);
        btCriaProcessoPdf = findViewById(R.id.btCriaProcessoPdf);
        btCriaProcessoEnviar = findViewById(R.id.btCriaProcessoEnviar);
        txtCriaProcessoNenhum = findViewById(R.id.txtCriaProcessoNenhum);
        txtCriaProcessoErro = findViewById(R.id.txtCriaProcessoErro);

        btCriaProcessoVoltar.setOnClickListener(this);
        btCriaProcessoConfig.setOnClickListener(this);
        btCriaProcessoPdf.setOnClickListener(this);
        btCriaProcessoEnviar.setOnClickListener(this);

        swipeCriarProcesso.setOnRefreshListener(() -> {
            verificarConexaoERP();
            Toast.makeText(this, "Atualizando dados...", Toast.LENGTH_SHORT).show();
            swipeCriarProcesso.setRefreshing(false);
        });

        //Adapter pro Spinner
        ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(
                this,
                R.array.setores_array, // já criado no strings.xml
                R.layout.spinner_item_selected // layout do item selecionado
        );
        adapterSpinner.setDropDownViewResource(R.layout.spinner_item_dropdown); // layout do dropdown
        spCriaProcessoSetor.setAdapter(adapterSpinner);

        verificarConexaoERP();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btCriaProcessoVoltar) {
            finish();
        }
        if (v.getId() == R.id.btCriaProcessoConfig) {
            Intent intent = new Intent(this, Configuracao.class);
            startActivity(intent);
        }
        if (v.getId() == R.id.btCriaProcessoPdf) {
            openFileChooser();
        }
        if (v.getId() == R.id.btCriaProcessoEnviar){
            if (pdfUri != null) {
                uploadPDF();
            } else {
                Toast.makeText(this, "Selecione um arquivo PDF", Toast.LENGTH_SHORT).show();
            }
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

    private void verificarConexaoERP() {
        String url = "http://10.0.2.2:8080/api_processos.php";

        com.android.volley.toolbox.StringRequest request = new com.android.volley.toolbox.StringRequest(
                com.android.volley.Request.Method.GET,
                url,
                response -> {
                    txtCriaProcessoErro.setVisibility(View.GONE);
                    Toast.makeText(this, "ERP conectada", Toast.LENGTH_SHORT).show();
                },
                error -> {
                    if (error instanceof com.android.volley.NoConnectionError || error instanceof com.android.volley.TimeoutError) {
                        txtCriaProcessoErro.setVisibility(View.VISIBLE);
                    } else {
                        txtCriaProcessoErro.setVisibility(View.GONE);
                    }
                }
        );
        // Definir timeout curto se quiser resposta rápida
        request.setRetryPolicy(new DefaultRetryPolicy(
                3000, // timeout inicial: 3 segundos
                0,    // número máximo de tentativas de retry
                1f    // multiplicador (não importa aqui porque tentativas = 0)
        ));
        Volley.newRequestQueue(this).add(request);
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        startActivityForResult(intent, PICK_PDF_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null) {
            pdfUri = data.getData();
            txtCriaProcessoNenhum.setText("Arquivo: " + getFileName(pdfUri));
        }
    }

    private String getFileName(Uri uri) {
        String result = "";
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            result = cursor.getString(index);
            cursor.close();
        }
        return result;
    }

    private void uploadPDF() {
        String nome = txtCriaProcessoNome.getText().toString().trim();
        String setor = spCriaProcessoSetor.getSelectedItem().toString();
        if (setor.equals("Selecione um Setor")) {
            Toast.makeText(this, "Por favor, selecione um setor válido", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            InputStream inputStream = getContentResolver().openInputStream(pdfUri);
            byte[] pdfBytes = new byte[inputStream.available()];
            inputStream.read(pdfBytes);
            inputStream.close();

            String url = "http://10.0.2.2:8080/upload.php";

            MultipartRequest request = new MultipartRequest(url, nome, setor, pdfBytes, getFileName(pdfUri),
                    response -> {
                Toast.makeText(CriarProcesso.this, "Processo enviado com sucesso!", Toast.LENGTH_LONG).show();
                //limpar os campos da tela após o envio
                txtCriaProcessoNome.setText("");
                spCriaProcessoSetor.setSelection(0);
                txtCriaProcessoNenhum.setText("Nenhum arquivo selecionado");
                pdfUri = null;
                    },
                    error -> Toast.makeText(CriarProcesso.this, "Erro ao enviar: " + error.getMessage(), Toast.LENGTH_LONG).show());

            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(request);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}