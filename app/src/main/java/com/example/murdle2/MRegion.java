package com.example.murdle2;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class MRegion extends RealmObject {
    @Required // disallow null if required
    @PrimaryKey
    private String name;
    //@Index //makes it easier to query, search which region your in
    private int side;
    // 1 - front, 2-back, 3- front legs, 4-back legs, 5,10,15,20
    @Index //makes it easier to query, update what should be highlighted
    private String group;
    private boolean wasGroupGuessed;
    private String reg; // the main thing- coordinates



    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSide() {
        return side;
    }

    public void setSide(int side) {
        this.side = side;
    }

    public String getReg() {
        return reg;
    }

    public void setReg(String reg) {
        this.reg = reg;
    }

    public boolean isWasGroupGuessed() {
        return wasGroupGuessed;
    }

    public void setWasGroupGuessed(boolean wasGroupGuessed) {
        this.wasGroupGuessed = wasGroupGuessed;
    }
}
