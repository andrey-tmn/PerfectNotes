package com.dorbugstudio.perfectnotes.ui.details;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import com.dorbugstudio.perfectnotes.R;
import com.dorbugstudio.perfectnotes.domain.Note;
import com.dorbugstudio.perfectnotes.ui.NavigationDrawable;
import com.dorbugstudio.perfectnotes.ui.list.NotesListFragment;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class NoteEditFragment extends Fragment {

    public static final String TAG = "NoteEditFragment";
    private static final String ARG_NOTE_ID = "ARG_NOTE_ID";

    public NoteEditFragment() {
        super(R.layout.fragment_note_edit);
    }

    public static NoteEditFragment newInstance(int noteId) {

        Bundle args = new Bundle();
        args.putInt(ARG_NOTE_ID, noteId);

        NoteEditFragment fragment = new NoteEditFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);

        TextInputEditText editBody = view.findViewById(R.id.note_body_edit);

        Note currentNote = getCurrentNote();
        if (currentNote == null) {
            return;
        }

        editBody.setText(currentNote.getNoteBody());
        toolbar.setTitle(currentNote.getTitle());

        if (requireActivity() instanceof NavigationDrawable) {
            ((NavigationDrawable) requireActivity()).setAppBar(toolbar);
        }

        toolbar.setOnMenuItemClickListener(new androidx.appcompat.widget.Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_save) {
                    Toast.makeText(requireContext(), getString(R.string.action_save), Toast.LENGTH_SHORT).show();
                    return true;
                }

                return false;
            }
        });
    }

    private Note getCurrentNote() {
        Note note = null;

        Bundle arguments = getArguments();
        if ((arguments != null) && (arguments.containsKey(ARG_NOTE_ID))) {
            int noteId = arguments.getInt(ARG_NOTE_ID);
            if (noteId > 0) {
                note = NotesRepositoryImpl.getInstance().getNoteById(noteId);
            }
        }

        return note;
    }
}
