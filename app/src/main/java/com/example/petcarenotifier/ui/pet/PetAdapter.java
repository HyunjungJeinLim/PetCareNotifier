package com.example.petcarenotifier.ui.pet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.petcarenotifier.R;
import com.example.petcarenotifier.data.entity.PetEntity;

import java.util.List;

public class PetAdapter extends BaseAdapter {
    private final Context context;
    private final List<PetEntity> pets;

    public PetAdapter(Context context, List<PetEntity> pets) {
        this.context = context;
        this.pets = pets;
    }

    @Override
    public int getCount() {
        return pets.size();
    }

    @Override
    public Object getItem(int position) {
        return pets.get(position);
    }

    @Override
    public long getItemId(int position) {
        return pets.get(position).id; // if you want to return Room ID
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_pet, parent, false);
        }

        PetEntity pet = pets.get(position);

        ImageView ivPet = convertView.findViewById(R.id.ivPet);
        TextView tvPetName = convertView.findViewById(R.id.tvPetName);

        ivPet.setImageResource(pet.imageResId);
        tvPetName.setText(pet.name);

        return convertView;
    }
}
