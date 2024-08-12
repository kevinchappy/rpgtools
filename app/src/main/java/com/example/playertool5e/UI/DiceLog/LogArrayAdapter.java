package com.example.playertool5e.UI.DiceLog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.playertool5e.R;
import com.example.playertool5e.Database.DiceRoll;

import java.util.List;

/**
 * Recycler view adapter that handles the items in the dice roll log.
 */
public class LogArrayAdapter extends RecyclerView.Adapter<LogArrayAdapter.LogViewHolder>{

    private List<DiceRoll> log;

    /**
     * Inflates the view for the dice roll log item.
     */
    @NonNull
    @Override
    public LogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dice_log_item,parent,false);
        return new LogViewHolder(view);
    }

    /**
     * Sets the ui elements to represent the dice roll at the specified position.
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull LogViewHolder holder, int position) {
        DiceRoll current = log.get(position);
        holder.resultView.setText(current.result);
        holder.formulaView.setText(current.formula);
        holder.diceView.setText(current.individualDice);
    }

    /**
     * Gets the amount of dice roll log entries.
     *
     * @return the size of the dice roll log
     */
    @Override
    public int getItemCount() {
        return log.size();
    }

    /**
     * Sets the data of the list containing all dice rolls. Notifies all observers that data has changed.
     *
     * @param newList the list to set as the new data
     */
    public void setData(List<DiceRoll> newList){
        this.log = newList;
        notifyDataSetChanged();
    }

    /**
     * Class that holds the ui elements for log item in recyclerview.
     */
    public class LogViewHolder extends RecyclerView.ViewHolder{

        private final TextView resultView;
        private final TextView formulaView;
        private final TextView diceView;

        /**
         * Instantiates new LogViewHolder and binds ui elements to variables.
         */
        public LogViewHolder(@NonNull View view){
            super(view);
            resultView = view.findViewById(R.id.result_text_view);
            formulaView = view.findViewById(R.id.formula_text_view);
            diceView = view.findViewById(R.id.individual_dice_text_view);
        }
    }

}
