package com.example.playertool5e.UI.Inventory;

import android.app.AlertDialog;
import android.content.Context;
import android.text.InputType;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.playertool5e.Database.MyCharacter;

/**
 * Class that creates dialogues for adding new characters to database and editing existing entries.
 */
public class CharacterDialog {

    private final Context context;
    private final InventoryFragment fragment;
    private final String name;
    private final long id;
    private final boolean edit;

    /**
     * Instantiates new character dialogue window for adding a new character to the database.
     *
     * @param context the context to display the dialogue in
     * @param fragment the inventory fragment that called this
     */
    public CharacterDialog(Context context, InventoryFragment fragment) {
        this.context = context;
        this.fragment = fragment;
        this.name = "";
        this.id = -1;
        this.edit = false;
    }

    /**
     * Instantiates a new character dialogue window editing an existing character.
     *
     * @param context the context to display the dialogue in
     * @param fragment the inventory fragment that called this
     * @param name name of the character to be edited
     * @param id the id of the character to be edited.
     */
    public CharacterDialog(Context context, InventoryFragment fragment, String name, long id) {
        this.context = context;
        this.fragment = fragment;
        this.name = name;
        this.id = id;
        this.edit = true;
    }

    /**
     * Builds, configures and shows a dialogue window for adding new character to database, or editing an existing one.
     */
    public void build() {
        EditText inputName = new EditText(context);
        inputName.setText(name);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
                    fragment.editCharacter(id, newName);

                } else {
                    fragment.insertNewCharacter(new MyCharacter(newName));

                }
            }

        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        if (edit) {
            builder.setNeutralButton("Delete", (dialog, which) -> {
                fragment.deleteCharacter(id);
                fragment.resetToolbarName();
            });
        }
        builder.show();
    }
}
