package com.example.playertool5e.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * Class that defines a dice roll macro.
 */
@Entity(tableName = "dicemacro")
public class DiceRollMacro {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "diesize")
    public long dieSize;


    /**
     * Instantiates new DiceRollMacro for database.
     */
    public DiceRollMacro() {
    }

    /**
     * Instantiates new DiceRollMacro
     *
     * @param dieSize size of die that is rolled with the macro.
     */
    @Ignore
    public DiceRollMacro(long dieSize) {
        this.dieSize = dieSize;

    }

    public long getId() {
        return id;
    }
}
