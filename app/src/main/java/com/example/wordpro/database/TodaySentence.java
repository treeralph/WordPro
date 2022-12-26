package com.example.wordpro.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "today_sentence")
public class TodaySentence {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "original_id")
    public int original_id;

    @ColumnInfo(name = "today_register_day")
    public String today_register_day;

    @ColumnInfo(name = "check")
    public int check;

    @ColumnInfo(name = "path")
    public String path;

    @ColumnInfo(name = "sentence")
    public String sentence;

    @ColumnInfo(name = "word")
    public String word;

    @ColumnInfo(name = "word_index")
    public String word_index;

    @ColumnInfo(name = "cheat_sheet")
    public String cheat_sheet;

    @ColumnInfo(name = "history")
    public String history;

    @ColumnInfo(name = "register_day")
    public String register_day;

    @ColumnInfo(name = "next")
    public String next;

    @ColumnInfo(name = "index")
    public int index;

}
