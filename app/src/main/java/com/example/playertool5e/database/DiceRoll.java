package com.example.playertool5e.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "diceroll")

public class DiceRoll {
    @PrimaryKey (autoGenerate = true)
    @ColumnInfo(name = "diceroll_id")
    public long id;

public DiceRoll(){}

    @ColumnInfo(name = "formula")
    public String formula;

    @ColumnInfo(name = "result")
    public String result;

    @ColumnInfo(name = "individual_dice")
    public String individualDice;


    @Ignore
    public DiceRoll(String formula, String result, String individualDice){
        this.formula = formula;
        this.result = result;
        this.individualDice = individualDice;
    }

}
