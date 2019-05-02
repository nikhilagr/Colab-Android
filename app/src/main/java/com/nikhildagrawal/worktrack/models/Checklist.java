package com.nikhildagrawal.worktrack.models;

public class Checklist implements Comparable<Checklist>{

    private String user_id;
    private String checklist_id;
    private String status;
    private String Title;



    public Checklist(){

    }


    public Checklist(String user_id, String checklist_id, String status, String title) {
        this.user_id = user_id;
        this.checklist_id = checklist_id;
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

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getChecklist_id() {
        return checklist_id;
    }

    public void setChecklist_id(String checklist_id) {
        this.checklist_id = checklist_id;
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
                ", checklist_id='" + checklist_id + '\'' +
                ", status='" + status + '\'' +
                ", Title='" + Title + '\'' +
                '}';
    }

    @Override
    public int compareTo(Checklist checklist) {
        return (this.getTitle().compareToIgnoreCase(checklist.getTitle()) < 0 ? -1 :
                (this.getTitle().compareToIgnoreCase(checklist.getTitle()) == 0 ? 0 : 1));
    }
}
