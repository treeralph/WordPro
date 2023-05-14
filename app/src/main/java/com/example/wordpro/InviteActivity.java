package com.example.wordpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.room.Update;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.wordpro.Adapter.InvitationViewPagerAdapter;
import com.example.wordpro.database.AppDatabase;
import com.example.wordpro.database.TeamStudy;
import com.example.wordpro.rds.RdsConnect;
import com.example.wordpro.rds.dataRequest.UpdateQuery;
import com.example.wordpro.tool.Callback;
import com.google.gson.JsonObject;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class InviteActivity extends AppCompatActivity {

    public static String TAG = InviteActivity.class.getName();

    private int ACTIVITY_TERMINATE = 0;

    ViewPager2 viewPager;
    SpringDotsIndicator indicator;
    InvitationViewPagerAdapter adapter;
    Handler handler;

    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if(msg.what == ACTIVITY_TERMINATE){
                    Intent intent = new Intent(InviteActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                return false;
            }
        });

        activityViewInitializer();
    }

    private void activityViewInitializer(){

        Intent intent = getIntent();
        String uid = intent.getStringExtra("uid");
        String wFCM = intent.getStringExtra("wFCM");
        String teamIdentifier = intent.getStringExtra("teamIdentifier");
        String teamName = intent.getStringExtra("teamName");
        String makeRequestUser = intent.getStringExtra("makeRequestUser");

        viewPager = findViewById(R.id.inviteActivityViewPager);
        indicator = findViewById(R.id.inviteActivityIndicator);

        db = AppDatabase.getDBInstance(this);
        List<TeamStudy> teamStudies = db.teamStudyDao().getTeamStudiesPermissionFalse();
        adapter = new InvitationViewPagerAdapter(this, teamStudies, uid, new Callback() {
            @Override
            public void OnCallback(Object object) {
                Message message = new Message();
                message.what = ACTIVITY_TERMINATE;
                handler.sendMessage(message);
            }
        });

        viewPager.setAdapter(adapter);
        indicator.attachTo(viewPager);
    }

}