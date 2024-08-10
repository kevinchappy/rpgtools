package com.example.playertool5e.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * Singleton class that contains the app database.
 */
@Database(entities = {MyCharacter.class, Item.class, Inventory.class, DiceRollMacro.class, DiceRoll.class}, version = 1)
public abstract class MyDatabase extends RoomDatabase {

    public abstract AllDao allDao();

    private static volatile MyDatabase INSTANCE;

    /**
     * Instantiates new MyDatabase for database impl.
     */
    MyDatabase(){}

    /**
     * Gets the instance of the database or creates a new one if it does not exist.
     * Allows main thread queries to stop race conditions when creating certain classes.
     *
     * @param context app context
     * @return the instance of the database
     */
    public synchronized static MyDatabase getInstance(Context context){
        if(INSTANCE == null) {

            INSTANCE = Room.databaseBuilder(context, MyDatabase.class, "rpg_tool_database_2").allowMainThreadQueries().build();

        }
        return INSTANCE;
    }
}
