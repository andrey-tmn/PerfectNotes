package com.dorbugstudio.perfectnotes.ui.details;

import com.dorbugstudio.perfectnotes.domain.NotesRepository;
import com.dorbugstudio.perfectnotes.domain.Note;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotesRepositoryImpl implements NotesRepository, Serializable {

    private final Map<Integer, Note> notes;

    public NotesRepositoryImpl() {
        notes = new HashMap<>();
        for (int i = 0; i < 5; i++) {
            int id = i + 1;
            Note note = new Note(id, "Название заметки " + id, "Текст заметки " + id);
            notes.put(id, note);
        }
    }

    private static final NotesRepository INSTANCE = new NotesRepositoryImpl();

    public static NotesRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Note> getNotes() {
        return new ArrayList<>(notes.values());
    }

    @Override
    public void changeNoteCreatedDate(int id, Date createdDate) {
        Note note = notes.get(id);
        if (note != null)
            note.changeCreatedDate(createdDate);
    }

    @Override
    public Note getNoteById(int id) {
        return notes.get(id);
    }
}