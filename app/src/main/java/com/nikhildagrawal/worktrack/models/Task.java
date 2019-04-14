package com.nikhildagrawal.worktrack.models;


import java.util.List;


public class Task {

    private String task_id;
    private String project_id;
    private String name;
    private String desc;
    private String start_date;
    private String end_date;
    private List<String> assigned_to;
    private String status;


    public Task(String task_id, String project_id, String name, String desc, String start_date, String end_date, List<String> assigned_to, String status) {
        this.task_id = task_id;
        this.project_id = project_id;
        this.name = name;
        this.desc = desc;
        this.start_date = start_date;
        this.end_date = end_date;
        this.assigned_to = assigned_to;
        this.status = status;
    }

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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

    public List<String> getAssigned_to() {
        return assigned_to;
    }

    public void setAssigned_to(List<String> assigned_to) {
        this.assigned_to = assigned_to;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Task{" +
                "task_id='" + task_id + '\'' +
                ", project_id='" + project_id + '\'' +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", start_date='" + start_date + '\'' +
                ", end_date='" + end_date + '\'' +
                ", assigned_to=" + assigned_to +
                ", status='" + status + '\'' +
                '}';
    }


}
