package com.example.useme.data.model;

import com.example.useme.data.model.statistic.Statistic;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Homework {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("dateOfIssue")
    @Expose
    private String dateOfIssue;
    @SerializedName("deadline")
    @Expose
    private String deadline;
    @SerializedName("group")
    @Expose
    private Group group;
    @SerializedName("tasks")
    @Expose
    private List<Task> tasks;
    @SerializedName("statistics")
    @Expose
    private List<Statistic> statistics;

    @SerializedName("countTasks")
    @Expose
    private int countTasks;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDateOfIssue() {
        return dateOfIssue;
    }

    public void setDateOfIssue(String dateOfIssue) {
        this.dateOfIssue = dateOfIssue;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public List<Statistic> getStatistics() {
        return statistics;
    }

    public void setStatistics(List<Statistic> statistics) {
        this.statistics = statistics;
    }

    public int getCountTasks() {
        return countTasks;
    }

    public void setCountTasks(int countTasks) {
        this.countTasks = countTasks;
    }
}
