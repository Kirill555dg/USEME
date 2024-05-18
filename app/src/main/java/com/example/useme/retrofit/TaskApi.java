package com.example.useme.retrofit;

import com.example.useme.model.Task;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface TaskApi {

    @GET("api/v1/tasks")
    Call<List<Task>> getAllTasks();

    @GET("api/v1/tasks/{id}")
    Call<Task> getOneTask(@Path("id") Long id);

    @POST("api/v1/tasks")
    Call<Task> saveTask(@Body Task task);
}
