package edu.utsa.cs3773.bookworm;

import android.os.Bundle;
import android.util.Log;
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
            if (!email.isEmpty()) {
                resetPassword(email);
            } else {
                Toast.makeText(getContext(), "Please enter your email", Toast.LENGTH_SHORT).show();
            }
        });

        loginLink.setOnClickListener(v -> {
            ((LoginActivity) getActivity()).showLoginFragment();
        });

        return view;
    }

    private void resetPassword(String email) {
        // Logic for sending a password reset email

        Toast.makeText(getContext(), "Reset password email sent to " + email, Toast.LENGTH_LONG).show();
    }
}