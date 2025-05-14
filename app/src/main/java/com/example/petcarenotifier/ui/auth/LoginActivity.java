package com.example.petcarenotifier.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.petcarenotifier.R;
import com.example.petcarenotifier.data.dao.UserDao;
import com.example.petcarenotifier.data.db.AppDatabase;
import com.example.petcarenotifier.data.entity.UserEntity;
import com.example.petcarenotifier.ui.pet.PetSelectionActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.Task;
import com.google.api.services.calendar.CalendarScopes;

/** @noinspection ALL*/
public class LoginActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 1000;

    private UserDao userDao;
    private GoogleSignInClient mGoogleSignInClient;
    public static GoogleSignInAccount signedInAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userDao = AppDatabase.getInstance(this).userDao();
        EditText etUsername = findViewById(R.id.Username);
        EditText etPassword = findViewById(R.id.Password);

        // ✅ Configure Google Sign-In with Calendar scope
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(new Scope(CalendarScopes.CALENDAR))
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // 👉 Local login button
        findViewById(R.id.btnLogin).setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            UserEntity user = userDao.login(username, password);
            if (user != null) {
                getSharedPreferences("petcare_prefs", MODE_PRIVATE)
                        .edit()
                        .putString("logged_in_username", user.username)
                        .putInt("logged_in_user_id", user.id)
                        .putBoolean("google_signed_in", false)
                        .apply();

                startActivity(new Intent(this, PetSelectionActivity.class));
                finish();
            } else {
                Toast.makeText(this, R.string.invalid_credentials, Toast.LENGTH_SHORT).show();
            }
        });

        // 👉 Google Sign-In button
        findViewById(R.id.btnGoogleSignIn).setOnClickListener(v -> {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });

        // 👉 Sign-up navigation
        findViewById(R.id.btnSignUp).setOnClickListener(v -> {
            startActivity(new Intent(this, SignupActivity.class));
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            if (task.isSuccessful()) {
                signedInAccount = task.getResult();
                String email = signedInAccount.getEmail();
                String name = signedInAccount.getDisplayName();

                // ✅ Check if user exists in Room DB
                UserEntity existingUser = userDao.getByUsername(email);
                if (existingUser == null) {
                    UserEntity newUser = new UserEntity();
                    newUser.username = email;
                    newUser.password = "";
                    newUser.name = name != null ? name : "Google User";
                    newUser.role = "user";
                    long newId = userDao.insert(newUser);
                    existingUser = userDao.getByUsername(email);
                }

                // ✅ Save login state and mark Google sign-in
                getSharedPreferences("petcare_prefs", MODE_PRIVATE)
                        .edit()
                        .putString("logged_in_username", existingUser.username)
                        .putInt("logged_in_user_id", existingUser.id)
                        .putBoolean("google_signed_in", true)
                        .apply();

                startActivity(new Intent(this, PetSelectionActivity.class));
                finish();

            } else {
                Log.e("LoginActivity", "Google Sign-In failed", task.getException());
                Toast.makeText(this, "Google Sign-In failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
