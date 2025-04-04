package com.example.petcarenotifier;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class PetSelectionActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_selection);

        android.widget.GridView gridView = findViewById(R.id.petGrid);
        gridView.setAdapter(new PetAdapter(this, PetData.pets));

        gridView.setOnItemClickListener((parent, view, position, id) -> {
            PetData.currentPetIndex = position;
            startActivity(new Intent(this, DashboardActivity.class));
        });

        findViewById(R.id.btnAddPet).setOnClickListener(v -> {
            PetData.pets.add(new PetData.Pet("New Pet", "1", "Unknown", "01/01/2023", R.drawable.ic_pet_care));
            gridView.invalidateViews();
        });
    }
}