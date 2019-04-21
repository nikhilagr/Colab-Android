package com.nikhildagrawal.worktrack.models;

public class Contact {

    private String name;
    private String email;
    private String contactNumber;
    private boolean selected;
    private String auth_id;


    public Contact(String name, String email, String contactNumber) {
        this.name = name;
        this.email = email;
        this.contactNumber = contactNumber;
    }

    public Contact(String name, String email, String contactNumber, boolean selected) {
        this.name = name;
        this.email = email;
        this.contactNumber = contactNumber;
        this.selected = selected;
    }

    public Contact(String name, String email, String contactNumber, boolean selected, String auth_id) {
        this.name = name;
        this.email = email;
        this.contactNumber = contactNumber;
        this.selected = selected;
        this.auth_id = auth_id;
    }

    public String getAuth_id() {
        return auth_id;
    }

    public void setAuth_id(String auth_id) {
        this.auth_id = auth_id;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Contact(String name, String contactNumber) {
        this.name = name;
        this.contactNumber = contactNumber;
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

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "name='" + name + '\'' +
                ", selected=" + selected +
                '}';
    }
}

