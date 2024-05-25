package com.example.useme.data;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.useme.data.dao.LocalStatisticDAO;
import com.example.useme.data.model.statistic.LocalStatistic;
import com.example.useme.data.model.statistic.Statistic;

@Database(entities = {LocalStatistic.class}, version = 1)
public abstract class LocalDatabase extends RoomDatabase {

    public abstract LocalStatisticDAO statisticDAO();
}
