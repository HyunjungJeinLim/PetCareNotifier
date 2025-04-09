package com.example.petcarenotifier.ui.tracking;

import android.os.Bundle;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.petcarenotifier.R;
import com.example.petcarenotifier.data.entity.TrackingRecordEntity;
import com.example.petcarenotifier.data.model.TrackingRecord;

import java.util.List;

public class TrackingHistoryActivity extends AppCompatActivity {
    private ListView listView;
    private TrackingHistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking_history);

        listView = findViewById(R.id.lvTrackingHistory);

        List<TrackingRecordEntity> records = TrackingRecord.getAll(this);
        adapter = new TrackingHistoryAdapter(this, records);
        listView.setAdapter(adapter);
    }
}
