package com.example.useme.data.model;

import com.example.useme.data.model.invite.Invite;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Group {

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("teacher")
    @Expose
    private Long teacher;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("targetSubject")
    @Expose
    private String targetSubject;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("countMembers")
    @Expose
    private Integer countMembers;
    @SerializedName("invites")
    @Expose
    private List<Invite> invites;
    @SerializedName("homeworks")
    @Expose
    private List<Long> homeworks_id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTeacher() {
        return teacher;
    }

    public void setTeacher(Long teacher) {
        this.teacher = teacher;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTargetSubject() {
        return targetSubject;
    }

    public void setTargetSubject(String targetSubject) {
        this.targetSubject = targetSubject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCountMembers() {
        return countMembers;
    }

    public void setCountMembers(Integer countMembers) {
        this.countMembers = countMembers;
    }

    public List<Invite> getInvites() {
        return invites;
    }

    public void setInvites(List<Invite> invites) {
        this.invites = invites;
    }

    public List<Long> getHomeworks_id() {
        return homeworks_id;
    }

    public void setHomeworks_id(List<Long> homeworks_id) {
        this.homeworks_id = homeworks_id;
    }
}
