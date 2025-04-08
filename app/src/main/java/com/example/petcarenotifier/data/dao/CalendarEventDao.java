package com.example.petcarenotifier.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.petcarenotifier.data.entity.CalendarEventEntity;

import java.util.List;

@Dao
public interface CalendarEventDao {
    @Query("SELECT * FROM calendar_events")
    List<CalendarEventEntity> getAll();

    @Insert
    void insert(CalendarEventEntity event);

    @Delete
    void delete(CalendarEventEntity event);
}
