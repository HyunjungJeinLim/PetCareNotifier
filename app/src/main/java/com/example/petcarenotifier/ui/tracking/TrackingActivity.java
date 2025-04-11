package com.example.petcarenotifier.ui.tracking;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.petcarenotifier.R;
import com.example.petcarenotifier.data.entity.TrackingRecordEntity;
import com.example.petcarenotifier.data.model.TrackingRecord;
import com.example.petcarenotifier.notifications.NotificationReceiver;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TrackingActivity extends AppCompatActivity {
    private int editRecordId = -1;
    private TrackingRecordEntity recordToEdit = null;

    private int selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);

        Button btnPickDate = findViewById(R.id.btnPickDate);
        Button btnPickTime = findViewById(R.id.btnPickTime);
        EditText etDetails = findViewById(R.id.etTrackingDetails);
        Spinner spinnerType = findViewById(R.id.spinnerType);
        Button btnSave = findViewById(R.id.btnSaveTracking);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.tracking_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapter);

        Calendar now = Calendar.getInstance();
        selectedYear = now.get(Calendar.YEAR);
        selectedMonth = now.get(Calendar.MONTH);
        selectedDay = now.get(Calendar.DAY_OF_MONTH);
        selectedHour = now.get(Calendar.HOUR_OF_DAY);
        selectedMinute = now.get(Calendar.MINUTE);

        btnPickDate.setOnClickListener(v -> {
            DatePickerDialog dpd = new DatePickerDialog(this,
                    (view, year, month, dayOfMonth) -> {
                        selectedYear = year;
                        selectedMonth = month;
                        selectedDay = dayOfMonth;
                        btnPickDate.setText(String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth));
                    }, selectedYear, selectedMonth, selectedDay);
            dpd.show();
        });

        btnPickTime.setOnClickListener(v -> {
            TimePickerDialog tpd = new TimePickerDialog(this,
                    (view, hourOfDay, minute) -> {
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

                    // Parse date
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        Date parsedDate = sdf.parse(recordToEdit.date);
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(parsedDate);

                        selectedYear = cal.get(Calendar.YEAR);
                        selectedMonth = cal.get(Calendar.MONTH);
                        selectedDay = cal.get(Calendar.DAY_OF_MONTH);

                        btnPickDate.setText(String.format(Locale.getDefault(), "%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // Parse time
                    try {
                        if (recordToEdit.time != null && !recordToEdit.time.isEmpty()) {
                            String[] parts = recordToEdit.time.split(":");
                            selectedHour = Integer.parseInt(parts[0]);
                            selectedMinute = Integer.parseInt(parts[1]);
                        } else {
                            selectedHour = 12;
                            selectedMinute = 0;
                        }
                    } catch (Exception e) {
                        selectedHour = 12;
                        selectedMinute = 0;
                        e.printStackTrace();
                    }

                    btnPickTime.setText(String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute));
                }


            }
        }

        btnSave.setOnClickListener(v -> {
            String details = etDetails.getText().toString().trim();
            String type = spinnerType.getSelectedItem().toString().toLowerCase();

            Calendar selectedDateTime = Calendar.getInstance();
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
                    recordToEdit.time = time; // ✅ fix

                    TrackingRecord.update(this, recordToEdit);

                    if (triggerAtMillis > System.currentTimeMillis()) {
                        scheduleReminder(this, triggerAtMillis, "Reminder: " + capitalize(type), details, recordToEdit.id);
                    }

                    Toast.makeText(this, "Tracking updated", Toast.LENGTH_SHORT).show();
                } else {
                    int newRecordId = TrackingRecord.add(this, date, details, type, time); // ✅ fix
                    Toast.makeText(this, "Tracking saved", Toast.LENGTH_SHORT).show();

                    if (triggerAtMillis > System.currentTimeMillis()) {
                        scheduleReminder(this, triggerAtMillis, "Reminder: " + capitalize(type), details, newRecordId);
                    }
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
        if (s == null || s.isEmpty()) return "";
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    public static void scheduleReminder(Context context, long triggerAtMillis, String title, String message, int requestCode) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                Toast.makeText(context, "Please allow exact alarm permission in settings.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                context.startActivity(intent);
                return;
            }
        }

        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.putExtra("title", title);
        intent.putExtra("message", message);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_IMMUTABLE
        );

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
            }
        } catch (SecurityException e) {
            Toast.makeText(context, "Exact alarm permission denied", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public static void cancelReminder(Context context, int requestCode) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_IMMUTABLE
        );

        alarmManager.cancel(pendingIntent);
    }

}
