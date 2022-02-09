package com.dorbugstudio.perfectnotes.ui.details;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import com.dorbugstudio.perfectnotes.R;
import com.dorbugstudio.perfectnotes.domain.Note;
import com.dorbugstudio.perfectnotes.ui.list.NotesListFragment;

import java.text.SimpleDateFormat;

public class NoteDetailsFragment extends Fragment {

    private static final String ARG_NOTE = "ARG_NOTE";
    private TextView noteTitleTextView;
    private TextView noteCreatedDateTextView;
    private TextView noteBodyTextView;

    public NoteDetailsFragment() {
        super(R.layout.fragment_note_details);
    }

    public static NoteDetailsFragment newInstance(Note note) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_NOTE, note);

        NoteDetailsFragment fragment = new NoteDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        noteTitleTextView = view.findViewById(R.id.title);
        noteCreatedDateTextView = view.findViewById(R.id.note_created_date);
        noteBodyTextView = view.findViewById(R.id.note_body);

        getParentFragmentManager()
                .setFragmentResultListener(NotesListFragment.NOTE_SELECTED, getViewLifecycleOwner(), new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                        Note note = (Note) result.getSerializable(NotesListFragment.SELECTED_NOTE_BUNDLE);

                        showNoteDetailsInfo(note);
                    }
                });

        Bundle arguments = getArguments();

        if ((arguments != null) && (arguments.containsKey(ARG_NOTE))) {
            Note note = (Note) arguments.getSerializable(ARG_NOTE);
            showNoteDetailsInfo(note);
        }
    }

    private void showNoteDetailsInfo(Note note) {
        noteTitleTextView.setText(note.getTitle());
        noteCreatedDateTextView.setText(new SimpleDateFormat("dd-MM-yyyy").format(note.getDate()));
        noteBodyTextView.setText(note.getNoteBody());
    }
}

