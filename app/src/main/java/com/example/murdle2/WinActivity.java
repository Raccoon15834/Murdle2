package com.example.murdle2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class WinActivity extends AppCompatActivity {
    TextView winText;
    String str1 = "The Mystery Muscle Is...\n";
    String tmp;
    Animation inB, outB;
    int numOfClicks;
    boolean leaveOption;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win);
        winText = findViewById(R.id.winText);
        Context c= getApplicationContext();
        outB  = AnimationUtils.loadAnimation(c, R.anim.outb);
        inB = AnimationUtils.loadAnimation(c, R.anim.inb);
        Bundle b = getIntent().getExtras();
        tmp = b.getString("name");
        tmp = GuessFrag.displayify(tmp);
        int gs = b.getInt("guessNum");
        leaveOption=false;

        numOfClicks=0;
        animateText(gs+" guesses", false);

    }
    public void animateText(String s, boolean willSetLeaveOption) {
        TextView v= winText;
        outB.setAnimationListener(new Animation.AnimationListener()
        {
            @Override public void onAnimationStart(Animation animation) {}
            @Override public void onAnimationRepeat(Animation animation) {}
            @Override public void onAnimationEnd(Animation animation)
            {
                winText.setText(s);
                v.startAnimation(inB);
                if(willSetLeaveOption==true) leaveOption=true;
            }
        });
        v.startAnimation(outB);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        numOfClicks++;
        if(numOfClicks==1) animateText(str1+tmp, true);
        if(numOfClicks>1 && leaveOption==true){
            Intent starter = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(starter);
        }
        return super.onTouchEvent(event);
    }
}
