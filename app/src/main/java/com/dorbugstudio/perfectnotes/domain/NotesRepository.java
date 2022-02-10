package com.dorbugstudio.perfectnotes.domain;

import java.util.Date;
import java.util.List;

public interface NotesRepository {

    List<Note> getNotes();

    void changeNoteCreatedDate(int id, Date createdDate);

    Note getNoteById(int id);

}