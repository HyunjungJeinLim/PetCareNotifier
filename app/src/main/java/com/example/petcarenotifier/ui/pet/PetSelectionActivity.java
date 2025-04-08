package com.example.petcarenotifier.ui.pet;

import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.petcarenotifier.R;
import com.example.petcarenotifier.data.entity.PetEntity;
import com.example.petcarenotifier.data.model.PetData;
import com.example.petcarenotifier.ui.dashboard.DashboardActivity;

import java.util.List;

public class PetSelectionActivity extends AppCompatActivity {
    private GridView gridView;
    private List<PetEntity> pets;
    private PetAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_selection);

        gridView = findViewById(R.id.petGrid);
        adapter = new PetAdapter(this, PetData.getAll(this));
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener((parent, view, position, id) -> {
            pets = PetData.getAll(this); // ensure latest data
            PetData.currentPetId = pets.get(position).id;
            startActivity(new Intent(this, DashboardActivity.class));
        });

        findViewById(R.id.btnAddPet).setOnClickListener(v -> {
            PetEntity newPet = new PetEntity("New Pet", "1", "Unknown", "01/01/2023", R.drawable.ic_pet_care);
            PetData.add(this, newPet);
            refreshPets(); // force reload
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshPets(); // ✅ update pets after returning from edit
    }

    private void refreshPets() {
        pets = PetData.getAll(this);
        adapter = new PetAdapter(this, pets);
        gridView.setAdapter(adapter);
    }
}
