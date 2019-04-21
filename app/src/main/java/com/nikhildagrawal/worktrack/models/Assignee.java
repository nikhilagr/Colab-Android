package com.nikhildagrawal.worktrack.models;

public class Assignee {

    private String name;
    private String email;
    private boolean selected;
    private String auth_id;

    public Assignee(String name, String email, boolean selected, String auth_id) {
        this.name = name;
        this.email = email;
        this.selected = selected;
        this.auth_id = auth_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getAuth_id() {
        return auth_id;
    }

    public void setAuth_id(String auth_id) {
        this.auth_id = auth_id;
    }

    @Override
    public String toString() {
        return "Assignee{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", selected=" + selected +
                ", auth_id='" + auth_id + '\'' +
                '}';
    }
}
