package com.example.petcarenotifier.data.dao;

import androidx.room.*;
import com.example.petcarenotifier.data.entity.PetEntity;

import java.util.List;

/**
 * DAO for pet database operations.
 */
@Dao
public interface PetDao {
    @Query("SELECT * FROM pets")
    List<PetEntity> getAll();

    @Query("SELECT * FROM pets WHERE userId = :userId")
    List<PetEntity> getAllByUser(int userId);

    @Insert
    void insert(PetEntity pet);

    @Update
    void update(PetEntity pet);

    @Delete
    void delete(PetEntity pet);

    // ✅ NEW: Get all pets for a specific user
    @Query("SELECT * FROM pets WHERE userId = :userId")
    List<PetEntity> getAll(int userId);

    // ✅ NEW: Get one pet by ID
    @Query("SELECT * FROM pets WHERE id = :id LIMIT 1")
    PetEntity getById(int id);
}
