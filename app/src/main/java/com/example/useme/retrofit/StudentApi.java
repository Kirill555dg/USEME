package com.example.useme.retrofit;

import com.example.useme.model.Student;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface StudentApi {

    String path = "api/v1/students";

    @POST(path+"/registration")
    Call<Student> register(@Body Student student);

    @POST(path+"/authorization")
    Call<Student> auth(@Body Student student);
}
