package com.example.playertool5e.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * Defines SQL operations to room database.
 */
@Dao
public interface AllDao {

    /**
     * Inserts item into database.
     *
     * @param i item to be inserted
     */
    @Insert
    void insertItem (Item i);

    /**
     * Inserts new character into database.
     *
     * @param c character to be inserted
     */
    @Insert
    void insertCharacter(MyCharacter c);

    /**
     * Inserts new DiceRollMacro into database.
     *
     * @param i DiceRollMacro to be inserted
     */
    @Insert
    void insertDice(DiceRollMacro i);

    /**
     * Inserts a list of inventory objects into the database.
     *
     * @param inventories A list of Inventory objects to be inserted
     */
    @Insert
    void insertMultipleInventories(List<Inventory> inventories);

    /**
     * Inserts a dice roll log entry into database.
     *
     * @param i dice roll to be inserted
     */
    @Insert
    void insertDiceRoll(DiceRoll i);

    /**
     * Gets a list of all character's inventory items and the amounts of items that each character has.
     *
     * @return LiveData list of all items and amounts per character
     */

    @Query("SELECT inventory.amount AS amount, item.name AS name, item.weight AS weight, inventory.inventory_id as id, inventory.character_id as characterId " +
            "FROM inventory INNER JOIN item ON inventory.item_id = item.item_id ")
    LiveData<List<ItemAmount>> getInventoryWithAmounts();

    /**
     * Checks if a character exists in the database by their id.
     *
     * @param id the id of character to check if exists
     * @return 'true' if they exist, otherwise 'false'
     */
    @Query("SELECT EXISTS(SELECT * FROM character WHERE character_id = :id)")
    boolean characterExists(long id);

    /**
     * Gets a list of all dice roll macros in database.
     *
     * @return LiveData list of all DiceRollMacro objects in the database
     */
    @Query("Select * FROM dicemacro")
    LiveData<List<DiceRollMacro>> getAllMacros();

    /**
     * Gets a list of all dice roll log entries in the database.
     *
     * @return LiveData list of all DiceRoll objects in the database
     */
    @Query("SELECT * FROM  diceroll ORDER BY diceroll_id DESC")
    LiveData<List<DiceRoll>> getDiceRolls();

    /**
     * Gets a list of all items in the database.
     *
     * @return LiveData list of all Item objects in the database
     */
    @Query("SELECT * FROM item")
    LiveData<List<Item>> getAllItems();

    /**
     * Gets a list of all characters in the database.
     *
     * @return LiveData list of all the Character objects in the database
     */
    @Query("SELECT * FROM character")
    LiveData<List<MyCharacter>> getAllCharacters();

    /**
     * Gets a character's name by their id.
     *
     * @param id the id of specified character
     * @return the name specified character
     */
    @Query("SELECT name FROM character WHERE character_id = :id")
    String getCharacterName(long id);

    /**
     * Updates the amount value in specified inventory.
     *
     * @param id id of inventory
     * @param amount new amount of item
     */
    @Query("UPDATE inventory SET amount = :amount WHERE inventory_id = :id")
    void updateInventoryAmount(long id, int amount);

    /**
     * Updates specified character's name.
     *
     * @param id id of character
     * @param name character's new name
     */
    @Query("UPDATE character SET name = :name WHERE character_id = :id")
    void updateCharacterName(long id, String name);

    /**
     * Updates specified item's name and weight.
     *
     * @param id id of item
     * @param name item's new name
     * @param weight item's new weight
     */
    @Query("UPDATE item SET name = :name, weight = :weight WHERE item_id = :id")
    void updateItem(long id, String name, int weight);

    /**
     * Deletes specified Inventory from database by id.
     *
     * @param id id of inventory to be deleted
     */
    @Query("DELETE FROM inventory WHERE inventory.inventory_id = :id")
    void deleteInventory(long id);

    /**
     * Deletes specified Item from database by id.
     *
     * @param id id of item to be deleted
     */
    @Query("DELETE FROM item WHERE item_id = :id")
    void deleteItem(long id);

    /**
     * Deletes specified Character from database by id.
     *
     * @param id id of character to be deleted
     */
    @Query("DELETE FROM character WHERE character_id = :id")
    void deleteCharacter(long id);


    /**
     * Deletes specified DiceRollMacro from database by id.
     *
     * @param id id of dice roll macro to be deleted
     */
    @Query("DELETE FROM dicemacro WHERE id = :id")
    void deleteDie(long id);

    /**
     * Deletes all dice roll log entries from database.
     */
    @Query("DELETE FROM diceroll")
    void nukeDiceLog();
}
