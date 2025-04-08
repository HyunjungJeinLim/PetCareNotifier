package com.example.petcarenotifier.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.petcarenotifier.ui.pet.PetSelectionActivity;
import com.example.petcarenotifier.R;
import com.example.petcarenotifier.data.dao.UserDao;
import com.example.petcarenotifier.data.db.AppDatabase;
import com.example.petcarenotifier.data.entity.UserEntity;

public class LoginActivity extends AppCompatActivity {
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userDao = AppDatabase.getInstance(this).userDao();

        EditText etUsername = findViewById(R.id.Username);
        EditText etPassword = findViewById(R.id.Password);

        findViewById(R.id.btnLogin).setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();


            UserEntity user = userDao.login(username, password);
            if (user != null) {
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, PetSelectionActivity.class));
                getSharedPreferences("petcare_prefs", MODE_PRIVATE)
                        .edit()
                        .putString("logged_in_username", user.username)
                        .putInt("logged_in_user_id", user.id)
                        .apply();

            } else {
                Toast.makeText(this, R.string.invalid_credentials, Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btnSignUp).setOnClickListener(v -> {
            startActivity(new Intent(this, SignupActivity.class));
        });

    }
}
