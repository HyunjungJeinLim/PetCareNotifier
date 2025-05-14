package com.example.petcarenotifier.ui.tracking;

import android.Manifest;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.widget.*;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.petcarenotifier.R;
import com.example.petcarenotifier.data.entity.TrackingRecordEntity;
import com.example.petcarenotifier.data.model.TrackingRecord;
import com.example.petcarenotifier.notifications.NotificationReceiver;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/** @noinspection ALL*/
public class TrackingActivity extends AppCompatActivity {
    private int editRecordId = -1;
    private TrackingRecordEntity recordToEdit = null;
    private int selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_CALENDAR, Manifest.permission.READ_CALENDAR}, 100);
        }

        Button btnPickDate = findViewById(R.id.btnPickDate);
        Button btnPickTime = findViewById(R.id.btnPickTime);
        EditText etDetails = findViewById(R.id.etTrackingDetails);
        Spinner spinnerType = findViewById(R.id.spinnerType);
        Button btnSave = findViewById(R.id.btnSaveTracking);
        CheckBox checkboxCalendar = findViewById(R.id.checkboxAddToCalendar);

        // Always show the checkbox if user signed in via Google at least once
        boolean googleSignedIn = getSharedPreferences("petcare_prefs", MODE_PRIVATE)
                .getBoolean("google_signed_in", false);
        checkboxCalendar.setVisibility(googleSignedIn ? CheckBox.VISIBLE : CheckBox.GONE);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.tracking_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapter);

        java.util.Calendar now = java.util.Calendar.getInstance();
        selectedYear = now.get(java.util.Calendar.YEAR);
        selectedMonth = now.get(java.util.Calendar.MONTH);
        selectedDay = now.get(java.util.Calendar.DAY_OF_MONTH);
        selectedHour = now.get(java.util.Calendar.HOUR_OF_DAY);
        selectedMinute = now.get(java.util.Calendar.MINUTE);

        btnPickDate.setOnClickListener(v -> {
            DatePickerDialog dpd = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                selectedYear = year;
                selectedMonth = month;
                selectedDay = dayOfMonth;
                btnPickDate.setText(String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth));
            }, selectedYear, selectedMonth, selectedDay);
            dpd.show();
        });

        btnPickTime.setOnClickListener(v -> {
            TimePickerDialog tpd = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
                selectedHour = hourOfDay;
                selectedMinute = minute;
                btnPickTime.setText(String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute));
            }, selectedHour, selectedMinute, true);
            tpd.show();
        });

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
                    etDetails.setText(recordToEdit.details);
                    spinnerType.setSelection(getSpinnerIndex(spinnerType, capitalize(recordToEdit.type)));
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        Date parsedDate = sdf.parse(recordToEdit.date);
                        java.util.Calendar cal = java.util.Calendar.getInstance();
                        cal.setTime(parsedDate);
                        selectedYear = cal.get(java.util.Calendar.YEAR);
                        selectedMonth = cal.get(java.util.Calendar.MONTH);
                        selectedDay = cal.get(java.util.Calendar.DAY_OF_MONTH);
                        btnPickDate.setText(String.format(Locale.getDefault(), "%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        if (recordToEdit.time != null && !recordToEdit.time.isEmpty()) {
                            String[] parts = recordToEdit.time.split(":");
                            selectedHour = Integer.parseInt(parts[0]);
                            selectedMinute = Integer.parseInt(parts[1]);
                            btnPickTime.setText(String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        btnSave.setOnClickListener(v -> {
            String details = etDetails.getText().toString().trim();
            String type = spinnerType.getSelectedItem().toString().toLowerCase();

            java.util.Calendar selectedDateTime = java.util.Calendar.getInstance();
            selectedDateTime.set(selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute, 0);
            long triggerAtMillis = selectedDateTime.getTimeInMillis();

            String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selectedDateTime.getTime());
            String time = String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute);

            if (!date.isEmpty() && !details.isEmpty()) {
                if (recordToEdit != null) {
                    cancelReminder(this, recordToEdit.id);
                    recordToEdit.date = date;
                    recordToEdit.details = details;
                    recordToEdit.type = type;
                    recordToEdit.time = time;
                    TrackingRecord.update(this, recordToEdit);

                    if (checkboxCalendar.isChecked()) {
                        showCalendarPicker(date, time, capitalize(type), details);
                    } else {
                        finish();
                    }

                    if (triggerAtMillis > System.currentTimeMillis()) {
                        scheduleReminder(this, triggerAtMillis, "Reminder: " + capitalize(type), details, recordToEdit.id);
                    }
                } else {
                    int newId = TrackingRecord.add(this, date, details, type, time);
                    Toast.makeText(this, "Tracking saved", Toast.LENGTH_SHORT).show();

                    if (checkboxCalendar.isChecked()) {
                        showCalendarPicker(date, time, capitalize(type), details);
                    } else {
                        finish();
                    }

                    if (triggerAtMillis > System.currentTimeMillis()) {
                        scheduleReminder(this, triggerAtMillis, "Reminder: " + capitalize(type), details, newId);
                    }
                }
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showCalendarPicker(String date, String time, String title, String description) {
        Cursor cursor = getContentResolver().query(
                CalendarContract.Calendars.CONTENT_URI,
                new String[]{CalendarContract.Calendars._ID, CalendarContract.Calendars.CALENDAR_DISPLAY_NAME},
                CalendarContract.Calendars.VISIBLE + " = 1 AND " + CalendarContract.Calendars.SYNC_EVENTS + " = 1",
                null, null
        );

        if (cursor == null || cursor.getCount() == 0) {
            Toast.makeText(this, "No calendars available", Toast.LENGTH_LONG).show();
            return;
        }

        ArrayList<Long> calendarIds = new ArrayList<>();
        ArrayList<String> calendarNames = new ArrayList<>();

        while (cursor.moveToNext()) {
            calendarIds.add(cursor.getLong(0));
            calendarNames.add(cursor.getString(1));
        }
        cursor.close();

        new AlertDialog.Builder(this)
                .setTitle("Choose Calendar")
                .setItems(calendarNames.toArray(new String[0]), (dialog, which) -> {
                    insertEventToCalendar(calendarIds.get(which), date, time, title, description);
                })
                .setCancelable(false)
                .show();
    }

    private void insertEventToCalendar(long calendarId, String date, String time, String title, String description) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            Date parsedDate = sdf.parse(date + "T" + time + ":00");
            if (parsedDate == null) return;

            long startMillis = parsedDate.getTime();
            long endMillis = startMillis + 60 * 60 * 1000;

            ContentValues values = new ContentValues();
            values.put(CalendarContract.Events.DTSTART, startMillis);
            values.put(CalendarContract.Events.DTEND, endMillis);
            values.put(CalendarContract.Events.TITLE, "PetCare: " + title);
            values.put(CalendarContract.Events.DESCRIPTION, description);
            values.put(CalendarContract.Events.CALENDAR_ID, calendarId);
            values.put(CalendarContract.Events.EVENT_TIMEZONE, java.util.TimeZone.getDefault().getID());

            Uri uri = getContentResolver().insert(CalendarContract.Events.CONTENT_URI, values);
            if (uri != null) {
                Toast.makeText(this, "Event added to calendar", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to add event", Toast.LENGTH_SHORT).show();
            }
            finish();
        } catch (Exception e) {
            Log.e("CalendarDebug", "Error inserting event", e);
            Toast.makeText(this, "Calendar insert failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
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
        return (s == null || s.isEmpty()) ? "" : s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    public static void scheduleReminder(Context context, long triggerAtMillis, String title, String message, int requestCode) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
            Toast.makeText(context, "Allow exact alarm permission in settings.", Toast.LENGTH_LONG).show();
            context.startActivity(new Intent(android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM));
            return;
        }

        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.putExtra("title", title);
        intent.putExtra("message", message);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, requestCode, intent, PendingIntent.FLAG_IMMUTABLE);

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
            }
        } catch (SecurityException e) {
            e.printStackTrace();
            Toast.makeText(context, "Alarm permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    public static void cancelReminder(Context context, int requestCode) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, requestCode, new Intent(context, NotificationReceiver.class), PendingIntent.FLAG_IMMUTABLE);
        alarmManager.cancel(pendingIntent);
    }
}
