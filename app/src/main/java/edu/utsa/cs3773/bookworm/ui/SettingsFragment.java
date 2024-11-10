package edu.utsa.cs3773.bookworm.ui;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import edu.utsa.cs3773.bookworm.MainActivity;
import edu.utsa.cs3773.bookworm.R;
import edu.utsa.cs3773.bookworm.model.User;

public class SettingsFragment extends Fragment implements View.OnClickListener {

    private NavController navController;
    private FragmentManager fragmentManager;
    private boolean optionsVisible;
    private int bottomNavigationVisibility;
    private MenuItem navSearch, navFavorites, navSettings;
    private View bottomNavigation;
    private Button emailButton;
    private User user;

    public SettingsFragment() {
        super(R.layout.fragment_settings);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        navController = Navigation.findNavController(view);
        fragmentManager = getParentFragmentManager();
        optionsVisible = getArguments().getBoolean("optionsVisible");
        bottomNavigationVisibility = getArguments().getInt("bottomNavigationVisibility");
        navSearch = ((Toolbar)getActivity().findViewById(R.id.toolbar)).getMenu().findItem(R.id.nav_search);
        navSearch.setVisible(false);
        navFavorites = ((Toolbar)getActivity().findViewById(R.id.toolbar)).getMenu().findItem(R.id.nav_favorites);
        navFavorites.setVisible(false);
        navSettings = ((Toolbar)getActivity().findViewById(R.id.toolbar)).getMenu().findItem(R.id.nav_settings);
        navSettings.setVisible(false);
        bottomNavigation = getActivity().findViewById(R.id.bottom_navigation);
        bottomNavigation.setVisibility(View.GONE);
        emailButton = view.findViewById(R.id.settings_email_button);
        emailButton.setOnClickListener(this);
        view.findViewById(R.id.settings_password_button).setOnClickListener(this);
        view.findViewById(R.id.settings_prefs_apply_button).setOnClickListener(this);
        view.findViewById(R.id.settings_prefs_cancel_button).setOnClickListener(this);
        user = ((MainActivity)getActivity()).getLoggedInUser();
        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            TextView field = view.findViewById(R.id.settings_current_email);
            field.setText("\t\t\tCurrently: " + user.getEmail());
            field.setVisibility(View.VISIBLE);
            field = view.findViewById(R.id.settings_email_confirmation);
            //populate field with user's current confirmation status, retrieved from the database
            field.setVisibility(View.VISIBLE);
            emailButton.setText("Change");
        }
        ((Spinner)view.findViewById(R.id.settings_length_unit_spinner)).setSelection(1);
        //populate fields with previously applied settings, retrieved from user object
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        navSearch.setVisible(optionsVisible);
        navFavorites.setVisible(optionsVisible);
        navSettings.setVisible(true);
        bottomNavigation.setVisibility(bottomNavigationVisibility);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.settings_email_button) {
            fragmentManager.setFragmentResultListener("updateEmail", this, new FragmentResultListener() {
                @Override
                public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                    if (result.getBoolean("changed") ) {
                        TextView field = getView().findViewById(R.id.settings_current_email);
                        field.setText("\t\t\tCurrently: " + user.getEmail());
                        field.setVisibility(View.VISIBLE);
                        field = getView().findViewById(R.id.settings_email_confirmation);
                        //populate field with user's current confirmation status, retrieved from the database
                        field.setVisibility(View.VISIBLE);
                        emailButton.setText("Change");
                    }
                }
            });
            navController.navigate(R.id.nav_change_email);
        }
        else if (view.getId() == R.id.settings_password_button) navController.navigate(R.id.nav_change_password);
        else if (view.getId() == R.id.settings_prefs_apply_button) {
            //update backend
        }
        else if (view.getId() == R.id.settings_prefs_cancel_button) {
            //populate fields with previously applied settings, retrieved from user object
        }
    }
}