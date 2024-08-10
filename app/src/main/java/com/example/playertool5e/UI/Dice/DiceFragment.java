package com.example.playertool5e.UI.Dice;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
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
import com.example.playertool5e.Database.DiceRoll;
import com.example.playertool5e.Database.DiceRollMacro;
import com.example.playertool5e.databinding.FragmentHomeBinding;

import java.math.BigInteger;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Fragment class that displays a list of dice that the user can roll. Also allows for creating new
 * dice, adding modifiers to dice rolls and navigating to dice roll history log.
 */
public class DiceFragment extends Fragment {
    private FragmentHomeBinding binding;
    private MacroArrayAdapter adapter;
    private DiceViewModel diceViewModel;

    /**
     * Inflates view binding and sets up observers for the ViewModels live data.
     * Sets up recyclerview.
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        diceViewModel = new ViewModelProvider(this).get(DiceViewModel.class);

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
     * Calculates the results of a dice roll event.
     * Gets the roll modifiers from the data in the view model.
     * If threshold is above 0 it will calculate amount of dice that rolled above the threshold,
     * otherwise it calculates the sum of all the rolls.
     *
     * @param dieSize the amount of sides on the dice to be rolled
     */
    public void rollDice(long dieSize) {

        int diceAmount = diceViewModel.getDiceAmount().getValue().intValue();
        int bonus = diceViewModel.getRollBonus().getValue().intValue();
        int threshold = diceViewModel.getThreshold().getValue().intValue();
        StringBuilder sb = new StringBuilder();
        BigInteger result = new BigInteger("0");

        if (threshold > 0) {
            for (int i = 0; i < diceAmount; i++) {
                long roll = ThreadLocalRandom.current().nextLong(dieSize) + 1 + bonus;
                if (roll >= threshold) {
                    result = result.add(BigInteger.valueOf(1));
                }
                sb.append(roll).append(", ");
            }

        } else {
            for (int i = 0; i < diceAmount; i++) {
                long roll = ThreadLocalRandom.current().nextLong(dieSize) + 1;
                result = result.add(BigInteger.valueOf(roll));
                sb.append(roll).append(", ");
            }
            result = result.add(BigInteger.valueOf(bonus));
        }
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 2);
        }

        buildResultDialog(result, bonus, diceAmount, threshold, dieSize, sb.toString());
    }

    /**
     * Builds and configures the dialogue window for a dice roll result.
     * Inserts the roll into the database.
     *
     * @param result The result of the dice roll
     * @param bonus the bonus added to the roll
     * @param diceAmount the amount of dice rolled
     * @param threshold the threshold that each dice had to roll over
     * @param dieSize the number of sides on the dice rolled
     * @param individualDice the results of each individual dice roll
     */
    private void buildResultDialog(BigInteger result, int bonus, int diceAmount, int threshold, long dieSize, String individualDice) {
        StringBuilder sb = new StringBuilder();
        sb.append(diceAmount).append("d").append(dieSize);

        if (bonus > 0) {
            sb.append("+");
            sb.append(bonus);
        } else if (bonus < 0) {
            sb.append(bonus);
        }

        if (threshold > 0) {
            sb.append("t");
            sb.append(threshold);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        TextView textView1 = new TextView(getContext());
        textView1.setText(sb.toString());
        textView1.setGravity(Gravity.CENTER);
        textView1.setTextSize(26);

        TextView textView2 = new TextView(getContext());
        textView2.setText(result.toString());
        textView2.setGravity(Gravity.CENTER);
        textView2.setTextSize(60);

        TextView textView3 = new TextView(getContext());
        textView3.setText(individualDice);
        textView3.setGravity(Gravity.CENTER);
        textView3.setTextSize(18);

        linearLayout.addView(textView1);
        linearLayout.addView(textView2);
        linearLayout.addView(textView3);
        builder.setView(linearLayout);

        builder.show();
        diceViewModel.insertDiceRollResult(new DiceRoll(sb.toString(), result.toString(), individualDice));
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