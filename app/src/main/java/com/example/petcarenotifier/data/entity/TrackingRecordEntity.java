package com.example.petcarenotifier.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tracking_records")
public class TrackingRecordEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String date;
    public String details;
    public String type;
    public int userId;
    public int petId;

    public TrackingRecordEntity(String date, String details, String type) {
        this.date = date;
        this.details = details;
        this.type = type;
    }
}
