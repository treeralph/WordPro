package com.example.wordpro.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TeamMateDao {
    @Update
    public void updateTeamMate(TeamMate... teamMates);
    @Insert
    public void insertTeamMates(TeamMate... teamMates);
    @Delete
    public void deleteTeamMate(TeamMate teamMate);
    @Query("select * from team_mate")
    public List<TeamMate> getAllTeamMate();
    @Query("select * from team_mate where team_study_id == :team_study_id")
    public List<TeamMate> getAllTeamMateByTeamStudyId(int team_study_id);
    @Query("select * from team_mate where team_study_id == :team_study_id and nickname == :nickname")
    public List<TeamMate> getTeamMateByTeamStudyIdAndNickname(int team_study_id, String nickname);
}
