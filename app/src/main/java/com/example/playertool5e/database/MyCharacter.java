package com.example.playertool5e.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity (tableName = "character")
public class MyCharacter {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "character_id")
    public long id;

    @ColumnInfo(name = "name")
    public String name;

    public MyCharacter(){}

    @Ignore
    public MyCharacter(String name){
        this.name = name;
    }

}
