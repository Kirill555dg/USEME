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

    public StatisticPK getPk() {
        return pk;
    }

    public void setPk(StatisticPK pk) {
        this.pk = pk;
    }

    public Boolean getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(Boolean isCorrect) {
        this.isCorrect = isCorrect;
    }

    @Override
    public String toString() {
        return "Statistic{" +
                "pk=" + pk +
                ", isCorrect=" + isCorrect +
                '}';
    }
}
