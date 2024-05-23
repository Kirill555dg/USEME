package com.example.useme.retrofit.api;

import com.example.useme.data.model.Teacher;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PATCH;
import retrofit2.http.POST;

public interface TeacherApi {

    String path = "api/v1/teachers";

    @POST(path+"/registration")
    Call<Teacher> register(@Body Teacher teacher);

    @POST(path+"/authorization")
    Call<Teacher> auth(@Body Teacher teacher);

    @PATCH(path+"/change_info")
    Call<Teacher> changeInfo(@Body Teacher teacher);
}
