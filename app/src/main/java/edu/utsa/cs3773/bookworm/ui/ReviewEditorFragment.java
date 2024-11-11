package edu.utsa.cs3773.bookworm.ui;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import edu.utsa.cs3773.bookworm.R;

/**
 * Fragment that displays the review editor page.
 *
 * @author Gavin C Wilson
 * @version %I% %G%
 * @see "res/layout/fragment_review_editor.xml"
 */
public class ReviewEditorFragment extends Fragment implements View.OnClickListener {

    private NavController navController;
    private FragmentManager fragmentManager;
    private Toolbar toolbar;

    /**
     * Class constructor.
     * Uses the resource ID of the review editor page layout.
     */
    public ReviewEditorFragment() {
        super(R.layout.fragment_review_editor);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        navController = Navigation.findNavController(view);
        fragmentManager = getParentFragmentManager();
        toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.GONE);
        view.findViewById(R.id.review_editor_save_button).setOnClickListener(this);
        view.findViewById(R.id.review_editor_cancel_button).setOnClickListener(this);
        //populate fields with appropriate data, retrieved from the database
    }

    @Override
    public void onStop() {
        super.onStop();
        toolbar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.review_editor_save_button) {
            //update backend
            Bundle result = new Bundle();
            result.putBoolean("changed", true);
            fragmentManager.setFragmentResult("updateReview", result);
        }
        navController.navigateUp();
    }
}