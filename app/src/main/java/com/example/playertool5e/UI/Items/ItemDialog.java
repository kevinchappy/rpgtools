package com.example.playertool5e.UI.Items;

import android.app.AlertDialog;
import android.content.Context;
import android.text.InputType;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.playertool5e.Database.Item;

/**
 * Class that creates dialogues for adding new items to database and editing existing entries.
 */
public class ItemDialog {
    private final Context context;
    private final ItemFragment fragment;
    private final EditText inputName;
    private final EditText inputWeight;
    private final long id;
    private final boolean edit;


    /**
     * Constructor for instantiating dialogue window for adding new item to database.
     *
     * @param context that dialogue is shown in
     * @param fragment ItemFragment that created dialogue
     */
    public ItemDialog(Context context, ItemFragment fragment) {
        this.context = context;
        this.fragment = fragment;
        this. inputName = new EditText(context);
        this.inputWeight = new EditText(context);
        this.id = -1;
        this.edit = false;
    }

    /**
     *Constructor for instantiating dialogue window for editing existing database entry.
     *
     * @param context that dialogue is shown in
     * @param fragment ItemFragment that created dialogue
     * @param name name of item to be edited
     * @param weight weight of item to be edited
     * @param id id of item to be edited
     */
    public ItemDialog(Context context, ItemFragment fragment, String name, int weight, long id) {
        this.context = context;
        this.fragment = fragment;
        this. inputName = new EditText(context);
        inputName.setText(name);
        this.inputWeight = new EditText(context);
        inputWeight.setText(String.valueOf(weight));
        this.id = id;
        this.edit = true;
    }

    /**
     * Builds, configures and shows a dialogue window for adding new item to database, or editing an existing one.
     */
    public void build() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        final TextView textView1 = new TextView(context);
        textView1.setText("Name:");
        textView1.setTextSize(20);
        final TextView textView2 = new TextView(context);
        textView2.setText("Weight:");
        textView2.setTextSize(20);

        inputName.setInputType(InputType.TYPE_CLASS_TEXT);
        inputWeight.setInputType(InputType.TYPE_CLASS_NUMBER);
        linearLayout.addView(textView1);
        linearLayout.addView(inputName);
        linearLayout.addView(textView2);
        linearLayout.addView(inputWeight);
        builder.setView(linearLayout);

        builder.setPositiveButton("OK", (dialog, which) -> {
            boolean weightFilled = true;
            boolean nameFilled = true;
            if (inputWeight.getText().toString().isEmpty()) {
                inputWeight.setError("Weight cannot be empty");
                weightFilled = false;

            }
            if (inputName.getText().toString().isEmpty()) {
                inputName.setError("Name cannot be empty");
                nameFilled = false;
            }
            try {
                if (weightFilled && nameFilled) {
                    int weight = Integer.parseInt(inputWeight.getText().toString());
                    String name = inputName.getText().toString();

                    if (weight > 9999) {
                        weight = 9999;
                    }
                    if (weight < 0) {
                        weight = 0;
                    }
                    if (edit) {
                        fragment.editItem(id, name, weight);
                    } else {
                        fragment.insertNewItem(new Item(name, weight));
                    }
                }
            } catch (NumberFormatException e) {
                dialog.cancel();
                Toast.makeText(context, "Please input a number between 0 and 9999", Toast.LENGTH_SHORT).show();
            }

        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }
}
