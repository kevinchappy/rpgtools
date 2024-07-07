package com.example.playertool5e.ui.dashboard;

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
import com.example.playertool5e.database.Inventory;
import com.example.playertool5e.database.Item;
import com.example.playertool5e.database.MyDataStore;
import com.example.playertool5e.database.MyDatabase;

import java.util.ArrayList;
import java.util.List;

public class ItemsArrayAdapter extends RecyclerView.Adapter<ItemsArrayAdapter.ItemViewHolder> {


    private List<Item> items;
    private final Context context;
    private final ItemViewModel viewModel;

    private final ArrayList<Long> selected;


    public ItemsArrayAdapter(ItemViewModel viewModel, Context context) {
        this.viewModel = viewModel;
        this.context = context;
        this.selected = new ArrayList<>();
    }

    public void setData(List<Item> newList) {
        this.items = newList;
        Log.d("itemviewholder", "count : " + items.size());
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_item, parent, false);


        return new ItemViewHolder(view);
    }


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
            new ItemDialog(context, viewModel, current.getName(), current.getWeight(), current.getId());
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

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private final ImageButton deleteButton;
        private final Button editButton;
        private final Button selectButton;
        private final TextView weightView;
        private final TextView nameView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            this.weightView = itemView.findViewById(R.id.item_weight_textview);
            this.nameView = itemView.findViewById(R.id.item_name_textview);
            this.deleteButton = itemView.findViewById(R.id.delete_item_button);
            this.editButton = itemView.findViewById(R.id.edit_item_button);
            this.selectButton = itemView.findViewById(R.id.item_select_button);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

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

    @Override
    public int getItemCount() {
        return items.size();
    }


    public void addToInventory() {
        long currentUser = MyDataStore.readValue(MyDataStore.CURRENT_CHARACTER_KEY);
        Log.d("addtoinventory", "addToInventory: " + currentUser);
        if (viewModel.characterExists(currentUser)) {
            ArrayList<Inventory> toAdd = new ArrayList<>();
            for (long currentItem : selected) {
                toAdd.add(new Inventory(currentUser, currentItem, 1));
            }
            viewModel.addItemToCurrentInventory(toAdd);
        } else {
            Toast.makeText(context, "Please select a current user in the top right",
                    Toast.LENGTH_SHORT).show();
        }
        selected.clear();
    }


}
