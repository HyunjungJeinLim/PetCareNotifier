package com.example.petcarenotifier.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.petcarenotifier.ui.calendar.CalendarActivity;
import com.example.petcarenotifier.R;
import com.example.petcarenotifier.ui.tracking.TrackingActivity;
import com.example.petcarenotifier.data.entity.PetEntity;
import com.example.petcarenotifier.data.entity.TrackingRecordEntity;
import com.example.petcarenotifier.data.model.PetData;
import com.example.petcarenotifier.data.model.TrackingRecord;
import com.example.petcarenotifier.ui.auth.LoginActivity;
import com.example.petcarenotifier.ui.tracking.TrackingHistoryActivity;

public class DashboardActivity extends AppCompatActivity {
    private PetEntity pet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Button handlers

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        findViewById(R.id.btnEdit).setOnClickListener(v -> startActivity(new Intent(this, EditPetActivity.class)));
        findViewById(R.id.btnCalendar).setOnClickListener(v -> startActivity(new Intent(this, CalendarActivity.class)));
        findViewById(R.id.btnAddTracking).setOnClickListener(v -> startActivity(new Intent(this, TrackingActivity.class)));
        findViewById(R.id.btnHistory).setOnClickListener(v ->
                startActivity(new Intent(this, TrackingHistoryActivity.class)));


        refreshPetInfo();

        findViewById(R.id.btnLogout).setOnClickListener(v -> {
            getSharedPreferences("petcare_prefs", MODE_PRIVATE)
                    .edit()
                    .clear()
                    .apply();

            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // clears back stack
            startActivity(intent);
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshPetInfo();
        populateTrackingData();
    }


    private void refreshPetInfo() {
        pet = PetData.getById(this, PetData.currentPetId);
        if (pet == null) return;

        ((android.widget.ImageView)findViewById(R.id.ivPet)).setImageResource(pet.imageResId);
        ((android.widget.TextView)findViewById(R.id.tvPetName)).setText(pet.name);
        ((android.widget.TextView)findViewById(R.id.tvAge)).setText(getString(R.string.age_format, pet.age));
        ((android.widget.TextView)findViewById(R.id.tvBreed)).setText(getString(R.string.breed_format, pet.breed));
        ((android.widget.TextView)findViewById(R.id.tvBirthday)).setText(getString(R.string.birthday_format, pet.birthday));
    }



    private void populateTrackingData() {
        android.widget.LinearLayout llPoop = findViewById(R.id.llPoopRecords);
        android.widget.LinearLayout llPotty = findViewById(R.id.llPottyRecords);
        android.widget.LinearLayout llVaccine = findViewById(R.id.llVaccineRecords);
        android.widget.LinearLayout llMedicine = findViewById(R.id.llMedicineRecords);

        llPoop.removeAllViews();
        llPotty.removeAllViews();
        llVaccine.removeAllViews();
        llMedicine.removeAllViews();

        java.util.List<TrackingRecordEntity> records = TrackingRecord.getAll(this);

        for (int i = 0; i < records.size(); i++) {
            TrackingRecordEntity record = records.get(i);

            android.widget.TextView tv = new android.widget.TextView(this);
            tv.setText(record.date + ": " + record.details);
            tv.setTextSize(14);
            tv.setPadding(4, 2, 4, 2);

            int index = i;

            // Click to edit
            tv.setOnClickListener(v -> {
                Intent intent = new Intent(this, TrackingActivity.class);
                intent.putExtra("edit_index", record.id); // use ID, not index
                startActivity(intent);
            });

            // Long click to delete with confirmation
            tv.setOnLongClickListener(v -> {
                new android.app.AlertDialog.Builder(this)
                        .setTitle("Delete Entry")
                        .setMessage("Are you sure you want to delete this tracking record?")
                        .setPositiveButton("Delete", (dialog, which) -> {
                            TrackingRecord.delete(this, record);
                            populateTrackingData();
                            android.widget.Toast.makeText(this, "Tracking deleted", android.widget.Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
                return true;
            });

            switch (record.type.toLowerCase()) {
                case "poop":
                    llPoop.addView(tv);
                    break;
                case "potty":
                    llPotty.addView(tv);
                    break;
                case "vaccine":
                    llVaccine.addView(tv);
                    break;
                case "medicine":
                    llMedicine.addView(tv);
                    break;
            }
        }


    }


}