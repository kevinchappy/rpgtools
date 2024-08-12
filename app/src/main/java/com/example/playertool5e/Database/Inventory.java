package com.example.playertool5e.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * Class that represents an inventory entry in database.
 * Has foreign keys of an item's id and a character's id.
 */
@Entity(tableName = "inventory",
        foreignKeys = {
                @ForeignKey(
                        entity = Item.class,
                        parentColumns = {"item_id"},
                        childColumns = {"item_id"},
                        onDelete = ForeignKey.CASCADE,
                        onUpdate = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = MyCharacter.class,
                        parentColumns = {"character_id"},
                        childColumns = {"character_id"},
                        onDelete = ForeignKey.CASCADE,
                        onUpdate = ForeignKey.CASCADE
                )
        })
public class Inventory {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "inventory_id")
    public long id;
    @ColumnInfo(name = "character_id")
    public long characterId;
    @ColumnInfo(name = "item_id")
    public long itemId;
    @ColumnInfo(name = "amount")
    public int amount;

    /**
     * Instantiates new Inventory for database.
     */
    public Inventory() {
    }

    /**
     * Instantiates new Inventory.
     *
     * @param characterId id of character that owns this inventories item
     * @param itemId      id of item that character owns
     * @param amount      the amount of the item that the character has.
     */
    @Ignore
    public Inventory(long characterId, long itemId, int amount) {
        this.characterId = characterId;
        this.itemId = itemId;
        this.amount = amount;
    }

}
