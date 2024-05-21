package com.example.useme.retrofit;

import com.example.useme.model.Teacher;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface TeacherApi {

    String path = "api/v1/teachers";

    @POST(path+"/registration")
    Call<Teacher> register(@Body Teacher teacher);

    @POST(path+"/authorization")
    Call<Teacher> auth(@Body Teacher teacher);
}
