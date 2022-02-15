package com.dorbugstudio.perfectnotes.ui.list;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;

import com.dorbugstudio.perfectnotes.R;
import com.dorbugstudio.perfectnotes.domain.Note;
import com.dorbugstudio.perfectnotes.ui.details.NoteDetailsFragment;
import com.dorbugstudio.perfectnotes.ui.details.NotesRepositoryImpl;
import com.dorbugstudio.perfectnotes.ui.settings.SettingsFragment;

import java.util.List;


public class NotesListFragment extends Fragment implements NotesListView, FragmentResultListener {

    public static final String NOTE_SELECTED = "NOTE_SELECTED";
    public static final String SELECTED_NOTE_ID_BUNDLE = "SELECTED_NOTE_ID_BUNDLE";

    private LinearLayout listContainer;

    private NotesListPresenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new NotesListPresenter(this, NotesRepositoryImpl.getInstance());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notes_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listContainer = view.findViewById(R.id.note_list_container);

        presenter.requestNotes();

        getParentFragmentManager()
                .setFragmentResultListener(NotesListFragment.NOTE_SELECTED,
                        this, this);

        view.findViewById(R.id.about_button).setOnClickListener(v -> showAbout());

        view.findViewById(R.id.settings_button).setOnClickListener(v -> showSettings());
    }

    private void showSettings() {
        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.main_activity_container,
                        new SettingsFragment(),
                        SettingsFragment.TAG)
                .addToBackStack("SettingsFragment")
                .commitAllowingStateLoss();
    }

    private void showAbout() {
        Toast.makeText(getContext(), getString(R.string.about_button_descr), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showNotes(List<Note> notes) {
        for (Note note : notes) {
            View itemView = getLayoutInflater().inflate(R.layout.item_note, listContainer, false);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(SELECTED_NOTE_ID_BUNDLE, note.getId());

                    getParentFragmentManager()
                            .setFragmentResult(NOTE_SELECTED, bundle);
                }
            });

            TextView name = itemView.findViewById(R.id.note_title_in_list);
            name.setText(note.getTitle());

            listContainer.addView(itemView);
        }
    }

    @Override
    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
        int noteId = result.getInt(NotesListFragment.SELECTED_NOTE_ID_BUNDLE);

        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.main_activity_container,
                        NoteDetailsFragment.newInstance(noteId),
                        NoteDetailsFragment.TAG)
                .addToBackStack("NoteDetailsFragment")
                .commitAllowingStateLoss();
    }
}

