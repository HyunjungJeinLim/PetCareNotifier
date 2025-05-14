package com.example.petcarenotifier.data.dao;

import androidx.room.*;
import com.example.petcarenotifier.data.entity.TrackingRecordEntity;

import java.util.List;

/**
 * DAO for tracking record operations.
 */
@Dao
public interface TrackingRecordDao {
    @Query("SELECT * FROM tracking_records")
    List<TrackingRecordEntity> getAll();

    @Query("SELECT * FROM tracking_records WHERE userId = :userId")
    List<TrackingRecordEntity> getAllByUser(int userId);

    @Query("SELECT * FROM tracking_records WHERE userId = :userId AND petId = :petId")
    List<TrackingRecordEntity> getAllByUserAndPet(int userId, int petId);

    @Insert
    long insert(TrackingRecordEntity record);

    @Update
    void update(TrackingRecordEntity record);

    @Delete
    void delete(TrackingRecordEntity record);
}
