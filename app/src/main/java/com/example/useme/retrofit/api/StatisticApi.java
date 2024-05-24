package com.example.useme.retrofit.api;

import com.example.useme.data.model.statistic.Statistic;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface StatisticApi {

    @GET("api/v1/statistics")
    Call<List<Statistic>> getStatistics(@Query("student_id") Long studentId,
                                        @Query("homework_id") Long homeworkId,
                                        @Query("task_id") Long taskId);

    @GET("api/v1/statistics/group")
    Call<List<Statistic>> findStudentStatisticInGroup(@Query("student_id") Long studentId,
                                               @Query("group_id") Long groupId);

    @GET("api/v1/statistics/student")
    Call<List<Statistic>> findStudentStatistic(@Query("student_id") Long studentId);

    @GET("api/v1/statistics/homework")
    Call<List<Statistic>> findHomeworkStatistic(@Query("homework_id") Long homeworkId);

    @POST("api/v1/statistics/create")
    Call<List<Statistic>> createHomeworkStatistic(@Body List<Statistic> statistics);
}
