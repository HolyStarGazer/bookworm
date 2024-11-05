package edu.utsa.cs3773.bookworm.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

public class ConfirmDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {

    private FragmentManager fragmentManager;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        fragmentManager = getParentFragmentManager();
        return new AlertDialog.Builder(getActivity())
            .setMessage(getArguments().getString("message"))
            .setPositiveButton("Yes", this)
            .setNegativeButton("No", this)
            .create();
    }

    @Override
    public void onClick(DialogInterface dialog, int id) {
        if (id == AlertDialog.BUTTON_POSITIVE) {
            Bundle result = new Bundle();
            result.putBoolean("confirm", true);
            fragmentManager.setFragmentResult(this.getTag(), result);
        }
    }
}
