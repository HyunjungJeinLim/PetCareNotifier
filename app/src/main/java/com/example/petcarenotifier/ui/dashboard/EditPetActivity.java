package com.example.petcarenotifier.ui.dashboard;

import android.os.Bundle;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

import com.example.petcarenotifier.R;
import com.example.petcarenotifier.data.entity.PetEntity;
import com.example.petcarenotifier.data.model.PetData;

public class EditPetActivity extends AppCompatActivity {
    private PetEntity pet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pet);

        pet = PetData.getById(this, PetData.currentPetId);
        if (pet == null) {
            finish();
            return;
        }

        EditText etName = findViewById(R.id.etName);
        EditText etAge = findViewById(R.id.etAge);
        EditText etBreed = findViewById(R.id.etBreed);
        EditText etBirthday = findViewById(R.id.etBirthday);

        etName.setText(pet.name);
        etAge.setText(pet.age);
        etBreed.setText(pet.breed);
        etBirthday.setText(pet.birthday);

        findViewById(R.id.btnSave).setOnClickListener(v -> {
            pet.name = etName.getText().toString();
            pet.age = etAge.getText().toString();
            pet.breed = etBreed.getText().toString();
            pet.birthday = etBirthday.getText().toString();

            PetData.update(this, pet);
            finish();
        });
    }
}
