package com.example.useme.data.model.statistic;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StatisticPK {
    @SerializedName("student")
    @Expose
    private Long studentId;
    @SerializedName("homework")
    @Expose
    private Long homeworkId;
    @SerializedName("task")
    @Expose
    private Long taskId;

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getHomeworkId() {
        return homeworkId;
    }

    public void setHomeworkId(Long homeworkId) {
        this.homeworkId = homeworkId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    @Override
    public String toString() {
        return "StatisticPK{" +
                "studentId=" + studentId +
                ", homeworkId=" + homeworkId +
                ", taskId=" + taskId +
                '}';
    }
}
