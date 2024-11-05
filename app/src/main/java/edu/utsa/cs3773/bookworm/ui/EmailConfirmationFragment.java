package edu.utsa.cs3773.bookworm.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import edu.utsa.cs3773.bookworm.MainActivity;
import edu.utsa.cs3773.bookworm.R;

public class EmailConfirmationFragment extends Fragment implements View.OnClickListener {

    private int from = 0;
    private NavController navController;
    private Toolbar toolbar;

    public EmailConfirmationFragment() {
        super(R.layout.fragment_email_confirmation);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        from = getArguments().getInt("from");
        navController = Navigation.findNavController(view);
        toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.GONE);
        ((TextView)view.findViewById(R.id.email_confirmation_body)).setText("\nIt was sent from " + getResources().getString(R.string.accounts_email_address));
        view.findViewById(R.id.email_confirmation_ok_button).setOnClickListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (from == R.layout.fragment_change_email) toolbar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        if (from == R.layout.fragment_change_email) navController.navigateUp();
        //else if (from == R.layout.fragment_setup_email) {
            //handle OK button press
        //}
    }
}
