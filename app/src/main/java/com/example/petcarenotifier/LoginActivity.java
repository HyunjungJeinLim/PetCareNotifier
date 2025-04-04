package com.example.petcarenotifier;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.btnLogin).setOnClickListener(v -> {
            String username = ((android.widget.EditText)findViewById(R.id.etUsername)).getText().toString();
            String password = ((android.widget.EditText)findViewById(R.id.etPassword)).getText().toString();

            if (UserData.users.containsKey(username) && UserData.users.get(username).equals(password)) {
                startActivity(new Intent(this, PetSelectionActivity.class));
            } else {
                Toast.makeText(this, R.string.invalid_credentials, Toast.LENGTH_SHORT).show();
            }
        });
    }
}