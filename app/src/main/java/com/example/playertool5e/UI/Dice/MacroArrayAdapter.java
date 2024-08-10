package com.example.playertool5e.UI.Dice;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.playertool5e.R;
import com.example.playertool5e.Database.DiceRollMacro;

import java.util.List;

/**
 * Recyclerview adapter for the list of dice roll macros. Handles item data and events.
 */
public class MacroArrayAdapter extends RecyclerView.Adapter<MacroArrayAdapter.MacroViewHolder> {


    private List<DiceRollMacro> macros;
    private final Context context;
    private final DiceFragment fragment;


    /**
     * Instantiates new MacroArrayAdapter.
     *
     * @param context Context the MacroArrayAdapter is in.
     * @param fragment The fragment that contains the recyclerview.
     */
    public MacroArrayAdapter(Context context, DiceFragment fragment) {
        this.context = context;
        this.fragment = fragment;
    }

    /**
     * Sets the data of the list containing all dice roll macros. Notifies all observers that data has changed.
     *
     * @param newList the list to set as the new data
     */
    public void setData(List<DiceRollMacro> newList) {
        this.macros = newList;
        notifyDataSetChanged();

    }

    /**
     * Inflates the view for each item.
     */
    @NonNull
    @Override
    public MacroArrayAdapter.MacroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dice_item, parent, false);
        return new MacroViewHolder(view);
    }

    /**
     * Sets the ui elements to represent the selected macro's data. Sets onClickListeners for the item.
     *
     * @param holder The ViewHolder for the recyclerview item
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull MacroArrayAdapter.MacroViewHolder holder, int position) {
        DiceRollMacro current = macros.get(position);
        String str = "d" + current.dieSize;
        holder.nameView.setText(str);

        holder.itemView.setOnClickListener(v -> {
            fragment.rollDice(current.dieSize);
        });
        holder.itemView.setOnLongClickListener(v -> {
            deleteItem(current);

            return true;
        });
    }

    /**
     * Gets the amount of dice roll macros in database.
     *
     * @return the size of dice roll macros list
     */
    @Override
    public int getItemCount() {
        return macros.size();
    }


    /**
     * Class that holds ui elements for dice roll macro item.
     */
    public class MacroViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameView;

        /**
         * Instantiates MacroViewHolder. Binds ui elements to variables in holder.
         */
        public MacroViewHolder(@NonNull View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.dice_item_textview);
        }
    }


    /**
     * Builds and shows dialogue to delete specific dice roll macro from the database.
     * Deletes item on confirm, otherwise cancels operation.
     *
     * @param current the dice roll macro to be deleted
     */
    private void deleteItem(DiceRollMacro current) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setMessage("Are you sure you want to delete this die?");
        builder.setPositiveButton("Confirm",
                (dialog, which) -> {
                    fragment.deleteDice(current.id);
                });
        builder.setNegativeButton("Cancel", (dialog, which) -> {
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
