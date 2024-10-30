package edu.utsa.cs3773.bookworm.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import edu.utsa.cs3773.bookworm.R;

public class SettingsFragment extends Fragment {

    String prevToolbarTitle;
    @ColorInt int prevToolbarColor;

    public SettingsFragment() {
        super(R.layout.fragment_settings);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
//        prevToolbarTitle = (String)toolbar.getTitle();
//        prevToolbarColor = toolbar.getSolidColor();
//        toolbar.setTitle("Settings");
//        toolbar.setBackgroundColor(0);
        toolbar.getMenu().findItem(R.id.nav_settings).setVisible(false);
    }
}
