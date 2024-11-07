package edu.utsa.cs3773.bookworm.ui.login;

import android.os.Bundle;
import android.util.Patterns;
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

public class ForgotPasswordFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        EditText emailEditText = view.findViewById(R.id.emailEditText);
        Button resetPasswordButton = view.findViewById(R.id.resetPasswordButton);
        TextView loginLink = view.findViewById(R.id.forgotPasswordLoginLink);

        resetPasswordButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            if (validateEmail(email)) {
                resetPassword(email);
            } else {
                Toast.makeText(getContext(), "Please enter valid email address", Toast.LENGTH_SHORT).show();
            }
        });

        loginLink.setOnClickListener(v -> {
            ((LoginActivity) getActivity()).showLoginFragment();
        });

        return view;
    }

    private boolean validateEmail(String email) {
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void resetPassword(String email) {
        // FIXME - Handle sending password reset email

        // FIXME - Handle logic for database after email confirmation

        Toast.makeText(getContext(), "Reset password email sent to " + email, Toast.LENGTH_LONG).show();
    }
}