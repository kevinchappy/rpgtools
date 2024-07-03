package com.example.playertool5e.ui.inventory;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.example.playertool5e.MainActivity;
import com.example.playertool5e.database.MyCharacter;
import com.example.playertool5e.database.ItemAmount;
import com.example.playertool5e.database.MyDataStore;
import com.example.playertool5e.database.MyDatabase;


import java.util.List;

public class InventoryViewModel extends AndroidViewModel {

    private LiveData<List<ItemAmount>> itemAmounts;
    private LiveData<List<MyCharacter>> characters;
    private MyDatabase db;






    public InventoryViewModel(@NonNull Application application) {
        super(application);


        db = MyDatabase.getInstance(application.getApplicationContext());


        MyDataStore.getInstance(application.getApplicationContext());

        itemAmounts = db.allDao().getInventoryWithAmounts();
        characters = db.allDao().getAllCharacters();


    }



    public LiveData<List<ItemAmount>> getItemsAndAmounts(){
        return itemAmounts;
    }
    public LiveData<List<MyCharacter>> getCharacters(){return characters;}

    public void removeInventoryAmount(long id){
        MainActivity.executor.execute(() ->
                db.allDao().deleteInventory(id)
        );
    }


    public void updateInventoryAmount(long id, int amount){
        MainActivity.executor.execute(() ->
                db.allDao().updateInventoryAmount(id, amount)
        );
    }

    public void insertNewCharacter(MyCharacter myCharacter) {
        MainActivity.executor.execute(() ->
        db.allDao().insertCharacter(myCharacter));

    }

    public void editCharacter(long id, String name) {
        MainActivity.executor.execute(() ->
        db.allDao().updateCharacterName(id, name));
    }

    public String characterName(long id){
        return db.allDao().getCharacterName(id);
    }

    public boolean characterExists(long id){
        return db.allDao().characterExists(id);
    }

    public void deleteCharacter(long id){
        db.allDao().deleteCharacter(id);
    }

}