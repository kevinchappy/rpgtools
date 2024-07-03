package com.example.playertool5e.ui.inventory;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.playertool5e.R;
import com.example.playertool5e.database.MyCharacter;
import com.example.playertool5e.database.MyDataStore;

import java.util.List;

public class CharacterArrayAdapter extends RecyclerView.Adapter<CharacterArrayAdapter.CharacterViewHolder> {
    private List<MyCharacter> characters;
    private final Context context;
    private final InventoryViewModel viewModel;

    private InventoryFragment fragment;

    public CharacterArrayAdapter(InventoryViewModel viewModel, Context context, InventoryFragment fragment){
        this.viewModel = viewModel;
        this.context = context;
        this.fragment = fragment;
    }

    public void setData(List<MyCharacter> newList){
        this.characters = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CharacterArrayAdapter.CharacterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.character_item, parent, false);

        return new CharacterArrayAdapter.CharacterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CharacterArrayAdapter.CharacterViewHolder holder, int position) {
        holder.setIsRecyclable(false);

        MyCharacter current = characters.get(position);
        Log.d("characterarrayadapter", current.name + ", " + current.id);

        holder.characterTextView.setText(current.name);
        holder.characterEditButton.setOnClickListener(v-> {
            new CharacterDialog(context,viewModel,fragment, current.name, current.id).build();
        });
        holder.invisibleButton.setOnClickListener(v -> {
            MyDataStore.saveValue(MyDataStore.CURRENT_CHARACTER_KEY, current.id);
            fragment.setInventoryRecyclerView(current.id);
            fragment.setName(current.name);

            fragment.closeDrawer();
        });
    }

    @Override
    public int getItemCount() {
        return characters.size();
    }



    public class CharacterViewHolder extends RecyclerView.ViewHolder{

        private final TextView characterTextView;
        private final Button characterEditButton;
        private final Button invisibleButton;


        public CharacterViewHolder(@NonNull View itemView) {
            super(itemView);
            characterTextView = itemView.findViewById(R.id.character_textview);
            characterEditButton = itemView.findViewById(R.id.edit_character_button);
            invisibleButton = itemView.findViewById(R.id.invisible_button);

        }
    }
}
