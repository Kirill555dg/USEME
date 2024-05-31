package com.example.useme.data.model;

import com.example.useme.data.model.statistic.Statistic;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

public class Homework {

    @SerializedName("id")
    @Expose
    private Long id;
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

    @SerializedName("completed")
    @Expose
    private List<Long> completed;

    private Integer completedTasks;

    private int countStudents;

    public int getCountStudents() {
        return countStudents;
    }

    public void setCountStudents(int countStudents) {
        this.countStudents = countStudents;
    }

    public List<Long> getCompleted() {
        return completed;
    }

    public void setCompleted(List<Long> completed) {
        this.completed = completed;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Integer getCompletedTasks() {
        return completedTasks;
    }

    public void setCompletedTasks(Integer completedTasks) {
        this.completedTasks = completedTasks;
    }

    @Override
    public String toString() {
        return "Homework{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", dateOfIssue='" + dateOfIssue + '\'' +
                ", deadline='" + deadline + '\'' +
                ", group=" + group +
                ", tasks=" + tasks +
                ", statistics=" + statistics +
                ", countTasks=" + countTasks +
                ", completed=" + completed +
                ", completedTasks=" + completedTasks +
                ", countStudents=" + countStudents +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Homework homework = (Homework) o;
        return Objects.equals(id, homework.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
