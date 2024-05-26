package com.example.useme.data.model.invite;

import com.example.useme.data.model.Group;
import com.example.useme.data.model.Student;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApplicationPK {

    @SerializedName("student")
    @Expose
    private Student student;
    @SerializedName("group")
    @Expose
    private Group group;

    public ApplicationPK(){

    }
    public ApplicationPK(Student student, Group group) {
        this.student = student;
        this.group = group;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    @Override
    public String toString() {
        return "ApplicationPK{" +
                "student=" + student +
                ", group=" + group +
                '}';
    }
}
