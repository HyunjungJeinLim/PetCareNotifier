package com.example.petcarenotifier.data.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Represents a tracking record for a specific pet and user.
 */
@Entity(
        tableName = "tracking_records",
        foreignKeys = {
                @ForeignKey(
                        entity = UserEntity.class,
                        parentColumns = "id",
                        childColumns = "userId",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = PetEntity.class,
                        parentColumns = "id",
                        childColumns = "petId",
                        onDelete = ForeignKey.CASCADE
                )
        },
        indices = {
                @Index("userId"),
                @Index("petId")
        }
)
public class TrackingRecordEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String date;     // e.g., "2025-04-14"
    public String details;  // e.g., "Gave medication"
    public String type;     // e.g., "medicine", "vaccine", etc.

    // Foreign keys
    public int userId;
    public int petId;

    public String time;     // e.g., "14:30"

    public TrackingRecordEntity(String date, String details, String type) {
        this.date = date;
        this.details = details;
        this.type = type;
    }
}
