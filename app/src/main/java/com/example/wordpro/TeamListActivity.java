package com.example.wordpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.wordpro.Adapter.BottomMenuRecyclerViewAdapter;
import com.example.wordpro.database.AppDatabase;
import com.example.wordpro.database.TeamStudy;
import com.example.wordpro.tool.Callback;
import com.example.wordpro.view.BottomMenuRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TeamListActivity extends AppCompatActivity {

    public String TAG = TeamListActivity.class.getName();

    LinearLayout behindLinearLayout;
    LinearLayout bottomLinearLayout;
    CardView touchCardView;
    RecyclerView recyclerView;
    TeamListRecyclerViewAdapter adapter;

    AppDatabase db;
    List<TeamStudy> teamStudies;
    String uid;

    ViewPager2 viewPager;
    BottomMenuRecyclerViewAdapter bottomMenuRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_list);

        db = AppDatabase.getDBInstance(this);
        uid = getIntent().getStringExtra("uid");

        activityViewInitializer();
    }

    public void activityViewInitializer(){

        behindLinearLayout = findViewById(R.id.teamListActivityBehindLinearLayout);
        bottomLinearLayout = findViewById(R.id.teamListActivityBottomLinearLayout);
        touchCardView = findViewById(R.id.teamListActivityCardView);
        recyclerView = findViewById(R.id.teamListActivityRecyclerView);
        viewPager = findViewById(R.id.teamListActivityViewPager);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        teamStudies = db.teamStudyDao().getAllTeamStudies();
        adapter = new TeamListRecyclerViewAdapter(this, teamStudies);
        recyclerView.setAdapter(adapter);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        bottomMenuRecyclerViewAdapter = new BottomMenuRecyclerViewAdapter(this, uid, BottomMenuRecyclerViewAdapter.TEAM_LIST_ACTIVITY,  new Callback() {
            @Override
            public void OnCallback(Object object) {
                MotionEvent me = (MotionEvent) object;
                Log.e(TAG, "rearLinearLayout: dispatchTouchEvent: " + me.toString());

                float refinedY = me.getY() + bottomLinearLayout.getY();
                me.setLocation(me.getX(), refinedY);
                Log.e(TAG, "rearLinearLayout: dispatchTouchEvent: refined: " + me.toString());
                Log.e(TAG, "rearLinearLayout: dispatchTouchEvent: height: " + height);


                behindLinearLayout.dispatchTouchEvent(me);
            }
        });

        viewPager.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        viewPager.setAdapter(bottomMenuRecyclerViewAdapter);

        viewPager.setClipToPadding(false);
        viewPager.setClipChildren(false);
        viewPager.setOverScrollMode(View.OVER_SCROLL_NEVER);
        viewPager.setOffscreenPageLimit(2);

        /*
        Log.e(TAG, "ViewPager: childAt(0): " + viewPager.getChildAt(0).toString());
        RecyclerView LABRecyclerView = (RecyclerView) viewPager.getChildAt(0);
        viewPager.setOnGenericMotionListener(new View.OnGenericMotionListener() {
            @Override
            public boolean onGenericMotion(View v, MotionEvent event) {
                Log.e(TAG, "View: " + v.toString() + "\nMotionEvent: " + event.toString());
                return false;
            }
        });

        viewPager.setMotionEventSplittingEnabled(false);

         */


        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                if (position < 0) {
                    // 페이지가 왼쪽으로 스크롤될 때
                    page.setTranslationY(-80f * position);
                } else if (position <= 1) {
                    // 페이지가 오른쪽으로 스크롤될 때
                    page.setTranslationY(-80f * position);
                } else {
                    // 페이지가 정중앙에 있을 때
                    page.setTranslationY(0);
                }
            }
        });

        viewPager.setPageTransformer(compositePageTransformer);





    }

    public class TeamListRecyclerViewAdapter extends RecyclerView.Adapter<TeamListRecyclerViewAdapter.TeamListViewHolder>{

        Context context;
        List<TeamStudy> dataList;

        public TeamListRecyclerViewAdapter(Context context, List<TeamStudy> dataList) {
            this.context = context;
            this.dataList = dataList;
        }

        @NonNull
        @Override
        public TeamListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_team_list_team, parent, false);
            return new TeamListViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull TeamListViewHolder holder, int position) {

            TeamStudy currentTeam = dataList.get(holder.getAdapterPosition());
            holder.teamNameTextView.setText(currentTeam.team_study_name);
            holder.numSentTextView.setText("#" + String.valueOf(currentTeam.num_sentence) + " sentences");
            holder.fromNameTextView.setText("From." + currentTeam.make_team_request_user);
            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, TeamActivity.class);

                    Pair<View, String>[] pairs = new Pair[4];
                    pairs[0] = new Pair<View, String>(holder.linearLayout, "linearLayout");
                    pairs[1] = new Pair<View, String>(holder.teamNameTextView, "teamNameTextView");
                    pairs[2] = new Pair<View, String>(holder.numSentTextView, "numSentTextView");
                    pairs[3] = new Pair<View, String>(holder.fromNameTextView, "fromNameTextView");
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) context, pairs);
                    //ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(context, imageView, "loading2signIn");

                    intent.putExtra("teamName", currentTeam.team_study_name);
                    intent.putExtra("numSent", String.valueOf(currentTeam.num_sentence));
                    intent.putExtra("fromName", currentTeam.make_team_request_user);
                    startActivity(intent, options.toBundle());
                }
            });

        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        public class TeamListViewHolder extends RecyclerView.ViewHolder {
            LinearLayout linearLayout;
            TextView teamNameTextView;
            TextView numSentTextView;
            TextView fromNameTextView;
            public TeamListViewHolder(@NonNull View itemView) {
                super(itemView);
                linearLayout = itemView.findViewById(R.id.itemTeamListTeamLinearLayout);
                teamNameTextView = itemView.findViewById(R.id.itemTeamListTeamNameTextView);
                numSentTextView = itemView.findViewById(R.id.itemTeamListTeamNumSentenceTextView);
                fromNameTextView = itemView.findViewById(R.id.itemTeamListTeamNicknameTextView);
            }
        }
    }

}