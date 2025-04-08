package com.example.petcarenotifier;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.petcarenotifier.ui.auth.LoginActivity;
import com.example.petcarenotifier.ui.pet.PetSelectionActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = getSharedPreferences("petcare_prefs", MODE_PRIVATE);
        if (prefs.contains("logged_in_user_id")) {
            startActivity(new Intent(this, PetSelectionActivity.class));
            finish();
            return;
        }

        findViewById(R.id.btnStart).setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish(); // optional, so user can't go back
        });
    }

}
