package com.nikhildagrawal.worktrack.models;


import com.google.firebase.firestore.IgnoreExtraProperties;

import java.util.List;

@IgnoreExtraProperties
public class User {

    private String user_id;
    private String first_name;
    private String last_name;
    private String dob;
    private String email;
    private String profile_url;
    private String fcm_instance_token;
    private List<String> projects;


    public String getFcm_instance_token() {
        return fcm_instance_token;
    }

    public void setFcm_instance_token(String fcm_instance_token) {
        this.fcm_instance_token = fcm_instance_token;
    }

    public List<String> getProjects() {
        return projects;
    }

    public void setProjects(List<String> projects) {
        this.projects = projects;
    }

    public User(){

    }

    public User(String first_name, String last_name) {
        this.first_name = first_name;
        this.last_name = last_name;
    }

    public User(String user_id, String first_name, String last_name, String dob, String email, String profile_url) {
        this.user_id = user_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.dob = dob;
        this.email = email;
        this.profile_url = profile_url;
    }

    public User(String user_id, String first_name, String last_name, String dob, String email, String profile_url, String fcm_instance_token, List<String> projects) {
        this.user_id = user_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.dob = dob;
        this.email = email;
        this.profile_url = profile_url;
        this.fcm_instance_token = fcm_instance_token;
        this.projects = projects;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
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



    @Override
    public String toString() {
        return "User{" +
                "user_id='" + user_id + '\'' +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", dob='" + dob + '\'' +
                ", email='" + email + '\'' +
                ", profile_url='" + profile_url + '\'' +
                '}';
    }
}
