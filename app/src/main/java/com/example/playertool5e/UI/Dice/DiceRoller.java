package com.example.playertool5e.UI.Dice;

import android.app.AlertDialog;
import android.content.Context;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.playertool5e.Database.DiceRoll;

import java.math.BigInteger;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Class that handles rolling dice, displaying results and adding the results to the database.
 */
public class DiceRoller {
    private final Context context;

    /**
     * Instantiates new DiceRoller.
     *
     * @param context The context to show result dialogs in.
     */
    public DiceRoller(Context context) {
        this.context = context;
    }

    /**
     * Calculates the results of a dice roll event.
     * Gets the roll modifiers from the data in the view model.
     * If threshold is above 0 it will calculate amount of dice that rolled above the threshold,
     * otherwise it calculates the sum of all the rolls.
     * Calls method to show dice result dialog window and adds the result into the database.
     *
     * @param dieSize    The amount of sides on the dice to be rolled
     * @param diceAmount The amount of dice to be rolled
     * @param bonus      The bonus to add to the result of the dice roll
     * @param threshold  The target number for dice to roll equal to or over
     */
    public DiceRoll rollDice(long dieSize, int diceAmount, int bonus, int threshold) {
        StringBuilder individualDiceBuilder = new StringBuilder();
        BigInteger result = new BigInteger("0");

        if (threshold > 0) {
            for (int i = 0; i < diceAmount; i++) {
                long roll = ThreadLocalRandom.current().nextLong(dieSize) + 1 + bonus;
                if (roll >= threshold) {
                    result = result.add(BigInteger.valueOf(1));
                }
                individualDiceBuilder.append(roll).append(", ");
            }
        } else {
            for (int i = 0; i < diceAmount; i++) {
                long roll = ThreadLocalRandom.current().nextLong(dieSize) + 1;
                result = result.add(BigInteger.valueOf(roll));
                individualDiceBuilder.append(roll).append(", ");
            }
            result = result.add(BigInteger.valueOf(bonus));
        }

        if (individualDiceBuilder.length() > 0) {
            individualDiceBuilder.setLength(individualDiceBuilder.length() - 2);
        }

        StringBuilder formulaBuilder = new StringBuilder();
        formulaBuilder.append(diceAmount).append("d").append(dieSize);

        if (bonus > 0) {
            formulaBuilder.append("+");
            formulaBuilder.append(bonus);
        } else if (bonus < 0) {
            formulaBuilder.append(bonus);
        }

        if (threshold > 0) {
            formulaBuilder.append("t");
            formulaBuilder.append(threshold);
        }

        buildResultDialog(result, formulaBuilder.toString(), individualDiceBuilder.toString());
        return new DiceRoll(formulaBuilder.toString(), result.toString(), individualDiceBuilder.toString());
    }

    /**
     * Builds and configures the dialogue window for a dice roll result.
     * Inserts the roll into the database.
     *
     * @param result         The result of the dice roll
     * @param formula        The formula of the dice roll
     * @param individualDice the results of each individual dice roll
     */
    private void buildResultDialog(BigInteger result, String formula, String individualDice) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        TextView textView1 = new TextView(context);
        textView1.setText(formula);
        textView1.setGravity(Gravity.CENTER);
        textView1.setTextSize(26);

        TextView textView2 = new TextView(context);
        textView2.setText(result.toString());
        textView2.setGravity(Gravity.CENTER);
        textView2.setTextSize(60);

        TextView textView3 = new TextView(context);
        textView3.setText(individualDice);
        textView3.setGravity(Gravity.CENTER);
        textView3.setTextSize(18);

        linearLayout.addView(textView1);
        linearLayout.addView(textView2);
        linearLayout.addView(textView3);
        builder.setView(linearLayout);

        builder.show();
    }
}
