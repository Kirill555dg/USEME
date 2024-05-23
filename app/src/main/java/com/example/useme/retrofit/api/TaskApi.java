package com.example.useme.retrofit.api;

import com.example.useme.data.model.Task;
import com.example.useme.data.model.taskdata.Category;
import com.example.useme.data.model.taskdata.Subject;
import com.example.useme.data.model.taskdata.Topic;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TaskApi {

    @GET("api/v1/tasks")
    Call<List<Task>> getAllTasks();

    @GET("api/v1/tasks/search")
    Call<List<Task>> filterTasks(@Query("id") Long id,
                                 @Query("subjectName") String subject,
                                 @Query("topicNum") Short topic,
                                 @Query("categoryName") String category);

    @GET("api/v1/tasks/{id}")
    Call<Task> getOneTask(@Path("id") Long id);

    @POST("api/v1/tasks")
    Call<Task> saveTask(@Body Task task);

    @GET("api/v1/data/subjects")
    Call<List<Subject>> getAllSubjects();

    @GET("api/v1/data/topics")
    Call<List<Topic>> getAllTopics(@Query("subject") String subject);

    @GET("api/v1/data/categories")
    Call<List<Category>> getAllCategories(@Query("subject") String subject, @Query("topic") Short topic);
}
