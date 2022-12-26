package com.example.wordpro.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ContentDao {

    @Update
    public void updateContents(Content... contents);

    @Delete
    public void deleteContent(Content content);

    @Insert
    public void insertContents(Content... contents);

    @Query("Select * from contents")
    public List<Content> getAllContents();

    @Query("Select * from contents where content_category == :content_category")
    public List<Content> getCategoryContents(String content_category);

}
