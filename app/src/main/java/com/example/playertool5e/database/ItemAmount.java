package com.example.playertool5e.database;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
public class ItemAmount {

    public String name;
    public int amount;
    public int weight;
    public long id;
    public long characterId;

    @NonNull
    @Override
    public String toString(){
        return name + ", " + amount + ", " + weight + "\n";
    }


    public String getName(){
        return name;
    }
}
