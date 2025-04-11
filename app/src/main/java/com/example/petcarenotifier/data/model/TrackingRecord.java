package com.example.petcarenotifier.data.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.petcarenotifier.data.db.AppDatabase;
import com.example.petcarenotifier.data.entity.TrackingRecordEntity;

import java.util.List;

public class TrackingRecord {
    public static List<TrackingRecordEntity> getAll(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("petcare_prefs", Context.MODE_PRIVATE);
        int userId = prefs.getInt("logged_in_user_id", -1);
        int petId = PetData.currentPetId;
        return AppDatabase.getInstance(context).trackingRecordDao().getAllByUserAndPet(userId, petId);
    }

    public static int add(Context context, String date, String details, String type, String time) {
        SharedPreferences prefs = context.getSharedPreferences("petcare_prefs", Context.MODE_PRIVATE);
        int userId = prefs.getInt("logged_in_user_id", -1);
        int petId = PetData.currentPetId;

        TrackingRecordEntity record = new TrackingRecordEntity(date, details, type);
        record.time = time;
        record.userId = userId;
        record.petId = petId;

        long id = AppDatabase.getInstance(context).trackingRecordDao().insert(record);
        return (int) id;
    }


    public static void update(Context context, TrackingRecordEntity record) {
        AppDatabase.getInstance(context).trackingRecordDao().update(record);
    }

    public static void delete(Context context, TrackingRecordEntity record) {
        AppDatabase.getInstance(context).trackingRecordDao().delete(record);
    }
}
