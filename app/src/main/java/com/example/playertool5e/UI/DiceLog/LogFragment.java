package com.example.playertool5e.UI.DiceLog;

import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.playertool5e.R;
import com.example.playertool5e.databinding.FragmentLogBinding;

/**
 * Fragment class that shows list of all dice roll log items. Provides button to delete all entries from database.
 */
public class LogFragment extends Fragment {
    private FragmentLogBinding binding;
    private LogViewModel diceViewModel;
    private LogArrayAdapter adapter;


    /**
     * Creates view model, sets up recyclerview and sets up observers for view model data.
     * Sets up all ui elements for dice roll log.
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentLogBinding.inflate(inflater, container, false);
        diceViewModel = new ViewModelProvider(this).get(LogViewModel.class);


        binding.logRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.logRecyclerView.setHasFixedSize(false);
        binding.logRecyclerView.addItemDecoration(new DividerItemDecoration(binding.logRecyclerView.getContext(), DividerItemDecoration.VERTICAL));


        adapter = new LogArrayAdapter();

        diceViewModel.getLog().observe(getViewLifecycleOwner(), log -> {
            adapter.setData(log);
            binding.logRecyclerView.setAdapter(adapter);
        });

        binding.toolbar.textView2.setText("Log");
        binding.toolbar.inventoryToolbarButton.setOnClickListener(v -> {
            NavHostFragment.findNavController(LogFragment.this).navigate(R.id.action_navigation_dice_log_to_navigation_home);
        });
        binding.toolbar.inventoryToolbarButton.setImageResource(R.drawable.return_svgrepo_com);
        binding.nukeLogButton.setOnClickListener(v -> {
            nukeDiceLog();
        });
        return binding.getRoot();
    }


    /**
     * Builds and shows dialogue for user to confirm if they want to remove all dice rolls from log.
     * Deletes all dice rolls from the log on confirm, otherwise cancels operation.
     */
    private void nukeDiceLog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Clear Log");
        builder.setMessage("Are you sure you want to delete the entire log?\nThis Cannot be reversed.");
        builder.setPositiveButton("Confirm",
                (dialog, which) -> {
                    diceViewModel.nukeDiceLog();
                });
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.cancel();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
