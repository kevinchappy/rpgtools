package com.example.playertool5e.database;

import android.content.Context;
import android.util.Log;

import androidx.datastore.preferences.core.MutablePreferences;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.PreferencesKeys;
import androidx.datastore.preferences.rxjava2.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava2.RxDataStore;

import io.reactivex.Single;

public final class MyDataStore {
    private static RxDataStore<Preferences> INSTANCE;
    public static final String CURRENT_CHARACTER_KEY = "current_character";
    private static final Object lock = new Object();

    private MyDataStore() {
    }

    public synchronized static RxDataStore<Preferences> getInstance(Context context) {

            if (INSTANCE == null) {
                INSTANCE = new RxPreferenceDataStoreBuilder(context, /*name=*/ "Character").build();
            }
            return INSTANCE;

    }

    public static void saveValue(String Key, Long value) {
        //synchronized (lock) {

        if (INSTANCE != null) {
            Preferences.Key<Long> PREF_KEY = PreferencesKeys.longKey(Key);
            Single<Preferences> updateResult = INSTANCE.updateDataAsync(prefsIn -> {
                MutablePreferences mutablePreferences = prefsIn.toMutablePreferences();
                mutablePreferences.set(PREF_KEY, value);
                    Log.d("datastore", "saveValue: " + value);
                    return Single.just(mutablePreferences);
                });
            }else{
                Log.d("datastore", "saveValue: null" );
            }
       // }
    }

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
