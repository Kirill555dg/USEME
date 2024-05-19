package com.example.useme.model.data;

public class TopicPK {

    private String subject;
    private Short topicNumber;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Short getTopicNumber() {
        return topicNumber;
    }

    public void setTopicNumber(Short topicNumber) {
        this.topicNumber = topicNumber;
    }

    @Override
    public String toString() {
        return "TopicPK{" +
                "subject='" + subject + '\'' +
                ", topicNumber=" + topicNumber +
                '}';
    }
}
