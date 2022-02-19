package com.dorbugstudio.perfectnotes.ui.list;

import com.dorbugstudio.perfectnotes.domain.NotesRepository;
import com.dorbugstudio.perfectnotes.domain.Note;

import java.util.List;

public class NotesListPresenter {

    private final NotesListView view;
    private final NotesRepository repository;

    public NotesListPresenter(NotesListView view, NotesRepository repository) {
        this.view = view;
        this.repository = repository;
    }

    public void requestNotes() {
        view.showNotes(repository.getNotes());
    }

    public String getNoteTitleById(int id) {
        String title = "";

        if (id == 0) {
            return title;
        }

        Note note = repository.getNoteById(id);

        if (note == null) {
            return title;
        }

        return note.getTitle();
    }

    public void deleteNote(int id) {
        repository.deleteNote(id);
        requestNotes();
    }
}

