package com.example.playertool5e.ui.inventory;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.example.playertool5e.R;
import com.example.playertool5e.database.ItemAmount;

import java.util.List;

public class InventoryArrayAdapter extends RecyclerView.Adapter<InventoryArrayAdapter.InventoryViewHolder> {


    private List<ItemAmount> itemAmounts;
    private final Context context;

    private final InventoryViewModel viewModel;



    public InventoryArrayAdapter(InventoryViewModel viewModel, Context context){
        this.viewModel = viewModel;
        this.context = context;
    }

    public void setData(List<ItemAmount> newList){
        this.itemAmounts = newList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public InventoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inventory_item, parent, false);

        return new InventoryViewHolder(view);
    }



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
                viewModel.updateInventoryAmount(current.id,i);
            }
        });

        holder.minusButton.setOnClickListener(v -> {
            if(current.amount > 0){
                int i = current.amount - 1;
                viewModel.updateInventoryAmount(current.id, i);
            }
        });
    }

    private void deleteInventoryItem(ItemAmount current) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        //builder.setTitle("Delete");
        builder.setMessage("Are you sure you want to delete " + current.name + " from your inventory?");
        builder.setPositiveButton("Confirm",
                (dialog, which) -> {
                    viewModel.removeInventoryAmount(current.id);
                });
        builder.setNegativeButton("Cancel", (dialog, which) -> {
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void updateAmount(@NonNull InventoryViewHolder holder, View v, ItemAmount current) {
        int i = 0;
        String str = holder.editText.getText().toString();
        if(!str.isEmpty()){
            i = Integer.parseInt(str);
        }
        if (i > 9999){
            i = 9999;
        }
        viewModel.updateInventoryAmount(current.id, i);
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    @Override
    public int getItemCount() {

        return itemAmounts.size();

    }

    public class InventoryViewHolder extends RecyclerView.ViewHolder{
        private final TextView nameView;
        //private final TextView countView;
        private final TextView weightView;
        private final Button button;
        private final ImageButton plusButton;
        private final ImageButton minusButton;
        private final EditText editText;
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
