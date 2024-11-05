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

public class LoginFragment extends Fragment {

    private EditText usernameEditText;
    private EditText passwordEditText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        usernameEditText = view.findViewById(R.id.usernameEditText);
        passwordEditText = view.findViewById(R.id.passwordEditText);
        Button loginButton = view.findViewById(R.id.loginButton);
        TextView signupLink = view.findViewById(R.id.signUpLink);
        TextView forgotPasswordLink = view.findViewById(R.id.forgotPasswordLink);

        loginButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            login(username, password);
        });

        signupLink.setOnClickListener(v -> navigateToSignUp());
        forgotPasswordLink.setOnClickListener(v -> navigateToForgotPassword());

        return view;
    }

    private void login(String username, String password) {
        if (!username.isEmpty() && !password.isEmpty()) {
            ((LoginActivity) getActivity()).login(username, password);
        } else {
            Toast.makeText(getContext(), "Please enter both username and password", Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateToSignUp() {
        ((LoginActivity) getActivity()).showSignUpFragment();
    }

    private void navigateToForgotPassword() {
        ((LoginActivity) getActivity()).showForgotPasswordFragment();
    }
}