package com.example.playertool5e.ui.inventory;

import android.app.AlertDialog;
import android.content.Context;
import android.text.InputType;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.playertool5e.database.MyCharacter;

public class CharacterDialog {

    private final Context context;
    private final InventoryViewModel inventoryViewModel;
    private final InventoryFragment fragment;
    private final String name;
    private final long id;
    private final boolean edit;

    public CharacterDialog(Context context, InventoryViewModel inventoryViewModel, InventoryFragment fragment) {
        this.context = context;
        this.inventoryViewModel = inventoryViewModel;
        this.fragment = fragment;
        this.name = "";
        this.id = -1;
        this.edit = false;
    }

    public CharacterDialog(Context context, InventoryViewModel inventoryViewModel, InventoryFragment fragment, String name, long id) {
        this.context = context;
        this.inventoryViewModel = inventoryViewModel;
        this.fragment = fragment;
        this.name = name;
        this.id = id;
        this.edit = true;
    }

    public void build() {
        EditText inputName = new EditText(context);
        inputName.setText(name);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //builder.setTitle("Title");
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        final TextView textView1 = new TextView(context);
        textView1.setText("Name:");
        textView1.setTextSize(20);


        inputName.setInputType(InputType.TYPE_CLASS_TEXT);
        linearLayout.addView(textView1);
        linearLayout.addView(inputName);

        builder.setView(linearLayout);


        builder.setPositiveButton("OK", (dialog, which) -> {
            boolean nameFilled = true;

            if (inputName.getText().toString().isEmpty()) {
                inputName.setError("Name cannot be empty");
                nameFilled = false;
            }
            if (nameFilled) {
                String newName = inputName.getText().toString();
                if (edit) {
                    inventoryViewModel.editCharacter(id, newName);
                } else {
                    inventoryViewModel.insertNewCharacter(new MyCharacter(newName));
                }
            }

            //String m_Text = inputName.getText().toString();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        if (edit) {
            builder.setNeutralButton("Delete", (dialog, which) -> {
                inventoryViewModel.deleteCharacter(id);
                fragment.setName();
            });
        }
        builder.show();

    }
}
