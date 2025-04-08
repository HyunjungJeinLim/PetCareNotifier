package com.example.petcarenotifier.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.petcarenotifier.data.entity.TrackingRecordEntity;

import java.util.List;

@Dao
public interface TrackingRecordDao {
    @Query("SELECT * FROM tracking_records")
    List<TrackingRecordEntity> getAll();

    @Query("SELECT * FROM tracking_records WHERE userId = :userId")
    List<TrackingRecordEntity> getAllByUser(int userId);

    @Query("SELECT * FROM tracking_records WHERE userId = :userId AND petId = :petId")
    List<TrackingRecordEntity> getAllByUserAndPet(int userId, int petId);

    @Insert
    void insert(TrackingRecordEntity... records);

    @Update
    void update(TrackingRecordEntity record);

    @Delete
    void delete(TrackingRecordEntity record);
}
