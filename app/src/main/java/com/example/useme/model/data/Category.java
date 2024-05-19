package com.example.useme.model.data;

public class Category {

    private TopicPK pk;
    private String category;

    public TopicPK getPk() {
        return pk;
    }

    public void setPk(TopicPK pk) {
        this.pk = pk;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Category{" +
                "pk=" + pk +
                ", category='" + category + '\'' +
                '}';
    }
}
