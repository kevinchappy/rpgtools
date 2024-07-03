package com.example.playertool5e.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "dicemacro")
public class DiceRollMacro {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "diesize")
    public long dieSize;


    public DiceRollMacro(){}

    @Ignore
    public DiceRollMacro(long dieSize){
        this.dieSize = dieSize;

    }

    public long getDieSize() {
        return dieSize;
    }

    public long getId() {
        return id;
    }
}
