package com.dorbugstudio.perfectnotes.ui.list;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.dorbugstudio.perfectnotes.R;

public class NoteDeleteDialogFragment extends DialogFragment {

    public static final String TAG = "NoteDeleteDialogFragment";
    public static final String REQUEST_KEY = "NoteDeleteDialogFragment_REQUEST_KEY";
    public static final String ARG_NOTE_ID = "ARG_NOTE_ID";

    private static final String ARG_TITLE = "ARG_TITLE";
    private static final String ARG_MESSAGE = "ARG_MESSAGE";


    public static NoteDeleteDialogFragment newInstance(String title, String message, int noteId) {

        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_MESSAGE, message);
        args.putInt(ARG_NOTE_ID, noteId);

        NoteDeleteDialogFragment fragment = new NoteDeleteDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        DialogInterface.OnClickListener yesButtonClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Bundle bundle = new Bundle();
                bundle.putInt(ARG_NOTE_ID, requireArguments().getInt(ARG_NOTE_ID));

                getParentFragmentManager()
                        .setFragmentResult(REQUEST_KEY, bundle);
            }
        };

        return new AlertDialog.Builder(requireContext())
                .setTitle(requireArguments().getString(ARG_TITLE))
                .setMessage(requireArguments().getString(ARG_MESSAGE))
                .setPositiveButton(getString(R.string.yes_word), yesButtonClickListener)
                .setNeutralButton(getString(R.string.no_word), null)
                .create();
    }
}
