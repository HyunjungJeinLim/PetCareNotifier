package com.example.petcarenotifier.ui.tracking;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.petcarenotifier.R;
import com.example.petcarenotifier.data.entity.TrackingRecordEntity;
import com.example.petcarenotifier.data.model.TrackingRecord;

public class TrackingActivity extends AppCompatActivity {
    private int editRecordId = -1;
    private TrackingRecordEntity recordToEdit = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);

        EditText etDate = findViewById(R.id.etTrackingDate);
        EditText etDetails = findViewById(R.id.etTrackingDetails);
        Spinner spinnerType = findViewById(R.id.spinnerType);
        Button btnSave = findViewById(R.id.btnSaveTracking);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.tracking_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapter);

        // Edit mode: check if an existing record ID is passed
        if (getIntent().hasExtra("edit_index")) {
            editRecordId = getIntent().getIntExtra("edit_index", -1);
            if (editRecordId != -1) {
                for (TrackingRecordEntity rec : TrackingRecord.getAll(this)) {
                    if (rec.id == editRecordId) {
                        recordToEdit = rec;
                        break;
                    }
                }

                if (recordToEdit != null) {
                    etDate.setText(recordToEdit.date);
                    etDetails.setText(recordToEdit.details);
                    spinnerType.setSelection(getSpinnerIndex(spinnerType, capitalize(recordToEdit.type)));
                }
            }
        }

        btnSave.setOnClickListener(v -> {
            String date = etDate.getText().toString();
            String details = etDetails.getText().toString();
            String type = spinnerType.getSelectedItem().toString().toLowerCase();

            if (!date.isEmpty() && !details.isEmpty()) {
                if (recordToEdit != null) {
                    // Update record
                    recordToEdit.date = date;
                    recordToEdit.details = details;
                    recordToEdit.type = type;
                    TrackingRecord.update(this, recordToEdit);
                    Toast.makeText(this, "Tracking updated", Toast.LENGTH_SHORT).show();
                } else {
                    // New record
                    TrackingRecord.add(this, date, details, type);
                    Toast.makeText(this, "Tracking saved", Toast.LENGTH_SHORT).show();
                }
                finish();
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int getSpinnerIndex(Spinner spinner, String value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(value)) {
                return i;
            }
        }
        return 0;
    }

    private String capitalize(String s) {
        if (s == null || s.length() == 0) return "";
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
}
