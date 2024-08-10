package com.example.playertool5e.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * Class the defines a dice roll log entry in database.
 */
@Entity(tableName = "diceroll")
public class DiceRoll {
    @PrimaryKey (autoGenerate = true)
    @ColumnInfo(name = "diceroll_id")
    public long id;

    /**
     * Instantiates new dice roll log entry for database.
     */
    public DiceRoll(){}

    @ColumnInfo(name = "formula")
    public String formula;

    @ColumnInfo(name = "result")
    public String result;

    @ColumnInfo(name = "individual_dice")
    public String individualDice;


    /**
     *Instantiates new dice roll log entry.
     *
     * @param formula The dice and modifiers of log entry
     * @param result The resulting number of dice roll
     * @param individualDice The result of each individual dice roll
     */
    @Ignore
    public DiceRoll(String formula, String result, String individualDice){
        this.formula = formula;
        this.result = result;
        this.individualDice = individualDice;
    }

}
