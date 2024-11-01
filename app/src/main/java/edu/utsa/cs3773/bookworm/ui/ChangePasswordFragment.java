package edu.utsa.cs3773.bookworm.ui;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import edu.utsa.cs3773.bookworm.R;

public class ChangePasswordFragment extends Fragment implements View.OnClickListener {

    NavController navController;
    Toolbar toolbar;

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
            //if invalid input
            //  update fields
            //else {
                //update backend
                navController.navigateUp();
            //}
        }
    }
}
