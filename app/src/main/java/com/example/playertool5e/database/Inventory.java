package com.example.playertool5e.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

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

        public Inventory(){}

        @Ignore
        public Inventory(long characterId, long itemId, int amount){
                this.characterId = characterId;
                this.itemId = itemId;
                this.amount = amount;
        }

}
