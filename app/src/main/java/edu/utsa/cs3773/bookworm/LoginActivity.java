package edu.utsa.cs3773.bookworm;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import edu.utsa.cs3773.bookworm.ui.login.ForgotPasswordFragment;
import edu.utsa.cs3773.bookworm.ui.login.LoginFragment;
import edu.utsa.cs3773.bookworm.ui.login.SignUpFragment;

public class LoginActivity extends AppCompatActivity {
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fragmentManager = getSupportFragmentManager();

        // Load the LoginFragment by default
        if (savedInstanceState == null) {
            showLoginFragment();
        }
    }

    public void showLoginFragment() {
        fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, new LoginFragment())
                .commit();
    }

    public void showSignUpFragment() {
        fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, new SignUpFragment())
                .addToBackStack(null)
                .commit();
    }

    public void showForgotPasswordFragment() {
        fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, new ForgotPasswordFragment())
                .addToBackStack(null)
                .commit();
    }

    public void login(String username, String password) {
        if (isLoginValid(username, password)) {
            Toast.makeText(this, "Welcome, " + username + "!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isLoginValid(String username, String password) {
        // TODO - Handle database logic
        // Query database to check if user exists
        // Fetch password and logic check

        return username.equals("devtest") && password.equals("devtest");
    }
}