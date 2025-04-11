package com.example.petcarenotifier.data.db;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.petcarenotifier.data.dao.PetDao;
import com.example.petcarenotifier.data.dao.TrackingRecordDao;
import com.example.petcarenotifier.data.dao.UserDao;
import com.example.petcarenotifier.data.entity.CalendarEventEntity;
import com.example.petcarenotifier.data.entity.PetEntity;
import com.example.petcarenotifier.data.entity.TrackingRecordEntity;
import com.example.petcarenotifier.data.entity.UserEntity;

@Database(entities = {
        PetEntity.class,
        TrackingRecordEntity.class,
        CalendarEventEntity.class,
        UserEntity.class
}, version = 8)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public abstract PetDao petDao();
    public abstract TrackingRecordDao trackingRecordDao();

    public abstract UserDao userDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "petcare_db"
                    )
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
}

