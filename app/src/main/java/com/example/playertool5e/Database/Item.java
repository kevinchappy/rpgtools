package com.example.playertool5e.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * Class that represents an item entry in the database.
 */
@Entity(tableName = "item")
public class Item {
    @PrimaryKey (autoGenerate = true)
    @ColumnInfo(name = "item_id")
    public long id;


    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "weight")
    public int weight;

    /**
     * Instantiates new Item object for database.
     */
    public Item(){}

    /**
     * Instantiates new Item object.
     *
     * @param name name of item
     * @param weight weight of item
     */
    @Ignore
    public Item(String name, int weight){
        this.name = name;
        this.weight = weight;

    }

    /**
     * Gets item's weight.
     *
     * @return weight value of item
     */
    @Ignore
    public int getWeight() {
        return weight;
    }

    /**
     * Gets item's id.
     *
     * @return the id value of item
     */
    @Ignore
    public long getId() {
        return id;
    }

    /**
     * Gets item's name.
     *
     * @return the name value of item
     */
    @Ignore
    public String getName() {
        return name;
    }
}
