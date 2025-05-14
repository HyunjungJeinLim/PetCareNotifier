package com.example.petcarenotifier.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Represents a user with login credentials.
 */
@Entity(tableName = "users")
public class UserEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String username;
    public String password;
    public String name;     // ✅ Full name (e.g. from Google Sign-In)
    public String role;     // ✅ Optional: admin/tester/user

    public UserEntity() {
        // Required for Room
    }

    public UserEntity(String username, String password) {
        this.username = username;
        this.password = password;
    }

}
