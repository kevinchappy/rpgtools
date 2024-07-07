package com.example.playertool5e.ui.dashboard;

import android.app.AlertDialog;
import android.content.Context;
import android.text.InputType;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.playertool5e.database.Item;
import com.example.playertool5e.ui.dashboard.ItemViewModel;

public class ItemDialog {

    public ItemDialog(Context context, ItemViewModel itemViewModel) {
        extracted(context, itemViewModel, new EditText(context), new EditText(context), -1, false);
    }

    public ItemDialog(Context context, ItemViewModel itemViewModel, String name, int weight, long id) {
        EditText inputName = new EditText(context);
        inputName.setText(name);
        EditText inputWeight = new EditText(context);
        inputWeight.setText(String.valueOf(weight));
        extracted(context, itemViewModel, inputName, inputWeight, id, true);
    }

    private void extracted(Context context, ItemViewModel itemViewModel, EditText inputName, EditText inputWeight, long id, boolean edit) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //builder.setTitle("Title");
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
                        itemViewModel.editItem(id, name, weight);
                    } else {
                        itemViewModel.insertNewItem(new Item(name, weight));
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
