package com.example.useme.data.model.statistic;

import com.example.useme.data.model.Homework;
import com.example.useme.data.model.Student;
import com.example.useme.data.model.Task;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class StatisticPK {
    @SerializedName("student")
    @Expose
    private Student student;
    @SerializedName("homework")
    @Expose
    private Homework homework;
    @SerializedName("task")
    @Expose
    private Task task;

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Homework getHomework() {
        return homework;
    }

    public void setHomework(Homework homework) {
        this.homework = homework;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    @Override
    public String toString() {
        return "StatisticPK{" +
                "student=" + student +
                ", homework=" + homework +
                ", task=" + task +
                '}';
    }
}
