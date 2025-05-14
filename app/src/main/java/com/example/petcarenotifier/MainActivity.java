package com.example.petcarenotifier;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.petcarenotifier.ui.auth.LoginActivity;
import com.example.petcarenotifier.ui.pet.PetSelectionActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // ✅ Apply saved theme before anything else
        SharedPreferences settingsPrefs = getSharedPreferences("settings", MODE_PRIVATE);
        int savedThemeMode = settingsPrefs.getInt("night_mode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        AppCompatDelegate.setDefaultNightMode(savedThemeMode);

        super.onCreate(savedInstanceState);

        // ✅ First: handle notification permission for Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                        new String[]{android.Manifest.permission.POST_NOTIFICATIONS},
                        1001
                );
            }
        }

        // ✅ Then handle login state
        SharedPreferences prefs = getSharedPreferences("petcare_prefs", MODE_PRIVATE);
        if (prefs.contains("logged_in_user_id")) {
            startActivity(new Intent(this, PetSelectionActivity.class));
            finish();
            return;
        }

        // ✅ Show welcome screen if not logged in
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnStart).setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
}
