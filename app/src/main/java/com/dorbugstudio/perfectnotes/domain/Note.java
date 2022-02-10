package com.dorbugstudio.perfectnotes.domain;

import java.io.Serializable;
import java.util.Date;

public class Note implements Serializable {

    private final int id;
    private Date date;
    private final String title;
    private final String noteBody;

    public Note(int id, String title, String noteBody) {
        this.id = id;
        this.title = title;
        this.date = new Date();
        this.noteBody = noteBody;
    }

    public String getNoteBody() {
        return noteBody;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Date getDate() {
        return date;
    }

    public void changeCreatedDate(Date date) {
        this.date = date;
    }
}
