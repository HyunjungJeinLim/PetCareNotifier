package com.example.petcarenotifier.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.petcarenotifier.data.entity.PetEntity;

import java.util.List;

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
}
