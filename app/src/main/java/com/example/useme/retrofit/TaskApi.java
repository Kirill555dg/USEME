package com.example.useme.retrofit;

import com.example.useme.model.Task;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface TaskApi {

    @GET("api/")
    Call<List<Task>> getAllTasks();
}
