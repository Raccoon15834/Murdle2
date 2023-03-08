package com.example.murdle2;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Region;
import android.media.Image;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ScrollView;

import androidx.core.graphics.PathParser;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.devs.vectorchildfinder.VectorChildFinder;
import com.devs.vectorchildfinder.VectorDrawableCompat;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;

import java.util.LinkedList;
import java.util.List;

//import io.realm.gradle.Realm;
import io.realm.Realm;
import io.realm.RealmResults;

public class AnatomyVIew {//This saves the state of the game
    private String answer;
    private int guessNum, currSide, currDrawable;
    private LinkedList<String> groupsGuessed; //these are all true
    Realm realm;
    Context ctx;
    ImageView gameWindow;
    //List<GuessFrag> guessedFrags;
    FragRecyclerViewAdapter frva;
    Resources res;
    public AnatomyVIew(Realm r, Context ctx, ImageView gameWindow, FragRecyclerViewAdapter frva) {
        currSide=1;
        currDrawable = R.drawable.back_sup2;
        guessNum=0;
        groupsGuessed = new LinkedList<String>();
        this.realm = r;
        this.ctx = ctx;
        this.gameWindow = gameWindow;
        this.frva = frva;
        resetScrollView();
        res = ctx.getResources();
        //guessedFrags = new LinkedList<GuessFrag>();
        // reset realm\
        realm.executeTransactionAsync( transactionRealm -> {
            RealmResults<MRegion> allofem =  transactionRealm.where(MRegion.class).findAll();
            for(MRegion currReg: allofem){
                currReg.setWasGroupGuessed(false);
            }//set random answer
            int randInt = (int)(Math.floor(Math.random()*allofem.size()));
            answer = allofem.get(randInt).getGroup();
            //Log.i("theanswerIS!", answer);
        });
    }
    public String getRegion(int x, int y, ImageView im){
        x = x*442/1080;
        y =y*410/1128;
        RealmResults<MRegion> allRegs = realm.where(MRegion.class).equalTo("side", currSide)
                .and().equalTo("wasGroupGuessed",false).findAll();
//        Log.i("hereTheyClickedXY!!!","whereClick "+ x + " and y "+ y
//                + "reg# "+allRegs.size()+ "side# "+currSide);
        Region clip = new Region(im.getLeft(), im.getTop(), im.getRight(), im.getBottom());//is the other way? for w + h
        //if (clip.contains(x,y)) Log.i("theCLIckexists",clip.toString());
        for(MRegion currReg: allRegs){
            String p = currReg.getReg();
            Region r = new Region();
            r.setPath(PathParser.createPathFromPathData(p), clip);
             if(r.contains(x, y)){
                String hereTheyClicked = currReg.getGroup();
                Log.i("actualGroupClicked!!!",hereTheyClicked);
                // Log.i(currReg.getName(),r.getBounds().toString());
                groupsGuessed.add(hereTheyClicked);
                //set all the ones they clicked to wasGroupGuessed true
                 realm.executeTransaction( transactionRealm -> {
                    RealmResults<MRegion> newSelected =  transactionRealm.where(MRegion.class).equalTo("group", hereTheyClicked).findAll();
                     //Log.i("howManyturnedSelected",newSelected.size()+"");
                    for (MRegion needsColorReg : newSelected ) {
                         needsColorReg.setWasGroupGuessed(true);
                     }
                });
                return hereTheyClicked;

            } // end if
        } //end for
        //if it IS A REGIOn in THAT side add it to list and return group name
        return "";
        //Then in the main activity if it returned ANYTHING at all- recolor();
    }
    // adding fragments
    public void addGuessFrag(GuessFrag mGF){
//        FragmentTransaction transaction = ft.beginTransaction();
//        transaction.add(R.id.guessesScroll, mGF).commit();
//        Log.i("fragWasAdded", mGF.getName());
        frva.guessData.add(0, mGF);
        frva.notifyItemInserted(0);
        //todo add to top
    }
//    public void addToGuessFragList(GuessFrag mGF){
//        guessedFrags.add(mGF);
//    }
//    public void addAllGuessFrag(LinkedList<GuessFrag> mGFs){
//        int i=0, s = mGFs.size();
//        while(i<s){
//            addGuessFrag(mGFs.get(i));
//            i++;
//        }
//    }
    private void resetScrollView(){
        frva.clear();
    }
    public String getAnswer() {
        return answer;
    }
    public int getGuessNum(){return guessNum;}
    public void incrementGuessNum() {guessNum+=1;}
    public int getCurrSide() {return currSide;}
    public void setCurrSide(int currSide) {this.currSide = currSide;}
    public void recolor(){
        //based on side, search for all the selected. TURN it red
        RealmResults<MRegion> colorRegResults = realm.where(MRegion.class).equalTo("side", currSide)
                .and().equalTo("wasGroupGuessed",true).findAll();
        //Log.i("howManyInGroup!!!",colorRegResults.size()+"");
        // all the vector stuff below
        VectorChildFinder vector = new VectorChildFinder(ctx, currDrawable, gameWindow);
        for(MRegion res: colorRegResults) {
            VectorDrawableCompat.VFullPath path1 = vector.findPathByName(res.getName());
            //Log.i("oneORegsColored!!!",res.getName());
            int r= findColorShade(res.getGroup());
            path1.setFillColor(r);
        }

    }

    public int findColorShade(String group) {
        DijkstraShortestPath<String, DefaultEdge> dijkstraAlg = new DijkstraShortestPath<>(Murdle2.prox);
        Log.i("whatDijkSees", group+ " "+answer);
        //Log.i("vertexes", Murdle2.prox.vertexSet().toString());
        int distance = dijkstraAlg.getPath(group, answer).getLength();
        Log.i("howfarwasguess",distance+ " far");
        switch (distance){
            case 0: return res.getColor(R.color.THE_RED);
            case 1: return res.getColor(R.color.red5);
            case 2: return res.getColor(R.color.red4);
            case 3: return res.getColor(R.color.red3);
            case 4: return res.getColor(R.color.red2);
            default: return res.getColor(R.color.red1);
        }
        //return Color.RED;
    }

    public int getCurrDrawable() {
        return currDrawable;
    }

    public void setCurrDrawable(int currDrawable) {
        this.currDrawable = currDrawable;
    }
}
