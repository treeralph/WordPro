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
    @ColumnInfo(name = "db_identifier")
    public String db_identifier;
    @ColumnInfo(name = "team_study_name")
    public String team_study_name;
    @ColumnInfo(name = "num_sentence")
    public int num_sentence;
    @ColumnInfo(name = "make_team_request_user")
    public String make_team_request_user;
    @ColumnInfo(name = "permission_download")
    public String permission_download;
}
