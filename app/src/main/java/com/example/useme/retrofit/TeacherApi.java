package com.example.useme.retrofit;

import com.example.useme.model.Teacher;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface TeacherApi {

    @POST("api/v1/teachers/registration")
    Call<Teacher> register(@Body Teacher teacher);
}
