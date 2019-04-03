package com.nikhildagrawal.worktrack.models;

public class Checklist {

    private String user_id;
    private String status;
    private String Title;

    public Checklist(String user_id, String status, String title) {
        this.user_id = user_id;
        this.status = status;
        Title = title;
    }

    public Checklist(String status, String title) {
        this.status = status;
        Title = title;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    @Override
    public String toString() {
        return "Checklist{" +
                "user_id='" + user_id + '\'' +
                ", status='" + status + '\'' +
                ", Title='" + Title + '\'' +
                '}';
    }

}
