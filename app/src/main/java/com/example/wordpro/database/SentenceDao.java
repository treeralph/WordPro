package com.example.wordpro.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface SentenceDao {

    @Update
    public void updateSentences(Sentence... sentences);

    @Delete
    public void deleteSentence(Sentence sentence);

    @Insert
    public void insertSentences(Sentence... sentences);

    @Query("Select * from sentence")
    public List<Sentence> getAllSentences();

    @Query("Select * from sentence where path == :path")
    public List<Sentence> getSentencesWithPath(String path);

    @Query("Select * from sentence Where next <= :today")
    public List<Sentence> getTodaySentences(String today);

    @Query("delete from sentence")
    public void deleteAllSentence();

    @Query("delete from sentence where id == :id")
    public void deleteSentenceById(int id);

    @Query("select distinct path from sentence")
    public List<String> getAllPaths();
}
