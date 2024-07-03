package com.example.playertool5e.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "item")
public class Item {
    @PrimaryKey (autoGenerate = true)
    @ColumnInfo(name = "item_id")
    public long id;


    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "weight")
    public int weight;



    public Item(){}

    @Ignore
    public Item(String name, int weight){
        this.name = name;
        this.weight = weight;

    }

    @Ignore
    public int getWeight() {
        return weight;
    }

    @Ignore
    public long getId() {
        return id;
    }

    @Ignore
    public String getName() {
        return name;
    }
}
