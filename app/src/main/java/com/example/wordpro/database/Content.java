package com.example.wordpro.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "contents")
public class Content {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "content_title")
    public String content_title;

    @ColumnInfo(name = "content_category")
    public String content_category;

    @ColumnInfo(name = "register_time")
    public String register_time;

    @ColumnInfo(name = "register_user")
    public String register_user;

    @ColumnInfo(name = "downloads_num")
    public int downloads_num;
}
