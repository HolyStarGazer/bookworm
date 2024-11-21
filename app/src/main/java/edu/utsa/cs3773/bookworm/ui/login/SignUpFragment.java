package edu.utsa.cs3773.bookworm.ui.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.Objects;

import edu.utsa.cs3773.bookworm.LoginActivity;
import edu.utsa.cs3773.bookworm.R;

public class SignUpFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        EditText signupUsernameEditText = view.findViewById(R.id.signupUsernameEditText);
        EditText signupPasswordEditText = view.findViewById(R.id.signupPasswordEditText);
        EditText signupConfirmPasswordEditText = view.findViewById(R.id.signupPasswordConfirmEditText);
        Button signupButton = view.findViewById(R.id.signupButton);
        TextView loginLink = view.findViewById(R.id.signUpLoginLink);

        TextView requirementsTotal = view.findViewById(R.id.passwordRequirements);
        TextView requirementLength = view.findViewById(R.id.passwordRequirementsLength);
        TextView requirementUppercase = view.findViewById(R.id.passwordRequirementsUppercase);
        TextView requirementLowercase = view.findViewById(R.id.passwordRequirementsLowercase);
        TextView requirementDigit = view.findViewById(R.id.passwordRequirementsDigit);
        TextView requirementSymbol = view.findViewById(R.id.passwordRequirementsSymbol);

        signupPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = s.toString();

                boolean lengthValid = password.length() >= 8;
                boolean uppercaseValid = password.matches(".*[A-Z].*");
                boolean lowercaseValid = password.matches(".*[a-z].*");
                boolean digitValid = password.matches(".*[0-9].*");
                boolean symbolValid = password.matches(".*[!@#$%^&*].*");
                boolean allValid = lengthValid && uppercaseValid && lowercaseValid && digitValid && symbolValid;

                updateRequirements(requirementLength, lengthValid);
                updateRequirements(requirementUppercase, uppercaseValid);
                updateRequirements(requirementLowercase, lowercaseValid);
                updateRequirements(requirementDigit, digitValid);
                updateRequirements(requirementSymbol, symbolValid);
                updateRequirements(requirementsTotal, allValid);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        signupButton.setOnClickListener(v -> {
            String username = signupUsernameEditText.getText().toString();
            String password = signupPasswordEditText.getText().toString();
            String confirmPassword = signupConfirmPasswordEditText.getText().toString();

            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(getContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show();
            } else {
                if (!password.equals(confirmPassword)) {
                    Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                } else {
                    if (!checkPassword(password)) {
                        Toast.makeText(getContext(), "Password does not meet requirements", Toast.LENGTH_SHORT).show();
                    } else {
                        // TODO - Handle Database logic
                        Toast.makeText(getContext(), "Account created. Redirecting you to login page", Toast.LENGTH_SHORT).show();
                        ((LoginActivity) getActivity()).showLoginFragment();
                    }
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
        if (password.length() < 8) {

            return false;
        }

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

    private void updateRequirements(TextView textView, boolean isValid) {
        if (isValid) {
            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.checkmark, 0, 0, 0);
            textView.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.holo_green_dark));
        } else {
            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.cross, 0, 0, 0);
            textView.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.holo_red_dark));
        }
    }
}