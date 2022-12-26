package com.example.wordpro.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "team_mate")
public class TeamMate {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "nickname")
    public String nickname;
    @ColumnInfo(name = "team_study_id")
    public int team_study_id;
    @ColumnInfo(name = "progress")
    public int progress;
}
