package com.example.playertool5e.UI.Inventory;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.playertool5e.R;
import com.example.playertool5e.Database.ItemAmount;
import com.example.playertool5e.Database.MyCharacter;
import com.example.playertool5e.Database.MyDataStore;
import com.example.playertool5e.databinding.FragmentInventoryBinding;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Fragment class that shows list of all items that is in current character's inventory.
 * Allows for editing the amount of each item in the inventory.
 * Allows for changing between the inventories of multiple different characters.
 */
public class InventoryFragment extends Fragment {
    private FragmentInventoryBinding binding;
    private RecyclerView inventoryRecyclerView;
    private RecyclerView characterRecyclerView;
    private InventoryArrayAdapter inventoryAdapter;
    private CharacterArrayAdapter characterAdapter;
    private InventoryViewModel inventoryViewModel;
    private List<ItemAmount> itemAmounts;
    private DrawerLayout drawerLayout;

    /**
     * Inflates the layout, initializes view model and recycler view.
     */
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentInventoryBinding.inflate(inflater, container, false);
        drawerLayout = binding.myDrawerLayout;
        inventoryViewModel = new ViewModelProvider(this).get(InventoryViewModel.class);
        itemAmounts = new ArrayList<>();
        binding.toolbar.inventoryToolbarButton.setImageResource(R.drawable.wizard_svgrepo_com);

        setInventoryRecyclerView();
        setCharacterRecyclerView();
        setOnClickListeners();

        return binding.getRoot();
    }

    /**
     * Sets up the recyclerview for viewing, selecting and editing characters.
     */
    private void setCharacterRecyclerView() {
        characterRecyclerView = binding.characterDrawerRecyclerView;
        characterRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        characterRecyclerView.setHasFixedSize(true);
        characterRecyclerView.addItemDecoration(new DividerItemDecoration(characterRecyclerView.getContext(), DividerItemDecoration.VERTICAL));

        characterAdapter = new CharacterArrayAdapter(this.getContext(), this);
        inventoryViewModel.getCharacters().observe(getViewLifecycleOwner(), characters -> {
            characterAdapter.setData(characters);
            characterRecyclerView.setAdapter(characterAdapter);
        });
    }

    /**
     * Sets up the main recyclerview for displaying the items in the current character's inventory.
     * Displays a filtered list with only the inventory the currently selected character.
     */
    private void setInventoryRecyclerView() {
        inventoryRecyclerView = binding.inventoryRecyclerView;
        inventoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        inventoryRecyclerView.setHasFixedSize(true);
        inventoryRecyclerView.addItemDecoration(new DividerItemDecoration(inventoryRecyclerView.getContext(), DividerItemDecoration.VERTICAL));

        inventoryAdapter = new InventoryArrayAdapter(this, this.getContext());

        inventoryViewModel.getItemsAndAmounts().observe(getViewLifecycleOwner(), itemAmounts -> {
            this.itemAmounts.clear();
            this.itemAmounts = itemAmounts;
            long currentCharacter = MyDataStore.readValue(MyDataStore.CURRENT_CHARACTER_KEY);

            setInventoryRecyclerView(currentCharacter);
            if (inventoryViewModel.characterExists(currentCharacter)) {
                setName(inventoryViewModel.getCharacterName(currentCharacter));
            } else {
                resetToolbarName();
            }
        });
    }

    /**
     * Sets OnClickListeners for adding items, opening the character drawer and adding characters.
     */
    private void setOnClickListeners() {
        binding.addItems.setOnClickListener(v -> {
            NavHostFragment.findNavController(InventoryFragment.this).navigate(R.id.action_navigation_inventory_to_navigation_dashboard);
        });

        binding.toolbar.inventoryToolbarButton.setOnClickListener(v -> {
            drawerLayout.openDrawer(GravityCompat.END);
        });
        binding.newCharacterButton.setOnClickListener(v -> {
            new CharacterDialog(this.getContext(), this).build();
        });
    }

    /**
     * Filters the recyclerview list so that only items belonging to the current character are displayed.
     * Sets the weight number to equal the total weight of all displayed items.
     *
     * @param currentCharacter the id of the current character
     */
    public void setInventoryRecyclerView(long currentCharacter) {
        ArrayList<ItemAmount> filteredList = new ArrayList<>(itemAmounts);

        BigInteger total = new BigInteger("0");
        Iterator<ItemAmount> iter = filteredList.iterator();
        while (iter.hasNext()){
            ItemAmount itemAmount = iter.next();
            if (itemAmount.characterId != currentCharacter){
                iter.remove();
            }else {
                total = total.add(BigInteger.valueOf((long) itemAmount.amount * itemAmount.weight));
            }
        }

        inventoryAdapter.setData(filteredList);
        inventoryRecyclerView.setAdapter(inventoryAdapter);
        String str = "Weight: " + total;
        binding.textView.setText(str);
    }

    /**
     * Sets the text of the toolbar to reflect the currently selected character's name.
     *
     * @param name The name of the character's whose name to display in toolbar
     */
    public void setName(String name) {
        String str = "";
        if (name.charAt(name.length() - 1) == 's') {
            str = name + "' Inventory";
        } else {
            str = name + "'s Inventory";
        }
        binding.toolbar.textView2.setText(str);
    }

    /**
     * Sets the toolbar text default value
     */
    public void resetToolbarName() {
        binding.toolbar.textView2.setText("Inventory");
    }

    /**
     * Closes the character drawer
     */
    public void closeDrawer() {
        drawerLayout.closeDrawer(GravityCompat.END);
    }

    /**
     * Edits the name of a character in the database by their id.
     *
     * @param id      The id of the character to be edited
     * @param newName The new name of the specified character.
     */
    public void editCharacter(long id, String newName) {
        inventoryViewModel.editCharacter(id, newName);
    }

    /**
     * Inserts a new character into the database.
     *
     * @param character The character to insert into the database
     */
    public void insertNewCharacter(MyCharacter character) {
        inventoryViewModel.insertNewCharacter(character);
    }

    /**
     * Deletes character from the database by their id.
     *
     * @param id The id of the character to be deleted.
     */
    public void deleteCharacter(long id) {
        inventoryViewModel.deleteCharacter(id);
    }

    /**
     * Updates the amount of an item that a character has.
     *
     * @param id the id of the inventory entry
     * @param i  The new amount of the item
     */
    public void updateInventoryAmount(long id, int i) {
        inventoryViewModel.updateInventoryAmount(id, i);
    }

    /**
     * Deletes an inventory entry by id.
     *
     * @param id The id of inventory to be deleted
     */
    public void removeInventoryAmount(long id) {
        inventoryViewModel.removeInventoryAmount(id);
    }

    /**
     * Destroys references on view destruction
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}