package com.example.playertool5e.ui.items;

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
import com.example.playertool5e.databinding.FragmentDashboardBinding;

public class ItemFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private ItemViewModel itemViewModel;
    private RecyclerView recyclerView;
    private ItemsArrayAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);

        binding.itemsToolbar.textView2.setText("Items");
            binding.itemsToolbar.inventoryToolbarButton.setImageResource(R.drawable.return_svgrepo_com);

        recyclerView = binding.itemListRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));



        itemViewModel = new ViewModelProvider(this).get(ItemViewModel.class);
        adapter = new ItemsArrayAdapter(itemViewModel, this.getContext());

        itemViewModel.getItemList().observe(getViewLifecycleOwner(), items -> {
            adapter.setData(items);

            recyclerView.setAdapter(adapter);

        });

        binding.itemsToolbar.inventoryToolbarButton.setOnClickListener(v -> {

            com.example.playertool5e.ui.items.ItemFragmentDirections.ActionItemsToInv action = com.example.playertool5e.ui.items.ItemFragmentDirections.actionItemsToInv();
            action.setItems(null);
            NavHostFragment.findNavController(ItemFragment.this).navigate(action);
        });

        binding.itemListNewButton.setOnClickListener(v -> {
            new ItemDialog(this.getContext(), itemViewModel);
        });

        binding.cancelButton.setOnClickListener(v -> {
            com.example.playertool5e.ui.items.ItemFragmentDirections.ActionItemsToInv action = com.example.playertool5e.ui.items.ItemFragmentDirections.actionItemsToInv();
            action.setItems(null);
            NavHostFragment.findNavController(ItemFragment.this).navigate(action);
        });
        binding.confirmButton.setOnClickListener(v -> {

            adapter.addToInventory();
            com.example.playertool5e.ui.items.ItemFragmentDirections.ActionItemsToInv action = com.example.playertool5e.ui.items.ItemFragmentDirections.actionItemsToInv();
            action.setItems(null);
            NavHostFragment.findNavController(ItemFragment.this).navigate(action);
        });

        binding.itemsToolbar.inventoryToolbarButton.setImageResource(R.drawable.return_svgrepo_com);
        //dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return binding.getRoot();
    }
}