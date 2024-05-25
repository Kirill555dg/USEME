package com.example.useme.data.repository;

import android.content.Context;

import androidx.room.Room;

import com.example.useme.data.LocalDatabase;
import com.example.useme.data.dao.LocalStatisticDAO;
import com.example.useme.data.model.statistic.LocalStatistic;

import java.util.List;

public class LocalStatisticRepository {

    LocalStatisticDAO statisticDAO;

    public LocalStatisticRepository(Context context){
        LocalDatabase database = Room.databaseBuilder(context, LocalDatabase.class, "database2")
                .build();
        statisticDAO = database.statisticDAO();
    }

    public List<LocalStatistic> findHomeworkStatistics(Long student_id, Long homework_id){
        return statisticDAO.findHomeworkStatistics(student_id, homework_id);
    }

    public LocalStatistic findTaskStatistics(Long student_id, Long homework_id,  Long task_id){
        return statisticDAO.findTaskStatistics(student_id, homework_id, task_id);
    }

    public void insertStatistic(LocalStatistic statistic){
        statisticDAO.insertStatistic(statistic);
    }

    public void deleteStatistics(Long student_id, Long homework_id){
        statisticDAO.deleteStatistic(student_id, homework_id);
    }
}
