package edu.utsa.cs3773.bookworm.ui;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import edu.utsa.cs3773.bookworm.R;

public class SettingsFragment extends Fragment implements View.OnClickListener {

    NavController navController;
    MenuItem navSettings;

    public SettingsFragment() {
        super(R.layout.fragment_settings);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        navController = Navigation.findNavController(view);
        navSettings = ((Toolbar)getActivity().findViewById(R.id.toolbar)).getMenu().findItem(R.id.nav_settings);
        navSettings.setVisible(false);
        view.findViewById(R.id.settings_email_button).setOnClickListener(this);
        view.findViewById(R.id.settings_password_button).setOnClickListener(this);
        ((Spinner)view.findViewById(R.id.settings_length_unit_spinner)).setSelection(1);
        view.findViewById(R.id.settings_prefs_apply_button).setOnClickListener(this);
        view.findViewById(R.id.settings_prefs_cancel_button).setOnClickListener(this);
        //Populate fields with previously applied settings
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        navSettings.setVisible(true);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.settings_email_button) navController.navigate(R.id.nav_change_email);
        else if (view.getId() == R.id.settings_password_button) navController.navigate(R.id.nav_change_password);
        else if (view.getId() == R.id.settings_prefs_apply_button) {
            //update backend
        }
        else if (view.getId() == R.id.settings_prefs_cancel_button) {
            //update backend
        }
    }
}
