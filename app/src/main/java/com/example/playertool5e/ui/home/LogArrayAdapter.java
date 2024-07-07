package com.example.playertool5e.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.playertool5e.R;
import com.example.playertool5e.database.DiceRoll;

import java.util.List;

public class LogArrayAdapter extends RecyclerView.Adapter<LogArrayAdapter.LogViewHolder>{

    private List<DiceRoll> log;

    @NonNull
    @Override
    public LogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dice_log_item,parent,false);
        return new LogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LogViewHolder holder, int position) {
        DiceRoll current = log.get(position);
        holder.resultView.setText(current.result);
        holder.formulaView.setText(current.formula);
        holder.diceView.setText(current.individualDice);
    }

    @Override
    public int getItemCount() {
        return log.size();
    }

    public void setData(List<DiceRoll> newList){
        this.log = newList;
        notifyDataSetChanged();
    }

    public class LogViewHolder extends RecyclerView.ViewHolder{

        private final TextView resultView;
        private final TextView formulaView;
        private final TextView diceView;
        public LogViewHolder(@NonNull View view){
            super(view);
            resultView = view.findViewById(R.id.result_text_view);
            formulaView = view.findViewById(R.id.formula_text_view);
            diceView = view.findViewById(R.id.individual_dice_text_view);
        }
    }

}
