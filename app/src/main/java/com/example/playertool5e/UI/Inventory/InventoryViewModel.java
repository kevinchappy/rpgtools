package com.example.playertool5e.UI.Inventory;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.playertool5e.MainActivity;
import com.example.playertool5e.Database.MyCharacter;
import com.example.playertool5e.Database.ItemAmount;
import com.example.playertool5e.Database.MyDatabase;


import java.util.List;

/**
 * View model class that handles the data regarding character inventories and holds methods for database calls.
 */
public class InventoryViewModel extends AndroidViewModel {
    private final LiveData<List<ItemAmount>> itemAmounts;
    private final LiveData<List<MyCharacter>> characters;
    private final MyDatabase db;

    /**
     * Instantiates new InventoryViewModel
     * Gets database instance and retrieves the livedata for characters and inventory items.
     */
    public InventoryViewModel(@NonNull Application application) {
        super(application);

        db = MyDatabase.getInstance(application.getApplicationContext());
        itemAmounts = db.allDao().getInventoryWithAmounts();
        characters = db.allDao().getAllCharacters();
    }


    /**
     * Gets list of all character inventory items and amounts.
     *
     * @return LiveData list of all itemAmounts in database
     */
    public LiveData<List<ItemAmount>> getItemsAndAmounts() {
        return itemAmounts;
    }

    /**
     * Gets list of all characters.
     *
     * @return LiveData list of all characters in database.
     */
    public LiveData<List<MyCharacter>> getCharacters() {
        return characters;
    }

    /**
     * Deletes an inventory entry by id.
     *
     * @param id The id of inventory to be deleted
     */
    public void removeInventoryAmount(long id) {
        MainActivity.executor.execute(() ->
                db.allDao().deleteInventory(id)
        );
    }

    /**
     * Updates the amount of an item that a character has.
     *
     * @param id     the id of the inventory entry
     * @param amount The new amount of the item
     */
    public void updateInventoryAmount(long id, int amount) {
        MainActivity.executor.execute(() ->
                db.allDao().updateInventoryAmount(id, amount)
        );
    }

    /**
     * Inserts new character into database.
     *
     * @param character The character to be inserted
     */
    public void insertNewCharacter(MyCharacter character) {
        MainActivity.executor.execute(() ->
                db.allDao().insertCharacter(character));
    }

    /**
     * Edits name of specified character in database.
     *
     * @param id   the id of the character to be edited
     * @param name The new name for the character
     */
    public void editCharacter(long id, String name) {
        MainActivity.executor.execute(() ->
                db.allDao().updateCharacterName(id, name));
    }

    /**
     * Gets the character name of specified character by id.
     *
     * @param id The id of the character to be edited
     * @return
     */
    public String getCharacterName(long id) {
        return db.allDao().getCharacterName(id);
    }

    /**
     * Checks if a character exists in the database.
     *
     * @param id the id of the character to check if exists
     * @return 'true' if the character exists, otherwise 'false'
     */
    public boolean characterExists(long id) {
        return db.allDao().characterExists(id);
    }

    /**
     * Deletes specified character from database by id.
     *
     * @param id The id of the character to be deleted.
     */
    public void deleteCharacter(long id) {
        db.allDao().deleteCharacter(id);
    }

}