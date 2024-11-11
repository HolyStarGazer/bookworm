package edu.utsa.cs3773.bookworm.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import edu.utsa.cs3773.bookworm.MainActivity;
import edu.utsa.cs3773.bookworm.R;

/**
 * Fragment that displays the change email page.
 *
 * @author Gavin C Wilson
 * @version %I% %G%
 * @see "res/layout/fragment_change_email.xml"
 */
public class ChangeEmailFragment extends Fragment implements View.OnClickListener {

    private NavController navController;
    private FragmentManager fragmentManager;
    private Toolbar toolbar;

    /**
     * Class constructor.
     * Uses the resource ID of the change email page layout.
     */
    public ChangeEmailFragment() {
        super(R.layout.fragment_change_email);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        navController = Navigation.findNavController(view);
        fragmentManager = getParentFragmentManager();
        toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.GONE);
        view.findViewById(R.id.change_email_up_button).setOnClickListener(this);
        view.findViewById(R.id.change_email_submit_button).setOnClickListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        toolbar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.change_email_up_button) navController.navigateUp();
        else if (view.getId() == R.id.change_email_submit_button) {
            EditText input = getView().findViewById(R.id.change_email_new);
            //if invalid input
            //  update feedback field
            //else {
            //update backend
            ((MainActivity)getActivity()).getLoggedInUser().setEmail(input.getText().toString());
            Bundle result = new Bundle();
            result.putBoolean("changed", true);
            fragmentManager.setFragmentResult("updateEmail", result);
            Bundle args = new Bundle();
            args.putInt("from", R.layout.fragment_change_email);
            navController.navigate(R.id.nav_email_confirmation, args, new NavOptions.Builder().setPopUpTo(R.id.nav_settings, false, false).build());
            //}
        }
    }
}