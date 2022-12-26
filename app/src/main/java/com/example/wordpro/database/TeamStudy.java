package com.example.wordpro.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "team_study")
public class TeamStudy {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "register_day")
    public String register_day;
    @ColumnInfo(name = "team_study_name")
    public String team_study_name;
}
