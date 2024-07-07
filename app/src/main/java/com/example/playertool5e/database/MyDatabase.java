package com.example.playertool5e.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {MyCharacter.class, Item.class, Inventory.class, DiceRollMacro.class, DiceRoll.class}, version = 1)
public abstract class MyDatabase extends RoomDatabase {

    public abstract AllDao allDao();

    private static volatile MyDatabase INSTANCE;

    MyDatabase(){}

    public synchronized static MyDatabase getInstance(Context context){
        if(INSTANCE == null) {

            INSTANCE = Room.databaseBuilder(context, MyDatabase.class, "rpg_tool_database_2").allowMainThreadQueries().build();

        }
        return INSTANCE;
    }
}
