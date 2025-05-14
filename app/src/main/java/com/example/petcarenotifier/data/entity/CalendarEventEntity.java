package com.example.petcarenotifier.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Represents a calendar event entry in the database.
 */
@Entity(tableName = "calendar_events")
public class CalendarEventEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String date;  // Format: "YYYY-MM-DD"
    public String title; // Event title/description

    public CalendarEventEntity(String date, String title) {
        this.date = date;
        this.title = title;
    }
}
