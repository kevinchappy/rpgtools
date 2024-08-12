package com.example.playertool5e.UI.DiceLog;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.playertool5e.MainActivity;
import com.example.playertool5e.Database.DiceRoll;
import com.example.playertool5e.Database.MyDatabase;

import java.util.List;

/**
 * View model class that contains the dice roll history log and provides functionality to clear the log.
 */
public class LogViewModel extends AndroidViewModel {
    private final MyDatabase db;
    private final LiveData<List<DiceRoll>> log;

    /**
     * Instantiates log view model. Gets instance of the database and grabs the list of all previous dice rolls.
     * @param application the global application state
     */
    public LogViewModel(@NonNull Application application) {
        super(application);

        db = MyDatabase.getInstance(application.getApplicationContext());
        log = db.allDao().getDiceRolls();
    }

    /**
     * Gets the list of all dice rolls in the log.
     * @return LiveData list of the dice roll log.
     */
    public LiveData<List<DiceRoll>> getLog(){
        return log;
    }

    /**
     * Deletes all entries in the dice roll log from the database.
     */
    public void nukeDiceLog(){
        MainActivity.executor.execute(() -> {
            db.allDao().nukeDiceLog();
        });
    }
}
