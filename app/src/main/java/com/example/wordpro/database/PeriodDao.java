package com.example.wordpro.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PeriodDao {

    @Insert
    public void insertPeriod(Period period);

    @Query("Select * from period")
    public List<Period> getAllPeriods();
}
