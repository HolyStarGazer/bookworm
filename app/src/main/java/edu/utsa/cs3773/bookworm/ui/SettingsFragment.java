package edu.utsa.cs3773.bookworm.ui;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import edu.utsa.cs3773.bookworm.R;

public class SettingsFragment extends Fragment implements View.OnClickListener {

    MenuItem navSettings;

    public SettingsFragment() {
        super(R.layout.fragment_settings);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        navSettings = ((Toolbar)getActivity().findViewById(R.id.toolbar)).getMenu().findItem(R.id.nav_settings);
        navSettings.setVisible(false);
        ((Spinner)getActivity().findViewById(R.id.settings_length_unit_spinner)).setSelection(1);
        //Populate fields with previously applied settings
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        navSettings.setVisible(true);
    }

    @Override
    public void onClick(View view) {
        //Handle button presses
    }
}
