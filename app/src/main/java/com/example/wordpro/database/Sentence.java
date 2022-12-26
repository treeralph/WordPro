package com.example.wordpro.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "sentence")
public class Sentence {
    @PrimaryKey(autoGenerate = true)
    public int id;

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

    @NonNull
    @Override
    public String toString() {
        return "(" +
                String.valueOf(id) + ", " +
                sentence + ", " +
                word + ", " +
                word_index + ", " +
                history + ", " +
                register_day + ", " +
                next + ", " +
                String.valueOf(index) + ")";
    }
}
