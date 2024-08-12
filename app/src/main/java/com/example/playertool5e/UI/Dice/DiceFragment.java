package com.example.playertool5e.UI.Dice;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.playertool5e.R;
import com.example.playertool5e.Database.DiceRollMacro;
import com.example.playertool5e.databinding.FragmentHomeBinding;

/**
 * Fragment class that displays a list of dice that the user can roll. Also allows for creating new
 * dice, adding modifiers to dice rolls and navigating to dice roll history log.
 */
public class DiceFragment extends Fragment {
    private FragmentHomeBinding binding;
    private MacroArrayAdapter adapter;
    private DiceViewModel diceViewModel;
    private DiceRoller diceRoller;

    /**
     * Inflates view binding and sets up observers for the ViewModels live data.
     * Sets up recyclerview and dice roller.
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        diceViewModel = new ViewModelProvider(this).get(DiceViewModel.class);
        diceRoller = new DiceRoller(this.getContext());
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        binding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        binding.recyclerView.setHasFixedSize(true);

        binding.toolbar.inventoryToolbarButton.setImageResource(R.drawable.log_svgrepo_com);
        adapter = new MacroArrayAdapter(this.getContext(), this);

        binding.toolbar.textView2.setText("Diceroller");

        diceViewModel.getMacros().observe(getViewLifecycleOwner(), macros -> {
            adapter.setData(macros);
            binding.recyclerView.setAdapter(adapter);
        });
        diceViewModel.getRollBonus().observe(getViewLifecycleOwner(), rollBonus -> {
            if (rollBonus >= 0) {
                String str = "+" + rollBonus;
                binding.rollBonusEditText.setText(str);
            } else {
                binding.rollBonusEditText.setText(rollBonus.toString());
            }
        });

        diceViewModel.getDiceAmount().observe(getViewLifecycleOwner(), diceAmount -> {
            String str = diceAmount + "d";
            binding.dieAmountEditText.setText(str);
        });

        diceViewModel.getThreshold().observe(getViewLifecycleOwner(), threshold -> {
            String str = "t" + threshold;
            binding.thresholdTextView.setText(str);
        });

        setButtonListeners();
        return binding.getRoot();
    }

    /**
     * Sets button listeners for the buttons in the ui. Each button calls a separate method that
     * manipulates the data in the view model.
     */
    private void setButtonListeners() {
        binding.toolbar.inventoryToolbarButton.setOnClickListener(v -> {
            NavHostFragment.findNavController(DiceFragment.this).navigate(R.id.action_navigation_home_to_navigation_dice_log);
        });
        binding.rollBonusInvisibleButton.setOnClickListener(v -> {
            diceViewModel.setRollBonus(0);
        });
        binding.bonusPlusButton.setOnClickListener(v -> {
            diceViewModel.incrementBonus();
        });
        binding.bonusMinusButton.setOnClickListener(v -> {
            diceViewModel.decrementBonus();
        });
        binding.dieAmountInvisibleButton.setOnClickListener(v -> {
            diceViewModel.setDiceAmount(1);
        });
        binding.dieAmountPlusButton.setOnClickListener(v -> {
            diceViewModel.incrementAmount();
        });
        binding.dieAmountMinusButton.setOnClickListener(v -> {
            diceViewModel.decrementAmount();
        });
        binding.thresholdPlusButton.setOnClickListener(v -> {
            diceViewModel.incrementThreshold();
        });
        binding.thresholdMinusButton.setOnClickListener(v -> {
            diceViewModel.decrementThreshold();
        });
        binding.thresholdInvisibleButton.setOnClickListener(v -> {
            diceViewModel.setThreshold(0);
        });
        binding.newDiceButton.setOnClickListener(v -> {
            buildAddNewDiceDialog();
        });
    }


    /**
     * Adds modifiers to dice roll and calls method to perform the roll.
     *
     * @param dieSize The amount of sides on the dice to be rolled
     */
    public void rollDice(long dieSize) {
        int diceAmount = diceViewModel.getDiceAmount().getValue().intValue();
        int bonus = diceViewModel.getRollBonus().getValue().intValue();
        int threshold = diceViewModel.getThreshold().getValue().intValue();
        diceViewModel.insertDiceRollResult(diceRoller.rollDice(dieSize, diceAmount, bonus, threshold));
    }

    /**
     * Builds and configures a dialogue for adding a new dice. Provides an edit text to provide number of sides.
     * Checks that the die size is within the allowable range.
     * Calls method to add new die to database if valid.
     */
    private void buildAddNewDiceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        TextView textView = new TextView(getContext());
        textView.setText("Number of sides:");
        textView.setTextSize(26);

        EditText editText = new EditText(getContext());
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);

        linearLayout.addView(textView);
        linearLayout.addView(editText);
        builder.setView(linearLayout);

        builder.setPositiveButton("OK", ((dialog, which) -> {
            try {
                long input = Long.parseLong(editText.getText().toString());
                if (input < 1 || input > 1000000) {
                    Toast.makeText(getContext(), "Please input a valid number between 1 and 1000000", Toast.LENGTH_SHORT).show();
                } else {
                    diceViewModel.insertDice(new DiceRollMacro(input));
                }
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Invalid input", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }

        }));

        builder.setNegativeButton("Cancel", ((dialog, which) -> {
            dialog.cancel();
        }));
        builder.show();
    }


    /**
     * Calls dice deletion method in view model. Deletes dice with specified id.
     *
     * @param id the id of the dice to be deleted.
     */
    public void deleteDice(long id) {
        diceViewModel.deleteDice(id);
    }

    /**
     * Releases references to clear up memory when view is destroyed.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}