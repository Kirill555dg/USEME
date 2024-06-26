package com.example.useme.data.model;

import com.example.useme.data.model.invite.Application;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Group {

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("teacher")
    @Expose
    private Teacher teacher;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("targetSubject")
    @Expose
    private String targetSubject;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("countMembers")
    @Expose
    private Integer countMembers;
    @SerializedName("countHomeworks")
    @Expose
    private Integer countHomeworks;
    @SerializedName("invites")
    @Expose
    private List<Application> applications;
    @SerializedName("homeworks")
    @Expose
    private List<Long> homeworks_id;

    public Group(){
    }
    public Group(Long id) {
        this.id = id;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getCountMembers() {
        return countMembers;
    }

    public void setCountMembers(Integer countMembers) {
        this.countMembers = countMembers;
    }

    public Integer getCountHomeworks() {
        return countHomeworks;
    }

    public void setCountHomeworks(Integer countHomeworks) {
        this.countHomeworks = countHomeworks;
    }

    public List<Application> getInvites() {
        return applications;
    }

    public void setInvites(List<Application> applications) {
        this.applications = applications;
    }

    public List<Long> getHomeworks_id() {
        return homeworks_id;
    }

    public void setHomeworks_id(List<Long> homeworks_id) {
        this.homeworks_id = homeworks_id;
    }
}
