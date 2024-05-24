package com.example.useme.retrofit.api;

import com.example.useme.data.model.Homework;
import com.example.useme.data.model.Task;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface HomeworkApi {

    @GET("api/v1/homeworks")
    Call<List<Homework>> findAllHomeworks();


    @GET("api/v1/homeworks/{id}")
    Call<Homework> findHomework(@Path("id") Long homeworkId);

    @GET("api/v1/homeworks/{id}/tasks")
    Call<List<Task>> getHomeworkTasks(@Path("id") Long homeworkId);

    @GET("api/v1/homeworks/group")
    Call<List<Homework>> findGroupHomeworks(@Query("group_id") Long groupId);

    @POST("api/v1/homeworks/create")
    Call<Homework> createHomework(@Body Homework homework);

    @PUT("api/v1/homeworks/{id}")
    Call<Homework> updateHomework(@Path("id") Long homeworkId, @Body Homework homework);

    @DELETE("api/v1/homeworks/{id}")
    Call<Void> deleteHomework(@Path("id") Long homeworkId);

}
