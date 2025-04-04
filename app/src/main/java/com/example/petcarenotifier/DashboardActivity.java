package com.example.petcarenotifier;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Pet info display
        PetData.Pet pet = PetData.pets.get(PetData.currentPetIndex);
        ((android.widget.ImageView)findViewById(R.id.ivPet)).setImageResource(pet.imageResId);
        ((android.widget.TextView)findViewById(R.id.tvPetName)).setText(pet.name);
        ((android.widget.TextView)findViewById(R.id.tvAge)).setText(getString(R.string.age_format, pet.age));
        ((android.widget.TextView)findViewById(R.id.tvBreed)).setText(getString(R.string.breed_format, pet.breed));
        ((android.widget.TextView)findViewById(R.id.tvBirthday)).setText(getString(R.string.birthday_format, pet.birthday));

        // Button handlers
        findViewById(R.id.btnPrevious).setOnClickListener(v -> finish());
        findViewById(R.id.btnEdit).setOnClickListener(v -> startActivity(new Intent(this, EditPetActivity.class)));
        findViewById(R.id.btnCalendar).setOnClickListener(v -> startActivity(new Intent(this, CalendarActivity.class)));
    }
}