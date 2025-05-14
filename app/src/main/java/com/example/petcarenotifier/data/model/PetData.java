package com.example.petcarenotifier.data.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.example.petcarenotifier.data.db.AppDatabase;
import com.example.petcarenotifier.data.entity.PetEntity;

import java.util.List;

/**
 * Helper methods to manage PetEntity records.
 */
public class PetData {
    public static int currentPetId = -1;

    /**
     * Add a pet for the current logged-in user.
     */
    public static void add(Context context, PetEntity pet) {
        SharedPreferences prefs = context.getSharedPreferences("petcare_prefs", Context.MODE_PRIVATE);
        int userId = prefs.getInt("logged_in_user_id", -1);

        if (userId == -1) {
            Toast.makeText(context, "⚠️ No logged-in user. Please log in before adding a pet.", Toast.LENGTH_LONG).show();
            Log.e("PetData", "Failed to insert pet: no userId found.");
            return;
        }

        pet.userId = userId;
        Log.d("PetData", "Trying to insert pet with userId: " + userId);

        try {
            AppDatabase.getInstance(context).petDao().insert(pet);
            Toast.makeText(context, "✅ Pet inserted for userId: " + userId, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("PetData", "Insert failed: " + e.getMessage(), e);
            Toast.makeText(context, "❌ Pet insert failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public static List<PetEntity> getAll(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("petcare_prefs", Context.MODE_PRIVATE);
        int userId = prefs.getInt("logged_in_user_id", -1);

        if (userId == -1) {
            Log.w("PetData", "No userId found in prefs. Returning empty pet list.");
            return List.of(); // Java 9+ compatible. Use new ArrayList<>() if needed.
        }

        return AppDatabase.getInstance(context).petDao().getAll(userId);
    }

    public static PetEntity getById(Context context, int id) {
        return AppDatabase.getInstance(context).petDao().getById(id);
    }

    public static void update(Context context, PetEntity pet) {
        AppDatabase.getInstance(context).petDao().update(pet);
    }

    public static void delete(Context context, PetEntity pet) {
        AppDatabase.getInstance(context).petDao().delete(pet);
    }
}
