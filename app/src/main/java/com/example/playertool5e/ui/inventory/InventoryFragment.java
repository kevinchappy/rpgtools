package com.example.playertool5e.ui.inventory;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.playertool5e.R;
import com.example.playertool5e.database.Item;
import com.example.playertool5e.database.ItemAmount;
import com.example.playertool5e.database.MyDataStore;
import com.example.playertool5e.databinding.FragmentInventoryBinding;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class InventoryFragment extends Fragment {

    private FragmentInventoryBinding binding;
    private RecyclerView inventoryRecyclerView;
    private RecyclerView characterRecyclerView;
    private InventoryArrayAdapter inventoryAdapter;

    private CharacterArrayAdapter characterAdapter;
    private InventoryViewModel inventoryViewModel;
    private List<ItemAmount> itemAmounts;
    private DrawerLayout drawerLayout;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentInventoryBinding.inflate(inflater, container, false);

        drawerLayout = binding.myDrawerLayout;

        inventoryViewModel = new ViewModelProvider(this).get(InventoryViewModel.class);

        itemAmounts = new ArrayList<>();

        inventoryRecyclerView = binding.inventoryRecyclerView;
        inventoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        inventoryRecyclerView.setHasFixedSize(true);
        inventoryRecyclerView.addItemDecoration(new DividerItemDecoration(inventoryRecyclerView.getContext(), DividerItemDecoration.VERTICAL));

        inventoryAdapter = new InventoryArrayAdapter(inventoryViewModel, this.getContext());

        inventoryViewModel.getItemsAndAmounts().observe(getViewLifecycleOwner(), itemAmounts -> {
            this.itemAmounts.clear();
            this.itemAmounts = itemAmounts;
            long currentCharacter = MyDataStore.readValue(MyDataStore.CURRENT_CHARACTER_KEY);

            setInventoryRecyclerView(currentCharacter);
            if (inventoryViewModel.characterExists(currentCharacter)){
                setName(inventoryViewModel.characterName(currentCharacter));
            }else {
                setName();
            }
        });

        binding.toolbar.inventoryToolbarButton.setImageResource(R.drawable.wizard_svgrepo_com);

        characterRecyclerView = binding.characterDrawerRecyclerView;
        characterRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        characterRecyclerView.setHasFixedSize(true);
        characterRecyclerView.addItemDecoration(new DividerItemDecoration(characterRecyclerView.getContext(), DividerItemDecoration.VERTICAL));

        characterAdapter = new CharacterArrayAdapter(inventoryViewModel, this.getContext(), this);


        inventoryViewModel.getCharacters().observe(getViewLifecycleOwner(), characters -> {
            characterAdapter.setData(characters);
            characterRecyclerView.setAdapter(characterAdapter);
        });

        binding.testButton.setOnClickListener(v -> {
            NavHostFragment.findNavController(InventoryFragment.this).navigate(R.id.action_navigation_inventory_to_navigation_dashboard);
        });

        binding.toolbar.inventoryToolbarButton.setOnClickListener(v -> {

            drawerLayout.openDrawer(GravityCompat.END);
        });
        binding.newCharacterButton.setOnClickListener(v -> {
            new CharacterDialog(this.getContext(), inventoryViewModel,this).build();
        });

        return binding.getRoot();
    }

    public void setInventoryRecyclerView(long currentCharacter) {
        ArrayList<ItemAmount> filteredList = new ArrayList<>(itemAmounts);

        filteredList.removeIf(itemAmount -> itemAmount.characterId != currentCharacter);

        inventoryAdapter.setData(filteredList);
        inventoryRecyclerView.setAdapter(inventoryAdapter);

        BigInteger total = new BigInteger("0");
        for (ItemAmount itemAmount : filteredList) {
            if (itemAmount.amount != 0) {
                total = total.add(BigInteger.valueOf((long) itemAmount.amount * itemAmount.weight)) ;
            }
        }
        String str = "Weight: " + total;
        binding.textView.setText(str);
    }

    public void setName(String name){
        String str = "";
        if (name.charAt(name.length() - 1) == 's'){
            str = name + "' Inventory";
        }else {
            str = name + "'s Inventory";
        }
        binding.toolbar.textView2.setText(str);
    }

    public void setName(){
        binding.toolbar.textView2.setText("Inventory");
    }


    public void closeDrawer(){
        drawerLayout.closeDrawer(GravityCompat.END);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}