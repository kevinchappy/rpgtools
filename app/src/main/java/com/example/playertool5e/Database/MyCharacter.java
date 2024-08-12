package com.example.playertool5e.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * Class that represents a character entry in database.
 */
@Entity(tableName = "character")
public class MyCharacter {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "character_id")
    public long id;

    @ColumnInfo(name = "name")
    public String name;

    /**
     * Instantiates new character for database.
     */
    public MyCharacter() {
    }

    /**
     * Instantiates new character.
     *
     * @param name the name of the character
     */
    @Ignore
    public MyCharacter(String name) {
        this.name = name;
    }

}
