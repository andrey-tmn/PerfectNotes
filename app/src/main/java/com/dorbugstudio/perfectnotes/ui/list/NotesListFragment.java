package com.dorbugstudio.perfectnotes.ui.list;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dorbugstudio.perfectnotes.R;
import com.dorbugstudio.perfectnotes.domain.Note;
import com.dorbugstudio.perfectnotes.ui.NavigationDrawable;
import com.dorbugstudio.perfectnotes.ui.details.NoteDetailsFragment;
import com.dorbugstudio.perfectnotes.ui.details.NoteEditFragment;
import com.dorbugstudio.perfectnotes.ui.details.NotesRepositoryImpl;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;


public class NotesListFragment extends Fragment implements NotesListView, FragmentResultListener {

    public static final String TAG = "NotesListFragment";
    public static final String NOTE_SELECTED = "NOTE_SELECTED";
    public static final String SELECTED_NOTE_ID_BUNDLE = "SELECTED_NOTE_ID_BUNDLE";

    private NotesListAdapter adapter;
    private NotesListPresenter presenter;
    private RecyclerView listRecyclerView;

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

        adapter = new NotesListAdapter();

        listRecyclerView = view.findViewById(R.id.note_list_container);
        registerForContextMenu(listRecyclerView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        listRecyclerView.setLayoutManager(layoutManager);

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
        adapter.setOnNoteClicked(new NotesListAdapter.OnNoteClicked() {
            @Override
            public void onNoteClicked(int noteId) {
                Bundle bundle = new Bundle();
                bundle.putInt(SELECTED_NOTE_ID_BUNDLE, noteId);

                getParentFragmentManager()
                        .setFragmentResult(NOTE_SELECTED, bundle);
            }
        });

        listRecyclerView.setAdapter(adapter);
        adapter.setNotes(notes);
        adapter.notifyDataSetChanged();
    }

    private void deleteNoteButtonClicked(int noteId) {
        String title = getString(R.string.note_delete_gialog_title);
        String message = getString(R.string.note_delete_gialog_question, presenter.getNoteTitleById(noteId));
        NoteDeleteDialogFragment.newInstance(title, message, noteId)
                .show(getParentFragmentManager(), NoteDeleteDialogFragment.TAG);
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = requireActivity().getMenuInflater();
        inflater.inflate(R.menu.context_menu_for_note_in_list, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int noteId = adapter.getLongClickNoteId();

        if (item.getItemId() == R.id.action_edit) {
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_activity_container,
                            NoteEditFragment.newInstance(noteId),
                            NoteEditFragment.TAG)
                    .addToBackStack("NoteEditFragment")
                    .commitAllowingStateLoss();
            return true;
        }

        if (item.getItemId() == R.id.action_delete) {
            deleteNoteButtonClicked(noteId);
            return true;
        }

        return false;
    }

    @Override
    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
        if (requestKey.equals(NotesListFragment.NOTE_SELECTED)) {
            int noteId = result.getInt(NotesListFragment.SELECTED_NOTE_ID_BUNDLE);

            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_activity_container,
                            NoteDetailsFragment.newInstance(noteId),
                            NoteDetailsFragment.TAG)
                    .addToBackStack("NoteDetailsFragment")
                    .commitAllowingStateLoss();
        } else if (requestKey.equals(NoteDeleteDialogFragment.REQUEST_KEY)) {
            int noteId = result.getInt(NoteDeleteDialogFragment.ARG_NOTE_ID);
            presenter.deleteNote(noteId);

            Snackbar.make(requireView(), getString(R.string.note_deleted), Snackbar.LENGTH_SHORT).show();
        }
    }
}

