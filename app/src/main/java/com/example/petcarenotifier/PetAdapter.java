package com.example.petcarenotifier;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PetAdapter extends BaseAdapter {
    private final Context context;
    private final java.util.List<PetData.Pet> pets;

    public PetAdapter(Context context, java.util.List<PetData.Pet> pets) {
        this.context = context;
        this.pets = pets;
    }

    @Override public int getCount() { return pets.size(); }
    @Override public Object getItem(int position) { return pets.get(position); }
    @Override public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_pet, parent, false);
        }

        PetData.Pet pet = pets.get(position);
        ((ImageView)convertView.findViewById(R.id.ivPet)).setImageResource(pet.imageResId);
        ((TextView)convertView.findViewById(R.id.tvPetName)).setText(pet.name);

        return convertView;
    }
}