package com.example.appblessfuture;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.MainActivity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        logo = findViewById(R.id.imgMainLogo);

        //1ª animação (rotação)
        ObjectAnimator rotation = ObjectAnimator.ofFloat(logo, "rotationY", 0f, 360f);
        rotation.setDuration(1500); // 1.5 segundos
        rotation.setInterpolator(new DecelerateInterpolator());

        // Quando a animação acabar, próxima função:
        rotation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                //2ª animação (encolher)
                ObjectAnimator shrinkX = ObjectAnimator.ofFloat(logo, "scaleX", 1f, 0f);
                ObjectAnimator shrinkY = ObjectAnimator.ofFloat(logo, "scaleY", 1f, 0f);
                shrinkX.setDuration(900);
                shrinkY.setDuration(900);

                // Quando terminar a animação de encolher, abrir a próxima tela
                shrinkY.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        Intent intent = new Intent(MainActivity.this, Login.class);
                        startActivity(intent);
                        finish();
                    }
                });

                shrinkX.start();
                shrinkY.start();
            }
        });

        rotation.start();
    }
}