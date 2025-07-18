package com.example.appblessfuture;

import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ConsultarProcesso extends AppCompatActivity implements View.OnClickListener {
    SwipeRefreshLayout swipeConsultarProcessos;
    private RecyclerView rvConsultarProcessos;
    private ProcessoAdapter adapter;
    private ArrayList<Processo> listaProcessos = new ArrayList<>();
    ImageButton btConsultarProcessosVoltar, btConsultarProcessosConfig;
    TextView txtConsultarProcessosErro;
    Spinner spConsultarProcessoSetor;
    Button btConsultarProcessosFiltrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_consultar_processo);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.consultarProcesso), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        swipeConsultarProcessos = findViewById(R.id.swipeConsultarProcessos);
        btConsultarProcessosVoltar = findViewById(R.id.btConsultarProcessosVoltar);
        btConsultarProcessosConfig = findViewById(R.id.btConsultarProcessosConfig);
        rvConsultarProcessos = findViewById(R.id.rvConsultarProcessos);
        spConsultarProcessoSetor = findViewById(R.id.spConsultarProcessoSetor);
        btConsultarProcessosFiltrar = findViewById(R.id.btConsultarProcessosFiltrar);
        txtConsultarProcessosErro = findViewById(R.id.txtConsultarProcessosErro);

        btConsultarProcessosVoltar.setOnClickListener(this);
        btConsultarProcessosConfig.setOnClickListener(this);
        btConsultarProcessosFiltrar.setOnClickListener(this);

        swipeConsultarProcessos.setOnRefreshListener(() -> {
            buscarProcessos();
            Toast.makeText(this, "Atualizando dados...", Toast.LENGTH_SHORT).show();
            swipeConsultarProcessos.setRefreshing(false);
        });

        adapter = new ProcessoAdapter(listaProcessos, new ProcessoAdapter.OnProcessoAction() {
            @Override
            public void visualizar(Processo p) {
                abrirPdf(p);
            }

            @Override
            public void excluir(Processo p) {
                excluirProcesso(p);
            }
        });

        //Adapter pro Spinner
        ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(
                this,
                R.array.setores_array, // já criado no strings.xml
                R.layout.spinner_item_selected // layout do item selecionado
        );
        adapterSpinner.setDropDownViewResource(R.layout.spinner_item_dropdown); // layout do dropdown
        spConsultarProcessoSetor.setAdapter(adapterSpinner);

        rvConsultarProcessos.setLayoutManager(new LinearLayoutManager(this));
        rvConsultarProcessos.setAdapter(adapter);

        buscarProcessos(); // carrega todos inicialmente
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.btConsultarProcessosVoltar){
            finish();
        }
        if (v.getId()==R.id.btConsultarProcessosConfig){
            Intent intent = new Intent(this, Configuracao.class);
            startActivity(intent);
        }
        if (v.getId()==R.id.btConsultarProcessosFiltrar){
            buscarProcessos();
        }
    }
    private void buscarProcessos() {
        String setor = spConsultarProcessoSetor.getSelectedItem().toString();
        if (setor.equals("Selecione um Setor")) {
            setor = ""; // Sem filtro
        }
        String url = "http://10.0.2.2:8080/api_processos.php";
        if (!setor.isEmpty()) {
            url += "?setor=" + setor;
        }

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    listaProcessos.clear();
                    txtConsultarProcessosErro.setVisibility(View.GONE);
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);
                            Processo p = new Processo();
                            p.setId(obj.getInt("id"));
                            p.setNome(obj.getString("nome"));
                            p.setSetor(obj.getString("setor"));
                            p.setArquivo(obj.getString("arquivo"));
                            p.setData(obj.getString("data_upload"));
                            listaProcessos.add(p);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    adapter.notifyDataSetChanged();
                },
                error -> {
                    if (error instanceof com.android.volley.NoConnectionError || error instanceof com.android.volley.TimeoutError) {
                        txtConsultarProcessosErro.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(this, "Erro ao buscar dados", Toast.LENGTH_LONG).show();
                        txtConsultarProcessosErro.setVisibility(View.GONE);
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

    private void abrirPdf(Processo processo) {
        String url = "http://10.0.2.2:8080/uploads/" + processo.getArquivo();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(url), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    private void excluirProcesso(Processo processo) {
        String url = "http://10.0.2.2:8080/delete.php?id=" + processo.getId();

        StringRequest request = new StringRequest(Request.Method.GET, url,
                response -> {
                    Toast.makeText(this, "Excluído com sucesso!", Toast.LENGTH_SHORT).show();
                    buscarProcessos();
                },
                error -> Toast.makeText(this, "Erro ao excluir", Toast.LENGTH_SHORT).show());

        Volley.newRequestQueue(this).add(request);
    }
}
