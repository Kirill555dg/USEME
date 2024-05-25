package com.example.useme.data.model.statistic;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName = "statistics", primaryKeys = {"student_id","homework_id","task_id"})
public class LocalStatistic {

    @ColumnInfo(name = "student_id")
    @NonNull
    private Long studentId;
    @ColumnInfo(name = "homework_id")
    @NonNull
    private Long homeworkId;
    @ColumnInfo(name = "task_id")
    @NonNull
    private Long taskId;
    @ColumnInfo(name = "correct")
    private Boolean isCorrect;

    @ColumnInfo(name = "answer")
    private String answer;

    public LocalStatistic(@NonNull Long studentId, @NonNull Long homeworkId, @NonNull Long taskId, Boolean isCorrect, String answer) {
        this.studentId = studentId;
        this.homeworkId = homeworkId;
        this.taskId = taskId;
        this.isCorrect = isCorrect;
        this.answer = answer;
    }

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

    public Boolean getCorrect() {
        return isCorrect;
    }

    public void setCorrect(Boolean correct) {
        isCorrect = correct;
    }


    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
    @Override
    public String toString() {
        return "LocalStatistic{" +
                "studentId=" + studentId +
                ", homeworkId=" + homeworkId +
                ", taskId=" + taskId +
                ", isCorrect=" + isCorrect +
                ", answer='" + answer + '\'' +
                '}';
    }
}
