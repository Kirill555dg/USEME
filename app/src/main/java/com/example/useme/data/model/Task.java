package com.example.useme.data.model;


import com.example.useme.data.model.taskdata.TopicPK;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Task {

    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("topicPK")
    @Expose
    private TopicPK topicPK;

    @SerializedName("category")
    @Expose
    private String category;

    @SerializedName("condition")
    @Expose
    private String condition;

    @SerializedName("answer")
    @Expose
    private String answer;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TopicPK getTopicPK() {
        return topicPK;
    }

    public void setTopicPK(TopicPK topicPK) {
        this.topicPK = topicPK;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

}
