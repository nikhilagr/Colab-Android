package com.nikhildagrawal.worktrack.models;


import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {

    private String user_id;
    private String firtst_name;
    private String last_name;
    private String dob;
    private String email;
    private String profile_url;
    private String user_auth_id;        ;



    public User(){

    }

    public User(String user_id, String firtst_name, String last_name, String dob, String email, String profile_url) {
        this.user_id = user_id;
        this.firtst_name = firtst_name;
        this.last_name = last_name;
        this.dob = dob;
        this.email = email;
        this.profile_url = profile_url;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_auth_id(String user_auth_id) {
        this.user_auth_id = user_auth_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFirtst_name() {
        return firtst_name;
    }

    public void setFirtst_name(String firtst_name) {
        this.firtst_name = firtst_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfile_url() {
        return profile_url;
    }

    public void setProfile_url(String profile_url) {
        this.profile_url = profile_url;
    }

    public String getUser_auth_id() {
        return user_auth_id;
    }

    @Override
    public String toString() {
        return "User{" +
                "user_id='" + user_id + '\'' +
                ", firtst_name='" + firtst_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", dob='" + dob + '\'' +
                ", email='" + email + '\'' +
                ", profile_url='" + profile_url + '\'' +
                '}';
    }
}
