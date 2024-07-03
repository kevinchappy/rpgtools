package com.example.playertool5e.ui.home;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.playertool5e.R;
import com.example.playertool5e.database.DiceRollMacro;
import com.example.playertool5e.database.Item;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class MacroArrayAdapter extends RecyclerView.Adapter<MacroArrayAdapter.MacroViewHolder> {


    private List<DiceRollMacro> macros;
    private final Context context;
    private final DiceViewModel viewModel;
    private final DiceFragment fragment;


    public MacroArrayAdapter(Context context, DiceViewModel viewModel, DiceFragment fragment){
        this.viewModel = viewModel;
        this.context = context;
        this.fragment = fragment;
    }

    public void setData(List<DiceRollMacro> newList){
        this.macros = newList;
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public MacroArrayAdapter.MacroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dice_item, parent, false);

        return new MacroViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MacroArrayAdapter.MacroViewHolder holder, int position) {
        DiceRollMacro current = macros.get(position);
        String str = "d" + current.dieSize;
        holder.nameView.setText(str);

        holder.itemView.setOnClickListener(v -> {
            fragment.rollDice(current.dieSize);
            /*long result = rollDice(current.dieSize);
            Toast.makeText(context, "Result: " + result, Toast.LENGTH_SHORT).show();*/
        });
        holder.itemView.setOnLongClickListener(v -> {
            deleteItem(current);

            return true;
        });

    }

    @Override
    public int getItemCount() {
        return macros.size();
    }


    public class MacroViewHolder extends RecyclerView.ViewHolder{

        private final TextView nameView;
        public MacroViewHolder(@NonNull View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.dice_item_textview);
        }
    }





    private void deleteItem(DiceRollMacro current) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setMessage("Are you sure you want to delete this die?");
        builder.setPositiveButton("Confirm",
                (dialog, which) -> {
                    viewModel.deleteDice(current.id);
                });
        builder.setNegativeButton("Cancel", (dialog, which) -> {
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
