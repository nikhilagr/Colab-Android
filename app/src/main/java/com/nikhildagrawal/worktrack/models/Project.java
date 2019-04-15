package com.nikhildagrawal.worktrack.models;

import com.google.firebase.firestore.IgnoreExtraProperties;
import java.util.List;


@IgnoreExtraProperties
public class Project  {

    private String project_id;
    private String title;
    private String description;
    private String creator_id;
    private String start_date;
    private String end_date;
    private List<String> members;
    private List<String> tasks;

    public Project(){

    }

    public Project(String project_id, String title, String description, String creator_id, String start_date, String end_date, List<String> members, List<String> tasks) {
        this.project_id = project_id;
        this.title = title;
        this.description = description;
        this.creator_id = creator_id;
        this.start_date = start_date;
        this.end_date = end_date;
        this.members = members;
        this.tasks = tasks;
    }

    public Project(String title, String description, String creator_id, String start_date, String end_date, List<String> members, List<String> tasks) {
        this.title = title;
        this.description = description;
        this.creator_id = creator_id;
        this.start_date = start_date;
        this.end_date = end_date;
        this.members = members;
        this.tasks = tasks;
    }

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(String creator_id) {
        this.creator_id = creator_id;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public List<String> getTasks() {
        return tasks;
    }

    public void setTasks(List<String> tasks) {
        this.tasks = tasks;
    }

    @Override
    public String toString() {
        return "Project{" +
                "project_id='" + project_id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", creator_id='" + creator_id + '\'' +
                ", start_date='" + start_date + '\'' +
                ", end_date='" + end_date + '\'' +
                ", members=" + members +
                ", tasks=" + tasks +
                '}';
    }
}