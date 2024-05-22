package com.example.useme.data.model.taskdata;

public class Topic {

    private TopicPK pk;
    private String topicName;

    public TopicPK getPk() {
        return pk;
    }

    public void setPk(TopicPK pk) {
        this.pk = pk;
    }


    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    @Override
    public String toString() {
        return pk.getTopicNumber() + ". " + topicName;
    }
}
