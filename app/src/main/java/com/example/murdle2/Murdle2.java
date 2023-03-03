package com.example.murdle2;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
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
    @Override
    public void onCreate() {
        Realm.init(this);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .name("clientMurdle.realm")
                .schemaVersion(0).allowWritesOnUiThread(true)
                .build();
        Realm.setDefaultConfiguration(realmConfig);
        Realm realm = Realm.getDefaultInstance();
        Log.i("wasrealmcreated!!!", "realm init made");

    //why wont this work from async trhead
        String hi = readJSONString("myJson2.txt", getApplicationContext());
        realm.executeTransaction(transactionRealm -> {
            realm.createOrUpdateAllFromJson(MRegion.class, hi);
            Log.i("wasrealmcreated!!!", "json made");
            Log.i("wasrealmcreated!!!", realm.where(MRegion.class).findFirst().getGroup());
        });

        super.onCreate();
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
