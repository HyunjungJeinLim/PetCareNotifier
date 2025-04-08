package com.example.petcarenotifier.ui.calendar;


import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.petcarenotifier.R;
import com.example.petcarenotifier.data.entity.CalendarEventEntity;
import com.example.petcarenotifier.data.entity.TrackingRecordEntity;
import com.example.petcarenotifier.data.model.TrackingRecord;
import com.example.petcarenotifier.ui.calendar.EventAdapter;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalendarActivity extends AppCompatActivity {
    private ListView eventList;
    private CalendarView calendarView;
    private List<CalendarEventEntity> calendarEvents;
    private EventAdapter adapter;
    private List<TrackingRecordEntity> allTracking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        eventList = findViewById(R.id.lvEvents);
        calendarView = findViewById(R.id.calendarView);

        allTracking = TrackingRecord.getAll(this);

        // Show all events on initial load
        refreshEventsForDate(null); // null = show all

        // Filter by selected date
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            String selectedDate = formatDate(year, month, dayOfMonth);
            refreshEventsForDate(selectedDate);
        });
    }

    private void refreshEventsForDate(String dateFilter) {
        List<CalendarEventEntity> result = new ArrayList<>();
        for (TrackingRecordEntity record : allTracking) {
            if (dateFilter == null || record.date.equals(dateFilter)) {
                result.add(new CalendarEventEntity(
                        record.date,
                        capitalize(record.type) + " - " + record.details
                ));
            }
        }
        calendarEvents = result;
        adapter = new EventAdapter(this, calendarEvents);
        eventList.setAdapter(adapter);
    }

    private String formatDate(int year, int month, int dayOfMonth) {
        return String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);
    }


    private String capitalize(String s) {
        return (s == null || s.isEmpty()) ? "" : s.substring(0, 1).toUpperCase() + s.substring(1);
    }
}
