package com.example.petcarenotifier.ui.calendar;

import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.petcarenotifier.R;
import com.example.petcarenotifier.data.entity.CalendarEventEntity;
import com.example.petcarenotifier.data.entity.TrackingRecordEntity;
import com.example.petcarenotifier.data.model.TrackingRecord;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalendarActivity extends AppCompatActivity {
    private ListView eventList;
    private List<CalendarEventEntity> calendarEvents;
    private EventAdapter adapter;
    private List<TrackingRecordEntity> allTracking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        eventList = findViewById(R.id.lvEvents);
        CalendarView calendarView = findViewById(R.id.calendarView);

        allTracking = TrackingRecord.getAll(this); // Load all tracking records once
        String today = formatDate(new Date());
        refreshEventsForDate(today); // Show today's events by default

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            String selectedDate = formatDate(year, month, dayOfMonth);
            refreshEventsForDate(selectedDate);
        });
    }

    private void refreshEventsForDate(String date) {
        List<CalendarEventEntity> result = new ArrayList<>();
        for (TrackingRecordEntity record : allTracking) {
            if (record.date.equals(date)) {
                result.add(new CalendarEventEntity(record.date,
                        capitalize(record.type) + " - " + record.details));
            }
        }
        calendarEvents = result;
        adapter = new EventAdapter(this, calendarEvents);
        eventList.setAdapter(adapter);
    }

    private String formatDate(int year, int month, int dayOfMonth) {
        // Note: month is 0-based
        return String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);
    }

    private String formatDate(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date);
    }

    private String capitalize(String s) {
        if (s == null || s.isEmpty()) return "";
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
}
