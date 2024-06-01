package com.example.useme.data.model.statistic;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Statistic {

    @SerializedName("pk")
    @Expose
    private StatisticPK pk;

    @SerializedName("isCorrect")
    @Expose
    private Boolean isCorrect;

    @SerializedName("answer")
    @Expose
    private String answer;

    public StatisticPK getPk() {
        return pk;
    }

    public void setPk(StatisticPK pk) {
        this.pk = pk;
    }

    public Boolean getCorrect() {
        return isCorrect;
    }

    public void setCorrect(Boolean isCorrect) {
        this.isCorrect = isCorrect;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "Statistic{" +
                "pk=" + pk +
                ", isCorrect=" + isCorrect +
                '}';
    }
}
