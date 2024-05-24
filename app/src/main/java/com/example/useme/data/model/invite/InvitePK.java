package com.example.useme.data.model.invite;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InvitePK {

    @SerializedName("student")
    @Expose
    private Long studentId;
    @SerializedName("group")
    @Expose
    private Long groupId;

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }
}
