package com.example.useme.retrofit.api;

import com.example.useme.data.model.Teacher;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PATCH;
import retrofit2.http.POST;

public interface TeacherApi {

    @POST("api/v1/teachers/registration")
    Call<Teacher> register(@Body Teacher teacher);

    @POST("api/v1/teachers/authorization")
    Call<Teacher> auth(@Body Teacher teacher);

    @PATCH("api/v1/teachers/change_info")
    Call<Teacher> changeInfo(@Body Teacher teacher);
}
