package edu.utsa.cs3773.bookworm.ui;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import edu.utsa.cs3773.bookworm.R;

public class ChangeEmailFragment extends Fragment implements View.OnClickListener {

    Toolbar toolbar;

    public ChangeEmailFragment() {
        super(R.layout.fragment_change_email);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(GONE);
        view.findViewById(R.id.change_email_up_button).setOnClickListener(this);
        view.findViewById(R.id.change_email_submit_button).setOnClickListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        toolbar.setVisibility(VISIBLE);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.change_email_up_button) {
            Navigation.findNavController(view).navigateUp();
        }
        //Handle button presses
    }
}
