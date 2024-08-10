package com.example.playertool5e.UI.Items;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.playertool5e.R;
import com.example.playertool5e.Database.Inventory;
import com.example.playertool5e.Database.Item;
import com.example.playertool5e.Database.MyDataStore;

import java.util.ArrayList;
import java.util.List;

/**
 * Recyclerview adapter for handling list of all items in the database. Handles data and events.
 */
public class ItemsArrayAdapter extends RecyclerView.Adapter<ItemsArrayAdapter.ItemViewHolder> {
    private List<Item> items;
    private final Context context;
    private final ItemFragment viewModel;
    private final ArrayList<Long> selected;

    /**
     * Instantiates new ItemsArrayAdapter.
     *
     * @param fragment The fragment that contains the recyclerview
     * @param context The context of the fragment
     */
    public ItemsArrayAdapter(ItemFragment fragment, Context context) {
        this.viewModel = fragment;
        this.context = context;
        this.selected = new ArrayList<>();
    }

    /**
     * Sets the the data of the recyclerview with a new list of items.
     * Notifies all observer that the data has changed.
     *
     * @param newList The new list to set as the data
     */
    public void setData(List<Item> newList) {
        this.items = newList;
        notifyDataSetChanged();
    }

    /**
     * Inflates the view for each item.
     */
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_item, parent, false);
        return new ItemViewHolder(view);
    }

    /**
     * Sets ui elements to represent the data of the items. Sets listeners for the buttons.
     * Each item can be toggled to be selected and added to a characters inventory.
     *
     * @param holder The ViewHolder which represents the contents of the item at the given position.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item current = items.get(position);
        Log.d("itemviewholder", "Item.current == name: " + current.getName() + " weight: " + current.getWeight() + " item_id: " + current.getId());

        holder.weightView.setText(String.valueOf(current.weight));
        holder.nameView.setText(current.getName());

        holder.deleteButton.setOnClickListener(v -> {
            deleteItem(current);
        });
        holder.editButton.setOnClickListener(v -> {
            ItemDialog dialog = new ItemDialog(context, viewModel, current.getName(), current.getWeight(), current.getId());
            dialog.build();
        });
        if (selected.contains(current.id)) {
            holder.selectButton.setBackgroundResource(R.color.paladin_gold);
        }
        holder.selectButton.setOnClickListener(v -> {
            if (selected.contains(current.id)) {
                holder.selectButton.setBackgroundColor(Color.TRANSPARENT);
                selected.remove(current.id);
            } else {
                holder.selectButton.setBackgroundResource(R.color.paladin_gold);
                selected.add(current.id);
            }
        });
    }

    /**
     * Builds and shows dialogue window for deleting the specified item from database.
     * Deletes on confirm, otherwise cancels operation.
     *
     * @param current the selected item
     */
    private void deleteItem(Item current) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setTitle("Delete Item");
        builder.setMessage("Are you sure you want to delete " + current.name + " from the item list?\n\nThis will remove it from every inventory.");
        builder.setPositiveButton("Confirm",
                (dialog, which) -> {
                    selected.remove(current.id);
                    viewModel.deleteItem(current.id);
                });
        builder.setNegativeButton("Cancel", (dialog, which) -> {
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Returns the amount of items.
     *
     * @return the size of items list
     */
    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * Adds all selected items to the inventory of the currently selected character.
     */
    public void addToInventory() {
        long currentUser = MyDataStore.readValue(MyDataStore.CURRENT_CHARACTER_KEY);
        Log.d("addtoinventory", "addToInventory: " + currentUser);
        if (viewModel.characterExists(currentUser)) {
            ArrayList<Inventory> toAdd = new ArrayList<>();
            for (long currentItem : selected) {
                toAdd.add(new Inventory(currentUser, currentItem, 1));
            }
            viewModel.addItemsToCurrentInventory(toAdd);
        } else {
            Toast.makeText(context, "Please select a current user in the top right",
                    Toast.LENGTH_SHORT).show();
        }
        selected.clear();
    }

    /**
     * Class that holds the ui elements for log Item items in recyclerview.
     */
    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private final ImageButton deleteButton;
        private final Button editButton;
        private final Button selectButton;
        private final TextView weightView;
        private final TextView nameView;

        /**
         * Instantiates new ItemViewHolder.
         * Binds ui elements to variables in holder.
         */
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            this.weightView = itemView.findViewById(R.id.item_weight_textview);
            this.nameView = itemView.findViewById(R.id.item_name_textview);
            this.deleteButton = itemView.findViewById(R.id.delete_item_button);
            this.editButton = itemView.findViewById(R.id.edit_item_button);
            this.selectButton = itemView.findViewById(R.id.item_select_button);
        }
    }
}
