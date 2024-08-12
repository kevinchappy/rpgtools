package com.example.playertool5e.UI.Items;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.playertool5e.MainActivity;
import com.example.playertool5e.Database.Inventory;
import com.example.playertool5e.Database.Item;
import com.example.playertool5e.Database.MyDatabase;

import java.util.List;

/**
 * View model class that contains the data regarding all items in database.
 */
public class ItemViewModel extends AndroidViewModel {
    private final LiveData<List<Item>> items;
    private final MyDatabase db;

    /**
     * Instantiates new ItemViewModel.
     * Gets instance of database and retrieves the livedata list of items from database.
     */
    public ItemViewModel(@NonNull Application application) {
        super(application);

        db = MyDatabase.getInstance(application.getApplicationContext());
        items = db.allDao().getAllItems();
    }

    /**
     * Gets list of all items in database.
     *
     * @return LiveDataList of all items
     */
    public LiveData<List<Item>> getItemList() {
        return items;
    }

    /**
     * Deletes item from database by id.
     *
     * @param id id of the item to be deleted
     */
    public void deleteItem(long id) {
        MainActivity.executor.execute(() ->
                db.allDao().deleteItem(id));
    }

    /**
     * Adds list of multiple inventories to database.
     *
     * @param toAdd list of inventories to add
     */
    public void addItemsToCurrentInventory(List<Inventory> toAdd) {
        MainActivity.executor.execute(() ->
                db.allDao().insertMultipleInventories(toAdd));
    }

    /**
     * Inserts new item into the database.
     *
     * @param item item to insert
     */
    public void insertNewItem(Item item) {
        MainActivity.executor.execute(() ->
                db.allDao().insertItem(item));
    }

    /**
     * Edits specified item in database by id.
     *
     * @param id     id of item to be edited
     * @param name   New name of the edited item
     * @param weight New weight of the edited item
     */
    public void editItem(long id, String name, int weight) {
        MainActivity.executor.execute(() ->
                db.allDao().updateItem(id, name, weight));
    }

    /**
     * Checks if the specified character exists in the database by id.
     *
     * @param id id of the character to check if exists
     * @return 'true' if character exists in database, otherwise 'false'
     */
    public boolean characterExists(long id) {
        return db.allDao().characterExists(id);
    }


}