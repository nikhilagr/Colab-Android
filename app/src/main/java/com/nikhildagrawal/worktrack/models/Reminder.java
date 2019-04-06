package com.nikhildagrawal.worktrack.models;

public class Reminder {

    private String user_id;
    private String reminder_id;
    private String title;
    private String desc;
    private String date;
    private String time;



    public Reminder(String title, String desc, String date, String time) {
        this.title = title;
        this.desc = desc;
        this.date = date;
        this.time = time;
    }

    public Reminder(String user_id, String reminder_id, String title, String desc, String date, String time) {
        this.user_id = user_id;
        this.reminder_id = reminder_id;
        this.title = title;
        this.desc = desc;
        this.date = date;
        this.time = time;
    }

    public Reminder() {
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getReminder_id() {
        return reminder_id;
    }

    public void setReminder_id(String reminder_id) {
        this.reminder_id = reminder_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Reminder{" +
                "user_id='" + user_id + '\'' +
                ", reminder_id='" + reminder_id + '\'' +
                ", title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
