package com.example.wordpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.transition.Transition;
import android.transition.TransitionListenerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wordpro.database.TeamMate;
import com.example.wordpro.tool.Callback;
import com.example.wordpro.tool.ProgressBarChart;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TeamActivity extends AppCompatActivity {

    LinearLayout linearLayout;
    TextView textView;
    TextView teamNameTextView;
    TextView numSentTextView;
    TextView fromNameTextView;

    RecyclerView recyclerView;
    RecyclerViewAdapterForTeamMember adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        activityViewInitializer();
    }

    private void activityViewInitializer(){

        String teamName = getIntent().getStringExtra("teamName");
        String numSent = getIntent().getStringExtra("numSent");
        String fromName = getIntent().getStringExtra("fromName");

        linearLayout = findViewById(R.id.teamActivityLinearLayout);
        textView = findViewById(R.id.teamActivityTextView);
        teamNameTextView = findViewById(R.id.teamActivityTeamNameTextView);
        numSentTextView = findViewById(R.id.teamActivityNumSentTextView);
        fromNameTextView = findViewById(R.id.teamActivityFromNameTextView);
        recyclerView = findViewById(R.id.teamActivityRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        teamNameTextView.setText(teamName);
        numSentTextView.setText("#" + String.valueOf(numSent) + " sentences");
        fromNameTextView.setText("From." + fromName);

        getTeamMate(new Callback() {
            @Override
            public void OnCallback(Object object) {
                List<TeamMate> dataList = (List<TeamMate>) object;
                adapter = new RecyclerViewAdapterForTeamMember(dataList, new Callback() {
                    @Override
                    public void OnCallback(Object object) {
                        int clicked = (int) object;
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });

        Transition transition = getWindow().getEnterTransition();
        transition.addListener(new TransitionListenerAdapter() {
            @Override
            public void onTransitionEnd(Transition transition) {
                super.onTransitionEnd(transition);
                teamNameTextView.setTextColor(getResources().getColor(R.color.character_color));
                textView.setText("Team Member");
                recyclerView.setAdapter(adapter);
            }
        });
    }

    public void getTeamMate(Callback callback){

    }

    public class RecyclerViewAdapterForTeamMember extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        public int clicked = 0;

        Callback callback;
        List<TeamMate> dataList;

        public RecyclerViewAdapterForTeamMember(List<TeamMate> dataList, Callback callback) {
            this.dataList = dataList;
            this.callback = callback;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if(viewType == 0) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_team_member, parent, false);
                return new TeamMemberViewHolder(itemView);
            }else{
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_team_member_clicked, parent, false);
                return new TeamMemberClickedViewHolder(itemView);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            switch(getItemViewType(holder.getAdapterPosition())){
                case 0:
                    TeamMemberViewHolder teamMemberViewHolder = (TeamMemberViewHolder) holder;
                    TeamMate curTeamMate = dataList.get(teamMemberViewHolder.getAdapterPosition());
                    teamMemberViewHolder.nickname.setText(curTeamMate.nickname);
                    teamMemberViewHolder.progress.setText(curTeamMate.progress);
                    teamMemberViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            clicked = holder.getAdapterPosition();
                            callback.OnCallback(clicked);
                        }
                    });

                    break;
                case 1:
                    TeamMemberClickedViewHolder teamMemberClickedViewHolder = (TeamMemberClickedViewHolder) holder;
                    TeamMate curTeamMemberClicked = dataList.get(teamMemberClickedViewHolder.getAdapterPosition());
                    teamMemberClickedViewHolder.nickname.setText(curTeamMemberClicked.nickname);
                    teamMemberClickedViewHolder.progress.setText(curTeamMemberClicked.progress);
                    ProgressBarChart progressBarChart = new ProgressBarChart(getApplicationContext(), teamMemberClickedViewHolder.barChart);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + getItemViewType(holder.getAdapterPosition()));
            }
        }

        @Override
        public int getItemViewType(int position) {
            if(position == clicked){
                return 1;
            }else{
                return 0;
            }
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        public class TeamMemberViewHolder extends RecyclerView.ViewHolder{
            CardView cardView;
            TextView nickname;
            TextView progress;
            public TeamMemberViewHolder(@NonNull View itemView) {
                super(itemView);
                cardView = itemView.findViewById(R.id.itemTeamMemberCardView);
                nickname = itemView.findViewById(R.id.itemTeamMemberNicknameTextView);
                progress = itemView.findViewById(R.id.itemTeamMemberProgressTextView);
            }
        }

        public class TeamMemberClickedViewHolder extends RecyclerView.ViewHolder{
            CardView cardView;
            TextView nickname;
            TextView progress;
            BarChart barChart;
            public TeamMemberClickedViewHolder(@NonNull View itemView) {
                super(itemView);
                cardView = itemView.findViewById(R.id.itemTeamMemberClickedCardView);
                nickname = itemView.findViewById(R.id.itemTeamMemberClickedNicknameTextView);
                progress = itemView.findViewById(R.id.itemTeamMemberClickedProgressTextView);
                barChart = itemView.findViewById(R.id.itemTeamMemberClickedBarChart);
            }
        }
    }

    @Override
    public void onBackPressed() {
        textView.setText("");
        recyclerView.setAdapter(null);
        super.onBackPressed();
    }
}