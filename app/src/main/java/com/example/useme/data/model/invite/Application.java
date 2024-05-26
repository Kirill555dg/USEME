package com.example.useme.data.model.invite;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Application {

    @SerializedName("pk")
    @Expose
    private ApplicationPK pk;
    @SerializedName("isAccept")
    @Expose
    private Boolean isAccept;

    public ApplicationPK getPk() {
        return pk;
    }

    public void setPk(ApplicationPK pk) {
        this.pk = pk;
    }

    public Boolean getAccept() {
        return isAccept;
    }

    public void setAccept(Boolean accept) {
        isAccept = accept;
    }

    @Override
    public String toString() {
        return "Application{" +
                "pk=" + pk +
                ", isAccept=" + isAccept +
                '}';
    }
}
