package com.example.petcarenotifier.ui.tracking;

import android.os.Bundle;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.petcarenotifier.R;
import com.example.petcarenotifier.data.entity.TrackingRecordEntity;
import com.example.petcarenotifier.data.model.TrackingRecord;

import java.util.List;

/**
 * Activity to show the history of tracking records.
 */
public class TrackingHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking_history);

        ListView listView = findViewById(R.id.lvTrackingHistory);

        // Load all tracking records and show in list
        List<TrackingRecordEntity> records = TrackingRecord.getAll(this);
        TrackingHistoryAdapter adapter = new TrackingHistoryAdapter(this, records);
        listView.setAdapter(adapter);
    }
}
