package com.example.playertool5e.UI.Inventory;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.playertool5e.R;
import com.example.playertool5e.Database.MyCharacter;
import com.example.playertool5e.Database.MyDataStore;

import java.util.List;

/**
 * Recyclerview adapter that handles a list of all characters in the database.
 */
public class CharacterArrayAdapter extends RecyclerView.Adapter<CharacterArrayAdapter.CharacterViewHolder> {
    private final Context context;
    private final InventoryFragment fragment;
    private List<MyCharacter> characters;

    /**
     * Instantiates new CharacterArrayAdapter.
     *
     * @param context  The context of the fragment that contains the recyclerview
     * @param fragment The inventory fragment that contains the recyclerview
     */
    public CharacterArrayAdapter(Context context, InventoryFragment fragment) {
        this.context = context;
        this.fragment = fragment;
    }

    /**
     * Sets the data of the list containing all characters in the database. Notifies all observers that data has changed.
     *
     * @param newList the list of data to set in the recyclerview
     */
    public void setData(List<MyCharacter> newList) {
        this.characters = newList;
        notifyDataSetChanged();
    }

    /**
     * Inflates the view for each item.
     */
    @NonNull
    @Override
    public CharacterArrayAdapter.CharacterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.character_item, parent, false);

        return new CharacterArrayAdapter.CharacterViewHolder(view);
    }

    /**
     * Sets ui elements to represent the data of the character. Sets click listeners for character editing and selection.
     *
     * @param holder   The ViewHolder represent ing the item at the given position.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull CharacterArrayAdapter.CharacterViewHolder holder, int position) {
        holder.setIsRecyclable(false);

        MyCharacter current = characters.get(position);
        Log.d("characterarrayadapter", current.name + ", " + current.id);

        holder.characterTextView.setText(current.name);
        holder.characterEditButton.setOnClickListener(v -> {
            new CharacterDialog(context, fragment, current.name, current.id).build();
        });
        holder.invisibleButton.setOnClickListener(v -> {
            Log.d("datastore", "onBindViewHolder: ");
            MyDataStore.saveValue(MyDataStore.CURRENT_CHARACTER_KEY, current.id);
            fragment.setInventoryRecyclerView(current.id);
            fragment.setName(current.name);

            fragment.closeDrawer();
        });
    }


    /**
     * Returns the amount of characters in the adapter's list.
     *
     * @return the size of the characters list
     */
    @Override
    public int getItemCount() {
        return characters.size();
    }


    /**
     * Class for holding ui elements for character item in recyclerview.
     */
    public class CharacterViewHolder extends RecyclerView.ViewHolder {
        private final TextView characterTextView;
        private final Button characterEditButton;
        private final Button invisibleButton;


        /**
         * Instantiates new CharacterViewHolder.
         * Binds ui elements to variables in holder.
         */
        public CharacterViewHolder(@NonNull View itemView) {
            super(itemView);
            characterTextView = itemView.findViewById(R.id.character_textview);
            characterEditButton = itemView.findViewById(R.id.edit_character_button);
            invisibleButton = itemView.findViewById(R.id.invisible_button);

        }
    }
}
