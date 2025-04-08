package com.example.petcarenotifier.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "pets")
public class PetEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public String age;
    public String breed;
    public String birthday;
    public int imageResId;
    public int userId;

    public PetEntity(String name, String age, String breed, String birthday, int imageResId) {
        this.name = name;
        this.age = age;
        this.breed = breed;
        this.birthday = birthday;
        this.imageResId = imageResId;
    }
}
