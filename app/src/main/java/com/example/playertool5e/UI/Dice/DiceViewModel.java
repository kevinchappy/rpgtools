package com.example.playertool5e.UI.Dice;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.playertool5e.MainActivity;
import com.example.playertool5e.Database.DiceRoll;
import com.example.playertool5e.Database.DiceRollMacro;
import com.example.playertool5e.Database.MyDatabase;

import java.util.List;

/**
 * View model class that contains the data regarding dice roll modifiers and methods for database calls.
 */
public class DiceViewModel extends AndroidViewModel {
    private final MyDatabase db;
    private final LiveData<List<DiceRollMacro>> macros;
    private final MutableLiveData<Integer> diceAmount;
    private final MutableLiveData<Integer> rollBonus;
    private final MutableLiveData<Integer> threshold;

    /**
     * Instantiates new DiceViewModel.
     * Gets livedata for dice roll macros and modifiers.
     */
    public DiceViewModel(@NonNull Application application) {
        super(application);

        db = MyDatabase.getInstance(application.getApplicationContext());

        macros = db.allDao().getAllMacros();

        diceAmount = new MutableLiveData<>();
        diceAmount.setValue(1);

        rollBonus = new MutableLiveData<>();
        rollBonus.setValue(0);

        threshold = new MutableLiveData<>();
        threshold.setValue(0);
    }

    /**
     * Gets live data list of dice roll macros.
     *
     * @return LiveData list of all DiceRollMacros
     */
    public LiveData<List<DiceRollMacro>> getMacros() {
        return macros;
    }

    /**
     * Inserts new dice roll macro into database.
     *
     * @param diceRollMacro the DiceRollMacro to insert
     */
    public void insertDice(DiceRollMacro diceRollMacro) {
        MainActivity.executor.execute(() -> {
            db.allDao().insertDice(diceRollMacro);
        });
    }

    /**
     * Adds a dice roll result to database.
     *
     * @param diceRoll the dice roll result to insert
     */
    public void insertDiceRollResult(DiceRoll diceRoll) {
        MainActivity.executor.execute(() -> {
            db.allDao().insertDiceRoll(diceRoll);
        });
    }

    /**
     * Deletes a die from database by id.
     *
     * @param id id of die
     */
    public void deleteDice(long id) {
        MainActivity.executor.execute(() -> {
            db.allDao().deleteDie(id);
        });
    }


    /**
     * Gets amount of dice to roll.
     *
     * @return amount of dice to roll
     */
    public MutableLiveData<Integer> getDiceAmount() {
        return diceAmount;
    }

    /**
     * Gets bonus to add to dice rolls.
     *
     * @return bonus to add to dice rolls
     */
    public MutableLiveData<Integer> getRollBonus() {
        return rollBonus;
    }

    /**
     * Gets threshold number for dice rolls.
     *
     * @return the number each roll has to get above
     */
    public MutableLiveData<Integer> getThreshold() {
        return threshold;
    }

    /**
     * Increases bonus by 1.
     */
    public void incrementBonus() {
        rollBonus.setValue(rollBonus.getValue().intValue() + 1);
    }

    /**
     * Decreases bonus by 1.
     */
    public void decrementBonus() {
        rollBonus.setValue(rollBonus.getValue().intValue() - 1);
    }

    /**
     * Increases amount of dice by 1.
     */
    public void incrementAmount() {
        diceAmount.setValue(diceAmount.getValue().intValue() + 1);
    }

    /**
     * Decreases amount of dice by 1.
     */
    public void decrementAmount() {
        if (diceAmount.getValue() > 1) {
            diceAmount.setValue(diceAmount.getValue().intValue() - 1);
        }
    }

    /**
     * Increases threshold by 1.
     */
    public void incrementThreshold() {
        threshold.setValue(threshold.getValue().intValue() + 1);
    }

    /**
     * Decreases threshold by 1.
     */
    public void decrementThreshold() {
        if (threshold.getValue() > 0) {
            threshold.setValue(threshold.getValue().intValue() - 1);
        }
    }

    /**
     * Sets the amount of dice to roll to specific number.
     *
     * @param integer number of dice to roll
     */
    public void setDiceAmount(Integer integer) {
        diceAmount.setValue(integer);
    }

    /**
     * Sets the bonus to add to dice rolls.
     *
     * @param integer number to att to rolls
     */
    public void setRollBonus(Integer integer) {
        rollBonus.setValue(integer);
    }

    /**
     * Sets the threshold for dice rolls.
     *
     * @param integer number each roll has to get above
     */
    public void setThreshold(Integer integer) {
        threshold.setValue(integer);
    }
}