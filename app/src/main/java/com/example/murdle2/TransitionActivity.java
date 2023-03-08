package com.example.murdle2;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class TransitionActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transitionscreen);

        Button btn1=findViewById(R.id.gamePlayBtn);
        Button btn2=findViewById(R.id.statsBtn);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent starter = new Intent(getApplicationContext(), GamePlayActivity.class);
                startActivity(starter);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent starter = new Intent(getApplicationContext(), StatsActivity.class);
                startActivity(starter);
            }
        });

    }
}
