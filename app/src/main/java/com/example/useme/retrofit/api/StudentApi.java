package com.example.useme.retrofit.api;

import com.example.useme.data.model.Group;
import com.example.useme.data.model.Student;
import com.example.useme.data.model.invite.Application;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface StudentApi {

    @POST("api/v1/students/registration")
    Call<Student> register(@Body Student student);

    @POST("api/v1/students/authorization")
    Call<Student> auth(@Body Student student);

    @PATCH("api/v1/students/change_info")
    Call<Student> changeInfo(@Body Student student);

    @GET("api/v1/students/group")
    Call<List<Student>> getGroupStudents(@Query("group_id") Long id);

}
