package com.example.wordpro.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TeamStudyDao {
    @Insert
    public void insertTeamStudy(TeamStudy... teamStudies);
    @Delete
    public void deleteTeamStudy(TeamStudy teamStudy);
    @Update
    public void updateTeamStudy(TeamStudy... teamStudies);
    @Query("update team_study set permission_download=:permission where :option=:option_value")
    public void updateTeamStudyPermission(String permission, String option, String option_value);;
    @Query("select * from team_study")
    public List<TeamStudy> getAllTeamStudies();
    @Query("select * from team_study where team_study_name=:team_study_name")
    public List<TeamStudy> getTeamStudiesFromName(String team_study_name);
    @Query("select * from team_study where permission_download='false'")
    public List<TeamStudy> getTeamStudiesPermissionFalse();
}
