package com.example.useme.retrofit.api;

import com.example.useme.data.model.Group;
import com.example.useme.data.model.invite.Application;
import com.example.useme.data.model.taskdata.Subject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GroupApi {

    @GET("api/v1/groups/{id}")
    Call<Group> findGroup(@Path("id") Long id);

    @GET("api/v1/groups/teacher")
    Call<List<Group>> getTeacherGroups(@Query("teacher_id") Long teacherId);

    @GET("api/v1/groups/student")
    Call<List<Group>> getStudentGroups(@Query("student_id") Long studentId);

    @POST("api/v1/groups/create")
    Call<Group> createGroup(@Body Group group);

    @GET("api/v1/data/subjects")
    Call<List<Subject>> getTargetSubjects();

    @PUT("api/v1/groups/{id}")
    Call<Group> updateGroup(@Path("id") Long id, @Body Group group);

    @DELETE("api/v1/groups/{id}")
    Call<Void> deleteGroup(@Path("id") Long id);
}
