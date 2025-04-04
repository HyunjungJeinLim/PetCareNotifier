package com.example.petcarenotifier;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class EditPetActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pet);

        PetData.Pet pet = PetData.pets.get(PetData.currentPetIndex);

        android.widget.EditText etName = findViewById(R.id.etName);
        android.widget.EditText etAge = findViewById(R.id.etAge);
        android.widget.EditText etBreed = findViewById(R.id.etBreed);
        android.widget.EditText etBirthday = findViewById(R.id.etBirthday);

        etName.setText(pet.name);
        etAge.setText(pet.age);
        etBreed.setText(pet.breed);
        etBirthday.setText(pet.birthday);

        findViewById(R.id.btnSave).setOnClickListener(v -> {
            pet.name = etName.getText().toString();
            pet.age = etAge.getText().toString();
            pet.breed = etBreed.getText().toString();
            pet.birthday = etBirthday.getText().toString();
            finish();
        });
    }
}