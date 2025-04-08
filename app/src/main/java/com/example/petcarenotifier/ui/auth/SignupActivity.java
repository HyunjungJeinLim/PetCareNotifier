package com.example.petcarenotifier.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.petcarenotifier.R;
import com.example.petcarenotifier.data.dao.UserDao;
import com.example.petcarenotifier.data.db.AppDatabase;
import com.example.petcarenotifier.data.entity.UserEntity;

public class SignupActivity extends AppCompatActivity {
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        userDao = AppDatabase.getInstance(this).userDao();

        EditText etUsername = findViewById(R.id.etUsername);
        EditText etPassword = findViewById(R.id.etPassword);

        findViewById(R.id.btnCreateAccount).setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter a username and password", Toast.LENGTH_SHORT).show();
                return;
            }

            if (userDao.getUser(username) == null) {
                userDao.insert(new UserEntity(username, password));
                Toast.makeText(this, "Account created successfully. Please log in.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btnGoToLogin).setOnClickListener(v ->
                startActivity(new Intent(this, LoginActivity.class))
        );
    }
}
