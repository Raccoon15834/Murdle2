package com.example.murdle2;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Scanner;


import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

//The first time the app is downloaded, local client realm is intialized
//JSON muscle objects are put in
public class Murdle2 extends Application {
    public static SimpleGraph<String, DefaultEdge> prox;//muscle proximities
    @Override
    public void onCreate() {
        Realm.init(this);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .name("clientMurdle.realm")
                .schemaVersion(0).allowWritesOnUiThread(true)
                .build();
        Realm.setDefaultConfiguration(realmConfig);
        Realm realm = Realm.getDefaultInstance();
        //Log.i("wasrealmcreated!!!", "realm init made");

        prox = new SimpleGraph<>(DefaultEdge.class);
        setUpAdjacencies();



    //why wont this work from async trhead
        String hi = readJSONString("myJson2.txt", getApplicationContext());

        realm.executeTransaction(transactionRealm -> {
            //realm.deleteAll();//to reset
            realm.createOrUpdateAllFromJson(MRegion.class, hi);
            //Log.i("wasrealmcreated!!!", "json made");
            //Log.i("wasrealmcreated!!!", realm.where(MRegion.class).findFirst().getGroup());
        });

        super.onCreate();
    }

    private void setUpAdjacencies() {
        try {
            //Log.i("wasGraphcreated!!!", "graph made");
            DataInputStream textFileStream = new DataInputStream(getAssets().open(String.format("myAdjacencies.txt")));
            Scanner sc = new Scanner(textFileStream);
            while(sc.hasNext()){
                String curr = sc.next().trim();
                prox.addVertex(curr);
                String[] connectsTo = sc.next().trim().split(",");
                for(String i: connectsTo){
                    prox.addVertex(i);
                    prox.addEdge(i,curr);
                }
            }
            sc.close();
            //Log.i("vertexes?!!!", prox.vertexSet().toString());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static String readJSONString(String filename, Context context){
        String answ ="";
        try {
            InputStream is = context.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            answ = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return answ;
    }
}
