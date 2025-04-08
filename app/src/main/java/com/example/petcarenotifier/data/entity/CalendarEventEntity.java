package com.example.petcarenotifier.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "calendar_events")
public class CalendarEventEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String date;
    public String title;

    public CalendarEventEntity(String date, String title) {
        this.date = date;
        this.title = title;
    }
}
