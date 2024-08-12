package com.example.playertool5e;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;


import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.playertool5e.Database.MyDataStore;
import com.example.playertool5e.Database.MyDatabase;
import com.example.playertool5e.databinding.ActivityMainBinding;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Entry class for rpg tools app. App provides dice roller and character based inventory management.
 */
public class MainActivity extends AppCompatActivity {
    private static final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    public static final Executor executor = Executors.newFixedThreadPool(NUMBER_OF_CORES);
    private ActivityMainBinding binding;

    /**
     * Handles inflating layout, creating database and datastore, and setting up app bar navigation.
     * Also disables night mode since it has not been implemented.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MyDatabase.getInstance(this);
        MyDataStore.getInstance(this);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_inventory).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);

        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    /**
     * Intercepts touch events to close the on screen keyboard if the touched element is not an EditText
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }
}