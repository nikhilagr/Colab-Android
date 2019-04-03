package com.nikhildagrawal.worktrack.models;

import com.google.firebase.firestore.IgnoreExtraProperties;


@IgnoreExtraProperties
public class Note {

    private String user_id;
    private String note_id;
    private String note_title;
    private String note_desc;


    public Note(){

    }

    public Note(String user_id, String note_title, String note_desc) {
        this.user_id = user_id;
        this.note_title = note_title;
        this.note_desc = note_desc;
    }

    public Note(String note_title, String note_desc) {
        this.note_title = note_title;
        this.note_desc = note_desc;
    }

    public Note(String user_id, String note_id, String note_title, String note_desc) {
        this.user_id = user_id;
        this.note_id = note_id;
        this.note_title = note_title;
        this.note_desc = note_desc;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getNote_id() {
        return note_id;
    }

    public void setNote_id(String note_id) {
        this.note_id = note_id;
    }

    public String getNote_title() {
        return note_title;
    }

    public void setNote_title(String note_title) {
        this.note_title = note_title;
    }

    public String getNote_desc() {
        return note_desc;
    }

    public void setNote_desc(String note_desc) {
        this.note_desc = note_desc;
    }

    @Override
    public String toString() {
        return "Note{" +
                "user_id='" + user_id + '\'' +
                ", note_id='" + note_id + '\'' +
                ", note_title='" + note_title + '\'' +
                ", note_desc='" + note_desc + '\'' +
                '}';
    }
}
