package com.example.useme.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.useme.data.model.statistic.LocalStatistic;

import java.util.List;

@Dao
public interface LocalStatisticDAO {

    @Query("SELECT * FROM statistics WHERE student_id = :student_id AND homework_id = :homework_id")
    public List<LocalStatistic> findHomeworkStatistics(Long student_id, Long homework_id);

    @Query("SELECT * FROM statistics WHERE student_id = :student_id AND homework_id = :homework_id AND task_id = :task_id")
    public LocalStatistic findTaskStatistics(Long student_id, Long homework_id, Long task_id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertStatistic(LocalStatistic statistic);

    @Query("DELETE FROM statistics WHERE student_id = :student_id AND homework_id = :homework_id")
    public void deleteStatistic(Long student_id, Long homework_id);
}
