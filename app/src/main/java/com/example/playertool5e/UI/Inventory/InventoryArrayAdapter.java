package com.example.playertool5e.UI.Inventory;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.example.playertool5e.R;
import com.example.playertool5e.Database.ItemAmount;

import java.util.List;

/**
 * Recyclerview adapter for handling a list of all characters in the database. Handles data and events.
 */
public class InventoryArrayAdapter extends RecyclerView.Adapter<InventoryArrayAdapter.InventoryViewHolder> {
    private List<ItemAmount> itemAmounts;
    private final Context context;
    private final InventoryFragment fragment;

    /**
     * Initiates new InventoryArrayAdapter.
     *
     * @param fragment The fragment that contains the recyclerview
     * @param context The context of the fragment
     */
    public InventoryArrayAdapter(InventoryFragment fragment, Context context){
        this.fragment = fragment;
        this.context = context;
    }

    /**
     * Sets the data of the item amounts list. Notifies all observers that the data has changed.
     * @param newList the new list to set as the data
     */
    public void setData(List<ItemAmount> newList){
        this.itemAmounts = newList;
        notifyDataSetChanged();
    }

    /**
     * Inflates the view for each item
     */
    @NonNull
    @Override
    public InventoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inventory_item, parent, false);

        return new InventoryViewHolder(view);
    }

    /**
     * Sets ui elements to represent the data of the items and amounts. Sets listeners for the buttons
     * and edit text in the items.
     *
     * @param holder The ViewHolder which represents the contents of the item at the given position.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull InventoryViewHolder holder, int position) {
        ItemAmount current = itemAmounts.get(position);
        holder.nameView.setText(current.name);
        holder.editText.setText(String.valueOf(current.amount));
        holder.weightView.setText(String.valueOf(current.weight));

        holder.button.setOnClickListener(v -> {
            deleteInventoryItem(current);
        });

        holder.editText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                updateAmount(holder, v, current);
                Log.d("focus" , "unfocused");
            } else {
                Log.d("focus", "focused");
            }
        });

        holder.editText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE){
                updateAmount(holder, v, current);
                holder.editText.clearFocus();
                return true;
            }
            return false;
        });

        holder.plusButton.setOnClickListener(v -> {
            if(current.amount < 9999){
                int i = current.amount + 1;
                fragment.updateInventoryAmount(current.id,i);
            }
        });

        holder.minusButton.setOnClickListener(v -> {
            if(current.amount > 0){
                int i = current.amount - 1;
                fragment.updateInventoryAmount(current.id, i);
            }
        });
    }

    /**
     * Builds and shows dialogue window for deleting the specified item amount object.
     * Deletes on confirm, otherwise cancels operation.
     *
     * @param current the selected item
     */
    private void deleteInventoryItem(ItemAmount current) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setMessage("Are you sure you want to delete " + current.name + " from your inventory?");
        builder.setPositiveButton("Confirm",
                (dialog, which) -> {
                    fragment.removeInventoryAmount(current.id);
                });
        builder.setNegativeButton("Cancel", (dialog, which) -> {
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Ensures that the entered item amount is within allowable parameters.
     * Updates the inventory entries amount and closes the on screen keyboard.
     *
     * @param holder the item's holder
     * @param v the view
     * @param current the selected
     */
    private void updateAmount(@NonNull InventoryViewHolder holder, View v, ItemAmount current) {
        int i = 0;
        String str = holder.editText.getText().toString();
        if(!str.isEmpty()){
            try{
            i = Integer.parseInt(str);
                if (i > 9999){
                    i = 9999;
                }
            }catch (NumberFormatException e){
                i = 9999;
            }
        }
        fragment.updateInventoryAmount(current.id, i);
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    /**
     * Returns the amount of items in inventory.
     *
     * @return the size of item amounts list
     */
    @Override
    public int getItemCount() {
        return itemAmounts.size();
    }

    /**
     * Class that holds the ui elements Inventory items in recyclerview.
     */
    public class InventoryViewHolder extends RecyclerView.ViewHolder{
        private final TextView nameView;
        //private final TextView countView;
        private final TextView weightView;
        private final ImageButton button;
        private final ImageButton plusButton;
        private final ImageButton minusButton;
        private final EditText editText;

        /**
         * Instantiates new InventoryViewHolder.
         * Binds ui elements to variables in holder.
         */
        public InventoryViewHolder(@NonNull View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.item_name);
            weightView = itemView.findViewById(R.id.item_weight);
            button = itemView.findViewById(R.id.item_button);
            editText = itemView.findViewById(R.id.item_edit_text);
            plusButton = itemView.findViewById(R.id.plus_button);
            minusButton = itemView.findViewById(R.id.minus_button);
        }
    }
}
