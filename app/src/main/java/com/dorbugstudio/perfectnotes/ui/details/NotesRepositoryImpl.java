package com.dorbugstudio.perfectnotes.ui.details;

import com.dorbugstudio.perfectnotes.domain.NotesRepository;
import com.dorbugstudio.perfectnotes.domain.Note;

import java.util.ArrayList;
import java.util.List;

public class NotesRepositoryImpl implements NotesRepository {

    private static final NotesRepository INSTANCE = new NotesRepositoryImpl();

    public static NotesRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Note> getNotes() {
        ArrayList<Note> notes = new ArrayList<>();

        notes.add(new Note(1, "Название заметки 1", "Текст заметки 1"));
        notes.add(new Note(2, "Название заметки 2", "Текст заметки 2"));
        notes.add(new Note(3, "Название заметки 3", "Текст заметки 3"));
        notes.add(new Note(4, "Название заметки 4", "Текст заметки 4"));
        notes.add(new Note(5, "Название заметки 5", "Текст заметки 5"));

        return notes;
    }
}