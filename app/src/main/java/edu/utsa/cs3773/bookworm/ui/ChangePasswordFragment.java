package edu.utsa.cs3773.bookworm.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import edu.utsa.cs3773.bookworm.R;

/**
 * Fragment that displays the change password page.
 *
 * @author Gavin C Wilson
 * @version %I% %G%
 * @see "res/layout/fragment_change_password.xml"
 */
public class ChangePasswordFragment extends Fragment implements View.OnClickListener {

    private NavController navController;
    private Toolbar toolbar;

    /**
     * Class constructor.
     * Uses the resource ID of the change password page layout.
     */
    public ChangePasswordFragment() {
        super(R.layout.fragment_change_password);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        navController = Navigation.findNavController(view);
        toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.GONE);
        view.findViewById(R.id.change_password_up_button).setOnClickListener(this);
        view.findViewById(R.id.change_password_submit_button).setOnClickListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        toolbar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.change_password_up_button) navController.navigateUp();
        else if (view.getId() == R.id.change_password_submit_button) {
            boolean valid = true;
            EditText input = getView().findViewById(R.id.change_password_old);
            TextView feedback = getView().findViewById(R.id.change_password_old_feedback);
            //if (correct old password) feedback.setVisibility(View.INVISIBLE);
            //else {
            //  input.setText("");
            //  feedback.setVisibility(View.VISIBLE);
            //  valid = false;
            //}
            input = getView().findViewById(R.id.change_password_new);
            EditText confirmation = getView().findViewById(R.id.change_password_confirm);
            feedback = getView().findViewById(R.id.change_password_new_feedback);
            if (input.getText() != null && checkPasswordRequirements(input.getText().toString())) feedback.setVisibility(View.INVISIBLE);
            else {
                input.setText("");
                confirmation.setText("");
                feedback.setVisibility(View.VISIBLE);
                valid = false;
            }
            feedback = getView().findViewById(R.id.change_password_confirm_feedback);
            if (confirmation.getText() != null && input.getText() != null && confirmation.getText().toString().equals(input.getText().toString())) feedback.setVisibility(View.INVISIBLE);
            else {
                input.setText("");
                confirmation.setText("");
                feedback.setVisibility(View.VISIBLE);
                valid = false;
            }
            if (valid) {
                //update backend
                navController.navigateUp();
            }
        }
    }

    /**
     * Checks whether the provided string meets the service's password requirements.
     *
     * @param s the string to be checked
     * @return  a boolean indicating whether <code>s</code> would be valid as a password
     */
    private boolean checkPasswordRequirements(String s) {
        if (s.length() < 8) return false;
        boolean containsLowercase = false;
        boolean containsUppercase = false;
        boolean containsNumber = false;
        boolean containsSpecial = false;
        for (int i = 0; i < s.length(); ++i) {
            if (s.charAt(i) < ' ' || s.charAt(i) > '~') return false;
            if (s.charAt(i) >= 'a' && s.charAt(i) <= 'z') containsLowercase = true;
            else if (s.charAt(i) >= 'A' && s.charAt(i) <= 'Z') containsUppercase = true;
            else if (s.charAt(i) >= '0' && s.charAt(i) <= '9') containsNumber = true;
            else containsSpecial = true;
        }
        return containsLowercase && containsUppercase && containsNumber && containsSpecial;
    }
}