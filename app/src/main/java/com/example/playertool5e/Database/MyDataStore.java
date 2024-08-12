package com.example.playertool5e.Database;

import android.content.Context;
import android.util.Log;

import androidx.datastore.preferences.core.MutablePreferences;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.PreferencesKeys;
import androidx.datastore.preferences.rxjava2.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava2.RxDataStore;

import io.reactivex.Single;

/**
 * Singleton preferences datastore that stores the current selected character for the inventory
 */
public final class MyDataStore {
    private static RxDataStore<Preferences> INSTANCE;
    public static final String CURRENT_CHARACTER_KEY = "current_character";

    /**
     * Gets the instance of the datastore or creates a new one if it is null.
     *
     * @param context the application context
     * @return the instance of the datastore
     */
    public synchronized static RxDataStore<Preferences> getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new RxPreferenceDataStoreBuilder(context, "Character").build();
        }
        return INSTANCE;
    }

    /**
     * Saves a new value to the datastore.
     *
     * @param Key   the key for the value to be stored
     * @param value the value to be stored
     */
    public static void saveValue(String Key, Long value) {
        if (INSTANCE != null) {
            Preferences.Key<Long> PREF_KEY = PreferencesKeys.longKey(Key);
            Single<Preferences> updateResult = INSTANCE.updateDataAsync(prefsIn -> {
                MutablePreferences mutablePreferences = prefsIn.toMutablePreferences();
                mutablePreferences.set(PREF_KEY, value);
                Log.d("datastore", "saveValue: " + value);
                return Single.just(mutablePreferences);
            });
        } else {
            Log.d("datastore", "saveValue: null");
        }
    }

    /**
     * Gets the current value stored in the datastore.
     *
     * @param Key the key for what value to read from the datastore
     * @return
     */
    public static Long readValue(String Key) {
        if (INSTANCE != null) {
            Preferences.Key<Long> PREF_KEY = PreferencesKeys.longKey(Key);
            Single<Long> value = INSTANCE.data().firstOrError().map(prefs -> prefs.get(PREF_KEY)).onErrorReturnItem((long) -1);
            long i = value.blockingGet();
            Log.d("datastore", "readValue: " + i);
            return i;
        }
        return null;
    }
}
