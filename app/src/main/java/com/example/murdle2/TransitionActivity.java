package com.example.murdle2;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.transition.Transition;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class TransitionActivity extends AppCompatActivity {
    //ImageView bkg;
    @Override
    //TODO change animation of btns coming in to JUST Fade
    //and make the lines drawviews that animate in
    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transitionscreen);

        Button btn1=findViewById(R.id.gamePlayBtn);
        Button btn2=findViewById(R.id.statsBtn);
        Animation inB = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.inb);
        LinearLayout cl = findViewById(R.id.clickTransition);
        Log.i("anim", "sees transition starting");
        ImageView bkg = findViewById(R.id.bkg);
        btn1.setAlpha(0);
        btn2.setAlpha(0);

        Log.i("anim", "this is onclick");
        bkg.setBackgroundResource(R.drawable.expanim);
        AnimatedVectorDrawable anim = (AnimatedVectorDrawable) bkg.getBackground();
        anim.start();
        anim.registerAnimationCallback(new Animatable2.AnimationCallback() {
            @Override
            public void onAnimationEnd(Drawable drawable) {
                Log.i("anim", "animation ended");
                btn1.startAnimation(inB);
                btn2.startAnimation(inB);
                btn1.setAlpha(1);
                btn2.setAlpha(1);
                bkg.setBackgroundColor(Color.WHITE);
                super.onAnimationEnd(drawable);
            }
        });


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
