package com.example.petcarenotifier.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.petcarenotifier.data.entity.UserEntity;

/**
 * DAO for user login and registration.
 */
@Dao
public interface UserDao {

    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    UserEntity login(String username, String password);

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    UserEntity getUser(String username);

    @Query("SELECT * FROM Users WHERE Username = :username LIMIT 1")
    UserEntity getByUsername(String username);

    @Insert
    long insert(UserEntity user);
}
