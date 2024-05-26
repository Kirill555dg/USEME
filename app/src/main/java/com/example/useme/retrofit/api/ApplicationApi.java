package com.example.useme.retrofit.api;

import com.example.useme.data.model.Group;
import com.example.useme.data.model.invite.Application;
import com.example.useme.data.model.invite.ApplicationPK;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ApplicationApi {

    @GET("api/v1/invites/student")
    Call<List<Group>> getStudentApplications(@Query("student_id") Long studentId);

    @POST("api/v1/invites")
    Call<Application> sendApplication(@Body ApplicationPK pk);
    @PUT("api/v1/invites")
    Call<Application> acceptApplication(@Body ApplicationPK pk);
    @DELETE("api/v1/invites")
    Call<Void> deleteApplication(@Body ApplicationPK pk);
}
