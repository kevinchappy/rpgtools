package com.example.playertool5e.UI.Items;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.playertool5e.R;
import com.example.playertool5e.Database.Inventory;
import com.example.playertool5e.Database.Item;
import com.example.playertool5e.databinding.FragmentDashboardBinding;

import java.util.ArrayList;

/**
 * Fragment class that displays a list of items and allows for them to be selected and added to the current character's inventory.
 */
public class ItemFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private ItemViewModel itemViewModel;
    private RecyclerView recyclerView;
    private ItemsArrayAdapter adapter;

    /**
     * Inflates the layout, sets up the RecyclerView and ViewModel.
     * Sets up observers for ViewModel.
     */
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);

        binding.itemsToolbar.textView2.setText("Items");
        binding.itemsToolbar.inventoryToolbarButton.setImageResource(R.drawable.return_svgrepo_com);

        recyclerView = binding.itemListRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        itemViewModel = new ViewModelProvider(this).get(ItemViewModel.class);
        adapter = new ItemsArrayAdapter(this, this.getContext());

        itemViewModel.getItemList().observe(getViewLifecycleOwner(), items -> {
            adapter.setData(items);

            recyclerView.setAdapter(adapter);

        });

        setOnClickListeners();

        binding.itemsToolbar.inventoryToolbarButton.setImageResource(R.drawable.return_svgrepo_com);
        return binding.getRoot();
    }

    /**
     * Sets onClickListeners for UI elements in the fragment.
     * Handles navigation and dialogue building.
     */
    private void setOnClickListeners() {
        binding.itemsToolbar.inventoryToolbarButton.setOnClickListener(v -> {
            com.example.playertool5e.UI.Items.ItemFragmentDirections.ActionItemsToInv action = com.example.playertool5e.UI.Items.ItemFragmentDirections.actionItemsToInv();
            action.setItems(null);
            NavHostFragment.findNavController(ItemFragment.this).navigate(action);
        });

        binding.itemListNewButton.setOnClickListener(v -> {
            ItemDialog dialog = new ItemDialog(this.getContext(), this);
            dialog.build();
        });

        binding.cancelButton.setOnClickListener(v -> {
            com.example.playertool5e.UI.Items.ItemFragmentDirections.ActionItemsToInv action = com.example.playertool5e.UI.Items.ItemFragmentDirections.actionItemsToInv();
            action.setItems(null);
            NavHostFragment.findNavController(ItemFragment.this).navigate(action);
        });
        binding.confirmButton.setOnClickListener(v -> {

            adapter.addToInventory();
            com.example.playertool5e.UI.Items.ItemFragmentDirections.ActionItemsToInv action = com.example.playertool5e.UI.Items.ItemFragmentDirections.actionItemsToInv();
            action.setItems(null);
            NavHostFragment.findNavController(ItemFragment.this).navigate(action);
        });
    }


    /**
     * Edits an existing item in the database.
     *
     * @param id     The id of the item to be edited
     * @param name   The new name of the edited item
     * @param weight The new Weight of the edited item
     */
    public void editItem(long id, String name, int weight) {
        itemViewModel.editItem(id, name, weight);
    }

    /**
     * Inserts a new item into the database.
     *
     * @param item The item to be inserted
     */
    public void insertNewItem(Item item) {
        itemViewModel.insertNewItem(item);
    }

    /**
     * Deletes an item from the database.
     *
     * @param id The id of the item to be deleted
     */
    public void deleteItem(long id) {
        itemViewModel.deleteItem(id);
    }

    /**
     * Checks if a character exists in database.
     *
     * @param id The id of character to be checked
     * @return 'true' if character exists, otherwise false
     */
    public boolean characterExists(long id) {
        return itemViewModel.characterExists(id);
    }

    /**
     * Adds a list of Inventory items into the database.
     *
     * @param toAdd the list of Inventory items
     */
    public void addItemsToCurrentInventory(ArrayList<Inventory> toAdd) {
        itemViewModel.addItemsToCurrentInventory(toAdd);
    }
}