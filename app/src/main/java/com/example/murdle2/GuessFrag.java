package com.example.murdle2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class GuessFrag extends Fragment {
    private String name;
    private String nameDisplay;
    private int num;
    private TextView mtxtView;
    //todo make newinstance as constructor instead
    // https://guides.codepath.com/android/creating-and-using-fragments

    //todo change order of addition with recyclerViewAdapter
    public GuessFrag(String name, int num){
        this.name= name;
        this.num = num;
        nameDisplay = displayify(name);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.guessfrag, parent, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mtxtView = view.findViewById(R.id.guessName);
        mtxtView.setText(nameDisplay);
        textEntrance();
    }

    public static String displayify(String name) {
        String answ = "";
        for(int i=0; i<name.length();i++){
            char currChar = name.charAt(i);
            if(Character.isUpperCase(currChar)){
                answ+=" ";
                answ+=Character.toLowerCase(currChar);
            }else if(currChar=='7'){
                answ += " & ";
            }
            else answ+=currChar;
        }
        return answ;
    }

    public String getName() {
        return name;
    }

    public void textEntrance() {
        Animation inL = AnimationUtils.loadAnimation(getContext(), R.anim.inl);
        mtxtView.startAnimation(inL);
    }
    //LONG TERM SOLUTION
//    getSupportFragmentManager().beginTransaction()
//        .setCustomAnimations(enter1, exit1, popEnter1, popExit1)
//        .add(R.id.container, ExampleFragment.class, null) // gets the first animations
//        .setCustomAnimations(enter2, exit2, popEnter2, popExit2)
//        .add(R.id.container, ExampleFragment.class, null) // gets the second animations
//        .commit()
}
