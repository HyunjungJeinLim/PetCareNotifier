package com.example.petcarenotifier.data.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Represents a pet owned by a specific user.
 */
@Entity(
        tableName = "pets",
        foreignKeys = @ForeignKey(
                entity = UserEntity.class,
                parentColumns = "id",
                childColumns = "userId",
                onDelete = ForeignKey.CASCADE
        ),
        indices = @Index("userId")
)
public class PetEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public String age;
    public String breed;
    public String birthday;

    // Links to default avatar (resource ID)
    public int imageResId;

    // Foreign key: ID of the owner (UserEntity)
    public int userId;

    // Optional photo URI
    public String photoUri;

    public PetEntity(String name, String age, String breed, String birthday, int imageResId) {
        this.name = name;
        this.age = age;
        this.breed = breed;
        this.birthday = birthday;
        this.imageResId = imageResId;
    }
}
