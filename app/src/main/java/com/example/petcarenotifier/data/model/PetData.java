package com.example.petcarenotifier.data.model;

import android.content.Context;

import com.example.petcarenotifier.data.db.AppDatabase;
import com.example.petcarenotifier.data.entity.PetEntity;

import java.util.List;

public class PetData {
    public static int currentPetId = -1; // previously currentPetIndex

    public static List<PetEntity> getAll(Context context) {
        int userId = context.getSharedPreferences("petcare_prefs", Context.MODE_PRIVATE)
                .getInt("logged_in_user_id", -1);
        return AppDatabase.getInstance(context).petDao().getAllByUser(userId);
    }

    public static void add(Context context, PetEntity pet) {
        pet.userId = context.getSharedPreferences("petcare_prefs", Context.MODE_PRIVATE)
                .getInt("logged_in_user_id", -1);
        AppDatabase.getInstance(context).petDao().insert(pet);
    }


    public static void update(Context context, PetEntity pet) {
        AppDatabase.getInstance(context).petDao().update(pet);
    }

    public static void delete(Context context, PetEntity pet) {
        AppDatabase.getInstance(context).petDao().delete(pet);
    }

    public static PetEntity getById(Context context, int id) {
        for (PetEntity pet : getAll(context)) {
            if (pet.id == id) return pet;
        }
        return null;
    }
}
