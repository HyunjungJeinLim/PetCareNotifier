package com.example.petcarenotifier.ui.dashboard;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.petcarenotifier.R;
import com.example.petcarenotifier.data.entity.PetEntity;
import com.example.petcarenotifier.data.model.PetData;

import java.io.InputStream;

public class EditPetActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private PetEntity pet;
    private ImageView ivPet;

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
        ivPet = findViewById(R.id.ivPet);

        etName.setText(pet.name);
        etAge.setText(pet.age);
        etBreed.setText(pet.breed);
        etBirthday.setText(pet.birthday);

        loadImage(pet.photoUri);

        findViewById(R.id.btnSave).setOnClickListener(v -> {
            pet.name = etName.getText().toString();
            pet.age = etAge.getText().toString();
            pet.breed = etBreed.getText().toString();
            pet.birthday = etBirthday.getText().toString();
            PetData.update(this, pet);
            finish();
        });

        findViewById(R.id.btnDelete).setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Delete Pet")
                    .setMessage("Are you sure you want to delete this pet?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        PetData.delete(this, pet);
                        PetData.currentPetId = -1;
                        startActivity(new Intent(this, com.example.petcarenotifier.ui.pet.PetSelectionActivity.class));
                        finish();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });

        findViewById(R.id.btnUploadPhoto).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (uri != null) {
                try {
                    final int takeFlags = data.getFlags() &
                            (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    getContentResolver().takePersistableUriPermission(uri, takeFlags);

                    pet.photoUri = uri.toString();
                    PetData.update(this, pet);
                    loadImage(pet.photoUri);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Failed to load selected image.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void loadImage(String uriString) {
        if (uriString != null && !uriString.isEmpty()) {
            try (InputStream inputStream = getContentResolver().openInputStream(Uri.parse(uriString))) {
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                if (bitmap != null) {
                    ivPet.setImageBitmap(bitmap);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
