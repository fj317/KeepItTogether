package com.example.keep_it_together;

public class TaskListData {
    private String name;
    private String date;
    private String task;

    public TaskListData(String name, String date, String task){
        this.date = date;
        this.name = name;
        this.task = task;
    }

    public String getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public String getTask() {
        return task;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTask(String task) {
        this.task = task;
    }
}

