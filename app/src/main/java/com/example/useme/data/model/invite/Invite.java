package com.example.useme.data.model.invite;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Invite {

    @SerializedName("pk")
    @Expose
    private InvitePK pk;
    @SerializedName("isAccept")
    @Expose
    private Boolean isAccept;

    public InvitePK getPk() {
        return pk;
    }

    public void setPk(InvitePK pk) {
        this.pk = pk;
    }

    public Boolean getIsAccept() {
        return isAccept;
    }

    public void setIsAccept(Boolean isAccept) {
        this.isAccept = isAccept;
    }

}
