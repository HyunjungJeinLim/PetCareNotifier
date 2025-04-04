package com.example.petcarenotifier;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class CalendarActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        android.widget.ListView eventList = findViewById(R.id.lvEvents);
        eventList.setAdapter(new EventAdapter(this, CalendarEvent.events));

        findViewById(R.id.btnAddEvent).setOnClickListener(v -> {
            String date = ((android.widget.EditText)findViewById(R.id.etEventDate)).getText().toString();
            String title = ((android.widget.EditText)findViewById(R.id.etEventTitle)).getText().toString();

            if (!date.isEmpty() && !title.isEmpty()) {
                CalendarEvent.events.add(new CalendarEvent.Event(date, title));
                ((android.widget.BaseAdapter)eventList.getAdapter()).notifyDataSetChanged();
            }
        });
    }
}