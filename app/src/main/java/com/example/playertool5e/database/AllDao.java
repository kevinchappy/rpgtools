package com.example.playertool5e.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
@Dao
public interface AllDao {

    @Insert
    long insertItem (Item i);
    @Insert
    long insertCharacter(MyCharacter c);
    @Insert
    long insertInventory(Inventory i);
    @Insert
    long insertDice(DiceRollMacro i);
    @Insert
    void insertMultipleInventories(List<Inventory> inventories);
    @Insert
    void insertDiceROll(DiceRoll i);

    @Query("SELECT * FROM inventory WHERE character_id = :character_id")
    List<Inventory> getCharacterInventory(long character_id);


    @Query("SELECT inventory.amount AS amount, item.name AS name, item.weight AS weight, inventory.inventory_id as id, inventory.character_id as characterId " +
            "FROM inventory INNER JOIN item ON inventory.item_id = item.item_id ")
    LiveData<List<ItemAmount>> getInventoryWithAmounts();

    @Query("SELECT EXISTS(SELECT * FROM character WHERE character_id = :id)")
    boolean characterExists(long id);

    @Query("Select * FROM dicemacro")
    LiveData<List<DiceRollMacro>> getAllMacros();

    @Query("SELECT * FROM  diceroll ORDER BY diceroll_id DESC")
    LiveData<List<DiceRoll>> getDiceRolls();

    @Query("SELECT * FROM item")
    LiveData<List<Item>> getAllItems();

    @Query("SELECT * FROM character")
    LiveData<List<MyCharacter>> getAllCharacters();

    @Query("SELECT name FROM character WHERE character_id = :id")
    String getCharacterName(long id);

    @Query("UPDATE inventory SET amount = :amount WHERE inventory_id = :id")
    void updateInventoryAmount(long id, int amount);

    @Query("UPDATE item SET weight = :weight WHERE item_id = :id")
    void updateItemWeight(long id, int weight);

    @Query("UPDATE item SET name = :name WHERE item_id = :id")
    void updateItemName(long id, String name);

    @Query("UPDATE character SET name = :name WHERE character_id = :id")
    void updateCharacterName(long id, String name);

    @Query("UPDATE item SET name = :name, weight = :weight WHERE item_id = :id")
    void updateItem(long id, String name, int weight);


    @Query("DELETE FROM inventory WHERE inventory.inventory_id = :id")
    void deleteInventory(long id);
    @Query("DELETE FROM item WHERE item_id = :id")
    void deleteItem(long id);
    @Query("DELETE FROM character WHERE character_id = :id")
    void deleteCharacter(long id);

    @Query("DELETE FROM dicemacro WHERE id = :id")
    void deleteDie(long id);

    @Query("DELETE FROM inventory ")
     void nukeInventory();
    @Query("DELETE FROM item ")
     void nukeItem();
    @Query("DELETE FROM character ")
     void nukeCharacter();
    @Query("DELETE FROM diceroll")
    void nukeDiceLog();


}
