package com.nikhildagrawal.worktrack.models;

public class FirebaseCloudMessage {

    private String to;
    private NotificationData data;

    public FirebaseCloudMessage() {

    }

    public FirebaseCloudMessage(String to, NotificationData data) {
        this.to = to;
        this.data = data;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public NotificationData getData() {
        return data;
    }

    public void setData(NotificationData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "FirebaseCloudMessage{" +
                "to='" + to + '\'' +
                ", data=" + data +
                '}';
    }
}
