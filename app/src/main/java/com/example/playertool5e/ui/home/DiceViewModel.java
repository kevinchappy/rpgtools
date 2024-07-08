package com.example.playertool5e.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.playertool5e.MainActivity;
import com.example.playertool5e.database.DiceRoll;
import com.example.playertool5e.database.DiceRollMacro;
import com.example.playertool5e.database.MyDatabase;

import java.util.List;

public class DiceViewModel extends AndroidViewModel {

    private LiveData<List<DiceRollMacro>> macros;
    private LiveData<List<DiceRoll>> log;
    private MyDatabase db;
    private final MutableLiveData<Integer> diceAmount;
    private final MutableLiveData<Integer> rollBonus;
    private final MutableLiveData<Integer> threshold;


    public DiceViewModel(@NonNull Application application) {
        super(application);

        db = MyDatabase.getInstance(application.getApplicationContext());

            macros = db.allDao().getAllMacros();
            log = db.allDao().getDiceRolls();


        diceAmount = new MutableLiveData<>();
        diceAmount.setValue(1);

        rollBonus = new MutableLiveData<>();
        rollBonus.setValue(0);

        threshold = new MutableLiveData<>();
        threshold.setValue(0);


    }

    public LiveData<List<DiceRollMacro>> getMacros() {
        return macros;
    }

    public LiveData<List<DiceRoll>> getLog(){
        return log;
    }


    public void insertDice(DiceRollMacro diceRollMacro) {
        MainActivity.executor.execute(() -> {
            db.allDao().insertDice(diceRollMacro);
        });
    }

    public void insertDiceRollResult(DiceRoll diceRoll) {
        MainActivity.executor.execute(() -> {
            db.allDao().insertDiceROll(diceRoll);
        });
    }

    public void deleteDice(long id) {
        MainActivity.executor.execute(() -> {
            db.allDao().deleteDie(id);
        });
    }

    public void nukeDiceLog(){
        MainActivity.executor.execute(() -> {
            db.allDao().nukeDiceLog();
        });


    }

    public MutableLiveData<Integer> getdiceAmount() {
        return diceAmount;
    }

    public MutableLiveData<Integer> getRollBonus() {
        return rollBonus;
    }

    public MutableLiveData<Integer> getThreshold() {
        return threshold;
    }

    public void incrementBonus() {
        rollBonus.setValue(rollBonus.getValue().intValue() + 1);
    }

    public void decrementBonus() {
        rollBonus.setValue(rollBonus.getValue().intValue() - 1);
    }

    public void incrementAmount() {
        diceAmount.setValue(diceAmount.getValue().intValue() + 1);
    }

    public void decrementAmount() {
        if (diceAmount.getValue() > 1) {
            diceAmount.setValue(diceAmount.getValue().intValue() - 1);
        }
    }

    public void incrementThreshold() {
        threshold.setValue(threshold.getValue().intValue() + 1);
    }

    public void decrementThreshold() {
        if (threshold.getValue() > 0) {
            threshold.setValue(threshold.getValue().intValue() - 1);
        }
    }

    public void setDiceAmount(Integer integer) {
        diceAmount.setValue(integer);
    }

    public void setRollBonus(Integer integer) {
        rollBonus.setValue(integer);
    }

    public void setThreshold(Integer integer) {
        threshold.setValue(integer);
    }
}