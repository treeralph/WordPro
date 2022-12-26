package com.example.wordpro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.wordpro.Adapter.TeamActivityRecyclerViewAdapter;
import com.example.wordpro.database.AppDatabase;
import com.example.wordpro.database.TeamStudy;

import java.util.List;

public class TeamActivity extends AppCompatActivity {

    CardView makeTeamButton;
    CardView chooseTeamButton;
    TextView teamStudyName;
    RecyclerView recyclerView;

    AppDatabase db;
    List<TeamStudy> teamStudies;
    TeamStudy currentTeamStudy;

    TeamActivityRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        activityViewInitializer();


    }

    public void activityViewInitializer(){
        makeTeamButton = findViewById(R.id.TeamActivityMakeTeamStudyButton);
        chooseTeamButton = findViewById(R.id.TeamActivityChooseTeamStudyButton);
        teamStudyName = findViewById(R.id.TeamActivityTeamStudyNameTextView);
        recyclerView = findViewById(R.id.TeamActivityRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = AppDatabase.getDBInstance(this);
        teamStudies = db.teamStudyDao().getAllTeamStudies();

        makeTeamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeamActivity.this, MakeTeamActivity.class);
                startActivity(intent);
            }
        });
    }

    public void refresher(int index){

        currentTeamStudy = teamStudies.get(index);






        teamStudyName.setText(currentTeamStudy.team_study_name);
    }


}