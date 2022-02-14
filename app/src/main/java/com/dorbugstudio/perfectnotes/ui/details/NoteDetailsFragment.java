package com.dorbugstudio.perfectnotes.ui.details;

import static com.google.android.material.timepicker.MaterialTimePicker.INPUT_MODE_KEYBOARD;

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
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NoteDetailsFragment extends Fragment implements MaterialPickerOnPositiveButtonClickListener<Long> {

    private static final String ARG_NOTE_ID = "ARG_NOTE_ID";

    private TextView noteTitleTextView;
    private TextView noteCreatedDateTextView;
    private TextView noteBodyTextView;

    public NoteDetailsFragment() {
        super(R.layout.fragment_note_details);
    }

    public static NoteDetailsFragment newInstance(int noteId) {

        Bundle args = new Bundle();
        args.putInt(ARG_NOTE_ID, noteId);

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
                .setOnClickListener(buttonView -> showDatePickerDialog(null));

        getParentFragmentManager()
                .setFragmentResultListener(NotesListFragment.NOTE_SELECTED, getViewLifecycleOwner(), new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                        int noteId = result.getInt(NotesListFragment.SELECTED_NOTE_ID_BUNDLE);
                        Note note = NotesRepositoryImpl.getInstance().getNoteById(noteId);
                        showNoteDetailsInfo(note);
                    }
                });

        showNoteDetailsInfo(getCurrentNote());
    }

    private void showDatePickerDialog(Date date) {
        final String datePickerDialogFragmentManagerTag = "DatePickerDialog";
        if (getParentFragmentManager().findFragmentByTag(datePickerDialogFragmentManagerTag) != null) {
            return;
        }

        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText(R.string.change_date_text);
        if (date != null) {
            builder.setSelection(date.getTime());
        } else {
            Note note = getCurrentNote();
            if (note != null) {
                builder.setSelection(note.getDate().getTime());
            }
        }

        MaterialDatePicker<Long> picker = builder.build();
        picker.addOnPositiveButtonClickListener(this);
        picker.show(getParentFragmentManager(), datePickerDialogFragmentManagerTag);
    }

    private Note getCurrentNote() {
        Note note = null;

        Bundle arguments = getArguments();
        if ((arguments != null) && (arguments.containsKey(ARG_NOTE_ID))) {
            int noteId = arguments.getInt(ARG_NOTE_ID);
            note = NotesRepositoryImpl.getInstance().getNoteById(noteId);
        }

        return note;
    }

    private void showNoteDetailsInfo(Note note) {
        if (note == null) {
            return;
        }

        noteTitleTextView.setText(note.getTitle());
        noteCreatedDateTextView.setText(new SimpleDateFormat("HH:mm dd-MM-yyyy").format(note.getDate()));
        noteBodyTextView.setText(note.getNoteBody());
    }

    @Override
    public void onPositiveButtonClick(Long selectedDate) {
        Date date = new Date(selectedDate);
        Date oldDate = getCurrentNote().getDate();

        MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setInputMode(INPUT_MODE_KEYBOARD)
                .setHour((int) (oldDate.getTime() % 86400000) / 3600000)
                .setMinute((int) (oldDate.getTime() % 86400000) / 60000)
                .build();
        timePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                String dateString = new SimpleDateFormat("yyyy-MM-dd").format(selectedDate)
                        + " " + String.format("%02d:%02d", timePicker.getHour(), timePicker.getMinute());
                Date dateWithTime;
                try {
                    dateWithTime = dateFormat.parse(dateString);
                } catch (ParseException e) {
                    dateWithTime = new Date();
                }

                NotesRepositoryImpl.getInstance()
                        .changeNoteCreatedDate(getCurrentNote().getId(), dateWithTime);
                showNoteDetailsInfo(getCurrentNote());
            }
        });
        timePicker.addOnCancelListener(view -> showDatePickerDialog(date));
        timePicker.addOnNegativeButtonClickListener(view -> showDatePickerDialog(date));
        timePicker.show(getParentFragmentManager(), "TimePickerDialog");
    }
}

