package edu.utsa.cs3773.bookworm.ui.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

            // FIXME - Handle Database logic

        });

        loginLink.setOnClickListener(v -> {
            ((LoginActivity) getActivity()).showLoginFragment();
        });

        return view;
    }
}