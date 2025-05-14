package com.example.petcarenotifier.ui.pet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.petcarenotifier.R;
import com.example.petcarenotifier.data.db.AppDatabase;
import com.example.petcarenotifier.data.entity.PetEntity;
import com.example.petcarenotifier.data.entity.UserEntity;
import com.example.petcarenotifier.data.model.PetData;
import com.example.petcarenotifier.ui.dashboard.DashboardActivity;

import java.util.List;

/**
 * Activity for selecting a pet or adding a new one.
 */
public class PetSelectionActivity extends AppCompatActivity {
    private GridView gridView;
    private List<PetEntity> pets;
    private PetAdapter adapter;

    @SuppressLint("ApplySharedPref")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_selection);

        SharedPreferences prefs = getSharedPreferences("petcare_prefs", MODE_PRIVATE);
        int userId = prefs.getInt("logged_in_user_id", -1);

        // ✅ Always auto-create test user if none exists
        if (userId == -1) {
            AppDatabase db = AppDatabase.getInstance(this);
            UserEntity testUser = new UserEntity("testuser", "1234");

            long insertedId = db.userDao().insert(testUser);
            userId = (int) insertedId;

            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("logged_in_user_id", userId);
            editor.putString("logged_in_username", testUser.username);
            editor.commit();

            Toast.makeText(this, "Test user created (ID: " + userId + ")", Toast.LENGTH_SHORT).show();
        }

        gridView = findViewById(R.id.petGrid);

        // Load existing pets
        pets = PetData.getAll(this);
        adapter = new PetAdapter(this, pets);
        gridView.setAdapter(adapter);

        // Handle pet selection
        gridView.setOnItemClickListener((parent, view, position, id) -> {
            PetData.currentPetId = pets.get(position).id;
            startActivity(new Intent(this, DashboardActivity.class));
        });

        // Add a default pet entry
        findViewById(R.id.btnAddPet).setOnClickListener(v -> {
            PetEntity newPet = new PetEntity("New Pet", "1", "Unknown", "01/01/2023", R.drawable.ic_pet_care);
            PetData.add(this, newPet);
            refreshPets();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshPets();
    }

    private void refreshPets() {
        pets = PetData.getAll(this);
        adapter = new PetAdapter(this, pets);
        gridView.setAdapter(adapter);
    }
}
