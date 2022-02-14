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
        List<Note> notes = repository.getNotes();
        view.showNotes(notes);
    }
}

