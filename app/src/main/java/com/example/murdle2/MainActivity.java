package com.example.murdle2;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devs.vectorchildfinder.VectorChildFinder;
import com.devs.vectorchildfinder.VectorDrawableCompat;
import com.google.gson.Gson;

//import io.realm.gradle.Realm;
import java.util.Iterator;
import java.util.LinkedList;

import io.realm.Realm;
import io.realm.RealmConfiguration;


// realm app id - applicationfx-tgpga

//type-mode for the muscles
    //replace scrollview with typed view, new game
//cool transition to profile, and stats
//sharedprefs
//add users stats to a beautiful log
//directions gifs scrollview
//~~~~change guessfrag to recycler view,
//~~~~ add feedback number to guessfrag

//awesome video!!!!!!!!!! Learning muscles is mindboggling! diagrams are hard to wrap your head around,
//and once you do, you forget!, Now you can learn the muscles in a gamified way

public class MainActivity extends AppCompatActivity {
    private Realm realm;
    ImageView gameWindow;
    ImageView gameWindowBkg;
    RecyclerView guessRecord;
    AnatomyVIew game;
    boolean secondpointerSyndrome;
    Drawable backSup, backDeep, frontSup, frontDeep,
            backLSup, backLDeep, frontLSup, frontLDeep;
    float orix, oriy, orix2, oriy2;
    FragmentManager ftm;
    Animation inL, outL, inR, outR, inT, outT, inB, outB, outbkg, inbkg;
    int MINCLICKDIST = 50, MINSWIPEDIST = 230;
    int mode;
    AppCompatActivity aca;
    FragRecyclerViewAdapter adapter;
    @SuppressLint("ClickableViewAccessibility")
    //can try making it accessible for visually impaired individuals,
    //but for now the app's whole purpose is to be visual tool
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        secondpointerSyndrome = false;
        gameWindow = findViewById(R.id.gameWindow);
        gameWindowBkg = findViewById(R.id.gameWindowBkg);
        guessRecord = findViewById(R.id.guessesScroll);
        backSup = getDrawable(R.drawable.back_sup2);
        backDeep = getDrawable(R.drawable.back_deep);
        frontSup = getDrawable(R.drawable.front_sup);
        frontDeep = getDrawable(R.drawable.front_deep);
        backLSup = getDrawable(R.drawable.legback_sup);
        backLDeep = getDrawable(R.drawable.legback_deep);
        frontLSup = getDrawable(R.drawable.legfront_sup);
        frontLDeep = getDrawable(R.drawable.legfront_deep);
        DisplayMetrics metrics = new DisplayMetrics();
        Context c = getApplicationContext();
        aca=this;

        inL = AnimationUtils.loadAnimation(c, R.anim.inl);
        outL  = AnimationUtils.loadAnimation(c, R.anim.outl);
        inR = AnimationUtils.loadAnimation(c, R.anim.inr);
        outR  = AnimationUtils.loadAnimation(c, R.anim.outr);
        inT = AnimationUtils.loadAnimation(c, R.anim.intop);
        outT  = AnimationUtils.loadAnimation(c, R.anim.outt);
        inB = AnimationUtils.loadAnimation(c, R.anim.inb);
        outB  = AnimationUtils.loadAnimation(c, R.anim.outb);
        inbkg = AnimationUtils.loadAnimation(c, R.anim.inbkg);
        outbkg  = AnimationUtils.loadAnimation(c, R.anim.outbkg);
        setupSettings();

        realm = Realm.getDefaultInstance();

        guessRecord.setLayoutManager(new LinearLayoutManager(this));
         adapter = new FragRecyclerViewAdapter(this, new LinkedList<GuessFrag>());
        guessRecord.setAdapter(adapter);
        //shared prefs gets game stats, and what side it was on
//        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
//        mode = mPrefs.getInt("mode", 1);
//        if (mode==1){
//          guessRecord.setLayoutManager(new LinearLayoutManager(this));
//          adapter = new FragRecyclerViewAdapter(this, new LinkedList<GuessFrag>());
//          guessRecord.setAdapter(adapter);
//        }else{
//
//        }
//        game.addAllGuessFrag(game.getCurrFragList());
        game = new AnatomyVIew(realm, getApplicationContext(), gameWindow, adapter);
        resetGameSide(1);
        game.recolor();
        gameWindow.setOnTouchListener(new gameViewListener());

    }

    private void setupSettings() {
        ImageButton tbtn = findViewById(R.id.transitionBtn);
        tbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent starter = new Intent(getApplicationContext(), TransitionActivity.class);
                ActivityOptions options = ActivityOptions
                        .makeSceneTransitionAnimation(aca, findViewById(R.id.circTransition), "circ");
                startActivity(starter);
            }
        });
    }

    private void resetGameSide(int s) {
        game.setCurrSide(s);
        switch (s){
            case 1:
                gameWindow.setImageDrawable(backSup);
                gameWindowBkg.setImageDrawable(backDeep);
                gameWindowBkg.setAlpha(180);
                game.setCurrDrawable(R.drawable.back_sup2);
                break;
            case 2:
                gameWindow.setImageDrawable(frontSup);
                gameWindowBkg.setImageDrawable(frontDeep);
                gameWindowBkg.setAlpha(180);
                game.setCurrDrawable(R.drawable.front_sup);
                break;
            case 3:
                gameWindow.setImageDrawable(backLSup);
                gameWindowBkg.setImageDrawable(backLDeep);
                gameWindowBkg.setAlpha(180);
                game.setCurrDrawable(R.drawable.legback_sup);
                break;
            case 4:
                gameWindow.setImageDrawable(frontLSup);
                gameWindowBkg.setImageDrawable(frontLDeep);
                gameWindowBkg.setAlpha(180);
                game.setCurrDrawable(R.drawable.legfront_sup);
                break;
            case 5:
                gameWindow.setImageDrawable(backDeep);
                gameWindowBkg.setAlpha(0);
                game.setCurrDrawable(R.drawable.back_deep);
                break;
            case 10:
                gameWindow.setImageDrawable(frontDeep);
                gameWindowBkg.setAlpha(0);
                game.setCurrDrawable(R.drawable.front_deep);
                break;
            case 15:
                gameWindow.setImageDrawable(backLDeep);
                gameWindowBkg.setAlpha(0);
                game.setCurrDrawable(R.drawable.legback_deep);
                break;
            case 20:
                gameWindow.setImageDrawable(frontLDeep);
                gameWindowBkg.setAlpha(0);
                game.setCurrDrawable(R.drawable.legfront_deep);
                break;
        }
        game.recolor();
    }


    private class gameViewListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if(motionEvent.getActionMasked()==MotionEvent.ACTION_UP && secondpointerSyndrome==true){
                secondpointerSyndrome=false;
                return true;
            }
            switch(motionEvent.getActionMasked()){
                case MotionEvent.ACTION_DOWN:
                    orix = motionEvent.getX();
                    oriy = motionEvent.getY();
                    break;
                case MotionEvent.ACTION_UP:
                    double distx =Math.abs(orix-motionEvent.getX());
                    double disty = Math.abs(oriy-motionEvent.getY());
                    if (distx>MINSWIPEDIST && distx>disty) swipeLeftRight(orix-motionEvent.getX());
                    if (disty>MINSWIPEDIST && disty>distx) swipeUpDown(oriy-motionEvent.getY());
                    if ((disty)<MINCLICKDIST && distx<MINCLICKDIST) {
                        String clickedReg = game.getRegion((int)motionEvent.getX(), (int)motionEvent.getY(), gameWindow);
                        if(clickedReg.equals(game.getAnswer())) {
                            int numittook = game.getGuessNum()+1;
                            game = new AnatomyVIew(realm, getApplicationContext(), gameWindow, adapter);
                            Intent starter = new Intent(getApplicationContext(), WinActivity.class);
                            Bundle extras = new Bundle();
                            extras.putString("name", clickedReg);
                            extras.putInt("guessNum", numittook);
                            starter.putExtras(extras);
                            startActivity(starter);
                            return false;
                        }
                        if(!clickedReg.equals("")) {
                            game.recolor();
                            game.incrementGuessNum();
                            GuessFrag tmpgf = new GuessFrag(clickedReg, game.findColorShade(clickedReg));
                            //if (mode==1){
                                game.addGuessFrag(tmpgf);
                                //scroll to top
                                guessRecord.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        guessRecord.smoothScrollToPosition(0);
                                    }
                                });
                            //}
                        }
                    }
                    break;
                case MotionEvent.ACTION_POINTER_DOWN: //checking double touch swipes
                    orix2 = motionEvent.getX(1);
                    oriy2 = motionEvent.getY(1);
                    secondpointerSyndrome=true;
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    double nowx2 = motionEvent.getX(1);
                    double nowy2 = motionEvent.getY(1);
                    double nowx = motionEvent.getX();
                    double nowy = motionEvent.getY();
                    double distEnd = Math.sqrt(Math.pow(nowx-nowx2,2)+Math.pow(nowy-nowy2, 2));
                    double distFirst = Math.sqrt(Math.pow(orix-orix2,2)+Math.pow(oriy-oriy2, 2));
                    if(distEnd>distFirst) peel();
                    if(distEnd<distFirst) cover();
                    break;
            }
            return true;

        }

        private void peel() { //todo set out fade
            int currSide=game.getCurrSide();
            if(currSide<5) animateReset2(currSide*5, outbkg, inbkg);
        }
        private void cover() {
            int currSide=game.getCurrSide();
            if(currSide>4) animateReset2(currSide/5, outbkg, inbkg);
        }

        private void swipeUpDown(float v) {
            Animation thisIn, thisOut;
            if(v<0){thisIn=inB; thisOut=outB;}//bottom to top
            else{thisIn=inT; thisOut=outT;}//top to bottom
            if (v<0){
            switch(game.getCurrSide()){
                case 3: //back of legs
                case 15: animateReset(1, thisIn, thisOut); break;
                case 4: //front of legs
                case 20: animateReset(2, thisIn, thisOut); break;
            }}
            else{
            switch(game.getCurrSide()){
                case 1:
                case 5: animateReset(3, thisIn, thisOut); break;
                case 2:
                case 10: animateReset(4, thisIn, thisOut); break;
            }}
        }
        private void swipeLeftRight(float v) {
            Animation thisIn, thisOut;
            if(v>0){thisIn=inR; thisOut=outR;}//right to left
            else{thisIn=inL; thisOut=outL;}//left to right
            switch(game.getCurrSide()){
                case 1: animateReset(2, thisIn, thisOut); break;
                case 5: animateReset(2, thisIn, thisOut); break;
                case 2: animateReset(1, thisIn, thisOut); break;
                case 10: animateReset(1, thisIn, thisOut); break;
                case 3: animateReset(4, thisIn, thisOut); break;
                case 15: animateReset(4, thisIn, thisOut); break;
                case 4: animateReset(3, thisIn, thisOut); break;
                case 20: animateReset(3, thisIn, thisOut); break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
        //shared prefs saves game stats, and what side it was on
//        Gson gson = new Gson();
//        String gameJSON = gson.toJson(game);

//        SharedPreferences.Editor mPrefEditor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
//        mPrefEditor.putString("gamejson", gameJSON);
//        mPrefEditor.apply();
    }
    //instead of just resetting image, gonna animated fade left or right
    public void animateReset(int s, Animation in, Animation out) {
        out.setAnimationListener(new Animation.AnimationListener()
        {
            @Override public void onAnimationStart(Animation animation) {}
            @Override public void onAnimationRepeat(Animation animation) {}
            @Override public void onAnimationEnd(Animation animation)
            {
                resetGameSide(s);
                gameWindow.startAnimation(in);
                gameWindowBkg.startAnimation(inbkg);
            }
        });
        gameWindow.startAnimation(out);
        gameWindowBkg.startAnimation(outbkg);

    }
    public void animateReset2(int s, Animation windowAnim, Animation bkgAnim){
        outbkg.setAnimationListener(new Animation.AnimationListener() {
            @Override public void onAnimationStart(Animation animation) {}
            @Override public void onAnimationRepeat(Animation animation) {}
            @Override public void onAnimationEnd(Animation animation)
            {
                resetGameSide(s);
            }
        });
        gameWindow.startAnimation(windowAnim);
        gameWindowBkg.startAnimation(bkgAnim);
    }


}
