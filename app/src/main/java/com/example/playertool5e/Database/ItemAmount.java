package com.example.playertool5e.Database;

import androidx.annotation.NonNull;

/**
 * Class that represents the amount of an item that a specific character has.
 */
public class ItemAmount {

    public String name;
    public int amount;
    public int weight;
    public long id;
    public long characterId;

    /**
     * toString override.
     * name, amount, weight
     */
    @NonNull
    @Override
    public String toString(){
        return name + ", " + amount + ", " + weight + "\n";
    }

    /**
     * Gets the name of the item.
     *
     * @return the name of the item
     */
    public String getName(){
        return name;
    }
}
