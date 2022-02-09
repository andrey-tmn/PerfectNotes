package com.dorbugstudio.perfectnotes.ui.details;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import com.dorbugstudio.perfectnotes.R;
import com.dorbugstudio.perfectnotes.domain.Note;
import com.dorbugstudio.perfectnotes.ui.list.NotesListFragment;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NoteDetailsFragment extends Fragment implements
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

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

        view.findViewById(R.id.change_date_button)
                .setOnClickListener(buttonView -> showDatePickerDialog());

        getParentFragmentManager()
                .setFragmentResultListener(NotesListFragment.NOTE_SELECTED, getViewLifecycleOwner(), new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                        Note note = (Note) result.getSerializable(NotesListFragment.SELECTED_NOTE_BUNDLE);

                        showNoteDetailsInfo(note);
                    }
                });

        showNoteDetailsInfo(getCurrentNote());
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this);
        datePickerDialog.setTitle(getString(R.string.datepicker_header));
        datePickerDialog.show(getParentFragmentManager(), "DatePickerDialog");
    }

    private Note getCurrentNote() {
        Note note = null;

        Bundle arguments = getArguments();
        if ((arguments != null) && (arguments.containsKey(ARG_NOTE))) {
            note = (Note) arguments.getSerializable(ARG_NOTE);

        }

        return note;
    }

    private void showNoteDetailsInfo(Note note) {
        if (note == null) {
            return;
        }

        noteTitleTextView.setText(note.getTitle());
        noteCreatedDateTextView.setText(new SimpleDateFormat("dd-MM-yyyy").format(note.getDate()));
        noteBodyTextView.setText(note.getNoteBody());
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(this, true);
        //timePickerDialog.setThemeDark(false);
        //timePickerDialog.showYearPickerFirst(false);
        timePickerDialog.setTitle("Time Picker");

        timePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                showDatePickerDialog();
            }
        });

        timePickerDialog.show(getParentFragmentManager(), "TimePickerDialog");
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        Date date = new Date();
        getCurrentNote().setDate(date);
    }
}

