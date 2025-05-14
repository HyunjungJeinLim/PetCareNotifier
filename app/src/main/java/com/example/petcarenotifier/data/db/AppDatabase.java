package com.example.petcarenotifier.data.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.petcarenotifier.data.dao.*;
import com.example.petcarenotifier.data.entity.*;

/**
 * The main Room database for the PetCare Notifier app.
 * Contains DAOs for Pets, Tracking Records, Calendar Events, and Users.
 */
@Database(entities = {
        PetEntity.class,
        TrackingRecordEntity.class,
        CalendarEventEntity.class,
        UserEntity.class
}, version = 10) // ✅ Incremented version due to schema change
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    // DAOs
    public abstract PetDao petDao();
    public abstract TrackingRecordDao trackingRecordDao();
    public abstract UserDao userDao();

    /**
     * Singleton instance of the database.
     */
    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "petcare_db"
                    )
                    .fallbackToDestructiveMigration() // ✅ Rebuild DB without migration
                    .allowMainThreadQueries()         // ⚠️ For development only
                    .build();
        }
        return instance;
    }
}
