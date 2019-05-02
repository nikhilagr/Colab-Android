package com.nikhildagrawal.worktrack.models;

public class NotificationData {

    private String title;
    private String message;
    private String data_type;

    public NotificationData(String title, String message, String data_type) {
        this.title = title;
        this.message = message;
        this.data_type = data_type;
    }


    public NotificationData(){

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData_type() {
        return data_type;
    }

    public void setData_type(String data_type) {
        this.data_type = data_type;
    }

    @Override
    public String toString() {
        return "NotificationData{" +
                "title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", data_type='" + data_type + '\'' +
                '}';
    }
}
