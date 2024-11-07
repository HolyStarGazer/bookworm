package edu.utsa.cs3773.bookworm.ui.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import edu.utsa.cs3773.bookworm.LoginActivity;
import edu.utsa.cs3773.bookworm.R;

public class SignUpFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        EditText signupUsernameEditText = view.findViewById(R.id.signupUsernameEditText);
        EditText signupPasswordEditText = view.findViewById(R.id.signupPasswordEditText);
        Button signupButton = view.findViewById(R.id.signupButton);
        TextView loginLink = view.findViewById(R.id.signUpLoginLink);

        signupButton.setOnClickListener(v -> {
            String username = signupUsernameEditText.getText().toString().trim();
            String password = signupPasswordEditText.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(getContext(), "Please enter both username and password", Toast.LENGTH_SHORT).show();
            } else {
                if (!checkPassword(password)) {
                    Toast.makeText(getContext(), "Password does not meet requirements", Toast.LENGTH_SHORT).show();
                } else {
                    // TODO - Handle Database logic
                    Toast.makeText(getContext(), "Account created. Redirecting you to login page", Toast.LENGTH_SHORT).show();
                    ((LoginActivity) getActivity()).showLoginFragment();
                }
            }
        });

        loginLink.setOnClickListener(v -> {
            ((LoginActivity) getActivity()).showLoginFragment();
        });

        return view;
    }

    private boolean checkPassword(String password) {
        /* Password requirement
        - 8+ Characters
        - 1+ Uppercase letter
        - 1+ Lowercase letter
        - 1+ Number
        - 1+ Special character
         */

        // Check if password length is at least 8 characters
        if (password.length() < 8)
            return false;

        // Check for at least one uppercase letter
        if (!password.matches(".*[A-Z].*"))
            return false;

        // Check for at least one lowercase letter
        if (!password.matches(".*[a-z].*"))
            return false;

        // Check for at least one digit
        if (!password.matches(".*[0-9].*"))
            return false;

        // Check for at least one special character (non-alphanumeric)
        if (!password.matches(".*[!@#$%^&*].*"))
            return false;

        return true;
    }
}