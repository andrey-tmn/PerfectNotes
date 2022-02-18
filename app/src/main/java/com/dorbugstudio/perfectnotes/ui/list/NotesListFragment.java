package com.dorbugstudio.perfectnotes.ui.list;

import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;

import com.dorbugstudio.perfectnotes.R;
import com.dorbugstudio.perfectnotes.domain.Note;
import com.dorbugstudio.perfectnotes.ui.NavigationDrawable;
import com.dorbugstudio.perfectnotes.ui.details.NoteDetailsFragment;
import com.dorbugstudio.perfectnotes.ui.details.NotesRepositoryImpl;
import com.dorbugstudio.perfectnotes.ui.settings.SettingsFragment;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;


public class NotesListFragment extends Fragment implements NotesListView, FragmentResultListener {

    public static final String TAG = "NotesListFragment";
    public static final String NOTE_SELECTED = "NOTE_SELECTED";
    public static final String SELECTED_NOTE_ID_BUNDLE = "SELECTED_NOTE_ID_BUNDLE";

    private LinearLayout listContainer;
    private View contextMenuSourceView;
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

        FragmentManager fragmentManager = getParentFragmentManager();
        fragmentManager.setFragmentResultListener(NoteDeleteDialogFragment.REQUEST_KEY,
                this, this);
        fragmentManager.setFragmentResultListener(NotesListFragment.NOTE_SELECTED,
                this, this);

        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);

        if (requireActivity() instanceof NavigationDrawable) {
            ((NavigationDrawable) requireActivity()).setAppBar(toolbar);
        }

        toolbar.setOnMenuItemClickListener(new androidx.appcompat.widget.Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_search) {
                    Toast.makeText(requireContext(), getString(R.string.action_search), Toast.LENGTH_SHORT).show();
                    return true;
                }

                if (item.getItemId() == R.id.action_sort) {
                    Toast.makeText(requireContext(), getString(R.string.action_sort), Toast.LENGTH_SHORT).show();
                    return true;
                }

                return false;
            }
        });
    }

    @Override
    public void showNotes(List<Note> notes) {
        listContainer.removeAllViews();

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

            itemView.setOnContextClickListener(new View.OnContextClickListener() {
                @Override
                public boolean onContextClick(View view) {
                    deleteNoteButtonClicked(note.getId());
                    return true;
                }
            });

            TextView name = itemView.findViewById(R.id.note_title_in_list);
            name.setText(note.getTitle());

            registerForContextMenu(itemView);

            listContainer.addView(itemView);
        }
    }

    private void deleteNoteButtonClicked(int noteId) {
        String title = getString(R.string.note_delete_gialog_title);
        String message = String.format(getString(R.string.note_delete_gialog_question), presenter.getNoteTitleById(noteId));
        NoteDeleteDialogFragment.newInstance(title, message, noteId)
                .show(getParentFragmentManager(), NoteDeleteDialogFragment.TAG);
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        contextMenuSourceView = v;

        MenuInflater inflater = requireActivity().getMenuInflater();
        inflater.inflate(R.menu.context_menu_for_note_in_list, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_delete) {
            contextMenuSourceView.performContextClick();
            return true;
        }

        return false;
    }

    @Override
    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
        if (requestKey == NotesListFragment.NOTE_SELECTED) {
            int noteId = result.getInt(NotesListFragment.SELECTED_NOTE_ID_BUNDLE);

            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_activity_container,
                            NoteDetailsFragment.newInstance(noteId),
                            NoteDetailsFragment.TAG)
                    .addToBackStack("NoteDetailsFragment")
                    .commitAllowingStateLoss();
        } else if (requestKey == NoteDeleteDialogFragment.REQUEST_KEY) {
            int noteId = result.getInt(NoteDeleteDialogFragment.ARG_NOTE_ID);
            presenter.deleteNote(noteId);

            Snackbar.make(this.getView(), getString(R.string.note_deleted), Snackbar.LENGTH_SHORT).show();
        }
    }
}

