package com.example.wordpro.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TodaySentenceDao {

    @Update
    public void updateTodaySentence(TodaySentence... todaySentences);

    @Delete
    public void deleteTodaySentence(TodaySentence todaySentence);

    @Insert
    public void insertTodaySentence(TodaySentence todaySentence);

    @Query("Select * from today_sentence")
    public List<TodaySentence> getAllTodaySentences();

    @Query("Select * from today_sentence where next = :current")
    public TodaySentence getCurrent(String current);

    @Query("Delete from today_sentence")
    public void deleteAllTodaySentence();

}
