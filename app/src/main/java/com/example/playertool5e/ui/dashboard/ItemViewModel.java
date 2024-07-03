package com.example.playertool5e.ui.dashboard;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.playertool5e.MainActivity;
import com.example.playertool5e.database.Inventory;
import com.example.playertool5e.database.Item;
import com.example.playertool5e.database.MyCharacter;
import com.example.playertool5e.database.MyDataStore;
import com.example.playertool5e.database.MyDatabase;

import java.util.List;

public class ItemViewModel extends AndroidViewModel {
    private LiveData<List<Item>> mText;
    private MyDatabase db;

    //private final MutableLiveData<String> mText;

    public ItemViewModel(@NonNull Application application) {
        super(application);

        db = MyDatabase.getInstance(application.getApplicationContext());
        mText = db.allDao().getAllItems();
    }



    public LiveData<List<Item>> getItemList() {
        return mText;
    }

    public void deleteItem(long id){
        MainActivity.executor.execute(() ->
                db.allDao().deleteItem(id));
    }

    public void addItemToCurrentInventory(List<Inventory> toAdd){
        MainActivity.executor.execute(() ->
        db.allDao().insertMultipleInventories(toAdd));
    }

    public void insertNewItem(Item item){
        MainActivity.executor.execute(() ->
        db.allDao().insertItem(item));
    }
    public void editItem(long id, String name, int weight){
        MainActivity.executor.execute(() ->
                db.allDao().updateItem(id,name,weight));
    }

    public boolean characterExists(long id){
        return db.allDao().characterExists(id);
    }


}