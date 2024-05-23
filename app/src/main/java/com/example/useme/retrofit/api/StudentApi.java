package com.example.useme.retrofit.api;

import com.example.useme.data.model.Student;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PATCH;
import retrofit2.http.POST;

public interface StudentApi {

    String path = "api/v1/students";

    @POST(path+"/registration")
    Call<Student> register(@Body Student student);

    @POST(path+"/authorization")
    Call<Student> auth(@Body Student student);

    @PATCH(path+"/change_info")
    Call<Student> changeInfo(@Body Student student);
}
