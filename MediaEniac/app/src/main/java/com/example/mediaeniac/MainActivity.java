package com.example.mediaeniac;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText txtExercicios, txtPortifolio, txtProva, txtMedia;
    Button btCalcular, btLimpar;
    ImageView imagem;

    @Override
    protected void onCreate (Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    EdgeToEdge.enable(this);
    setContentView(R.layout.activity_main);
    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
        Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
        v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
        return insets;
    });

    txtExercicios = findViewById(R.id.txtExercicio);
    txtPortifolio = findViewById(R.id.txtPortifolio);
    txtProva = findViewById(R.id.txtProva);
    txtMedia = findViewById(R.id.txtMedia);
    btCalcular = findViewById(R.id.btCalcular);
    btLimpar = findViewById(R.id.btLimpar);
    imagem = findViewById(R.id.imagem);

    btCalcular.setOnClickListener(this);
    btLimpar.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.btLimpar){
            txtExercicios.setText("");
            txtPortifolio.setText("");
            txtProva.setText("");
            txtMedia.setText("");
            imagem.setImageResource(R.drawable.imc);
        }
        if (v.getId()==R.id.btCalcular){
            float exercicios = Float.parseFloat(txtExercicios.getText().toString());
            float portifolio = Float.parseFloat(txtPortifolio.getText().toString());
            float prova = Float.parseFloat(txtProva.getText().toString());
            float media = ((exercicios * 2)+(portifolio * 3)+(prova * 5))/10;

            if (media >= 6){
                imagem.setImageResource(R.drawable.sucesso2);
            }else {
                imagem.setImageResource(R.drawable.triste);
            }
        }
    }

}