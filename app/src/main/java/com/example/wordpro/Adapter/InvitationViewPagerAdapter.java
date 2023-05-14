package com.example.wordpro.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wordpro.MainActivity;
import com.example.wordpro.R;
import com.example.wordpro.database.AppDatabase;
import com.example.wordpro.database.TeamStudy;
import com.example.wordpro.rds.RdsConnect;
import com.example.wordpro.rds.dataRequest.UpdateQuery;
import com.example.wordpro.tool.Callback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class InvitationViewPagerAdapter extends RecyclerView.Adapter<InvitationViewPagerAdapter.InvitationViewHolder>{

    private String TAG = InvitationViewPagerAdapter.class.getName();

    Context context;
    List<TeamStudy> dataList;
    String uid;
    Callback callback;

    public InvitationViewPagerAdapter(Context context, List<TeamStudy> dataList, String uid, Callback callback) {
        this.context = context;
        this.dataList = dataList;
        this.uid = uid;
        this.callback = callback;
    }

    @NonNull
    @Override
    public InvitationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_invitation, parent, false);
        return new InvitationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InvitationViewHolder holder, int position) {

        TeamStudy teamStudy = dataList.get(holder.getAdapterPosition());

        holder.nicknameTextView.setText(teamStudy.make_team_request_user);
        holder.teamNameTextView.setText(teamStudy.team_study_name);

        holder.agreeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agreeButtonAction(teamStudy.db_identifier, new Callback() {
                    @Override
                    public void OnCallback(Object object) {
                        dataList.remove(holder.getAdapterPosition());
                        notifyDataChanged();
                    }
                });
            }
        });

        holder.denyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                denyButton(teamStudy.db_identifier, new Callback() {
                    @Override
                    public void OnCallback(Object object) {
                        dataList.remove(holder.getAdapterPosition());
                        notifyDataChanged();
                    }
                });
            }
        });

    }

    public void notifyDataChanged(){
        this.notifyDataSetChanged();
        if(getItemCount() <= 0){
            callback.OnCallback("");
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class InvitationViewHolder extends RecyclerView.ViewHolder{
        TextView nicknameTextView;
        TextView teamNameTextView;
        CardView agreeButton;
        CardView denyButton;
        public InvitationViewHolder(@NonNull View itemView) {
            super(itemView);
            nicknameTextView = itemView.findViewById(R.id.ItemInvitationNickNameTextView);
            teamNameTextView = itemView.findViewById(R.id.ItemInvitationTeamNameTextView);
            agreeButton = itemView.findViewById(R.id.ItemInvitationAgreeButton);
            denyButton = itemView.findViewById(R.id.ItemInvitationDenyButton);
        }
    }

    public void agreeButtonAction(String teamIdentifier, Callback callback){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                JSONArray jsonArray = new JSONArray();
                UpdateQuery updateQuery = UpdateQuery.builder()
                        .tableName("sentences").columnName("permission").columnNewValue("true").optionField("uid").option(uid)
                        .build();
                jsonArray.put(updateQuery.toString() + " and team_identifier=" + teamIdentifier);

                RdsConnect rdsConnect = new RdsConnect();
                rdsConnect.requestPost(jsonArray, new Callback() {
                    @Override
                    public void OnCallback(Object object) {
                        try{
                            JSONObject response = new JSONObject((String) object);
                            Log.d(TAG, "Sentence Permission Set: Success: " + response.getString("message"));

                            AppDatabase db = AppDatabase.getDBInstance(context);
                            db.teamStudyDao().updateTeamStudyPermission("true", "db_identifier", teamIdentifier);

                            callback.OnCallback(null);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e(TAG, "Sentence Permission Set: Failure: " + e.toString());
                        }
                    }
                });
            }
        });
        thread.start();
    }

    public void denyButton(String teamIdentifier, Callback callback){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                JSONArray jsonArray = new JSONArray();
                UpdateQuery updateQuery = UpdateQuery.builder()
                        .tableName("sentences").columnName("permission").columnNewValue("deny").optionField("uid").option(uid)
                        .build();
                jsonArray.put(updateQuery.toString() + " and team_identifier=" + teamIdentifier);

                RdsConnect rdsConnect = new RdsConnect();
                rdsConnect.requestPost(jsonArray, new Callback() {
                    @Override
                    public void OnCallback(Object object) {
                        try{
                            JSONObject response = new JSONObject((String) object);
                            Log.d(TAG, "Sentence Permission Set: Success: " + response.getString("message"));

                            AppDatabase db = AppDatabase.getDBInstance(context);
                            db.teamStudyDao().updateTeamStudyPermission("deny", "db_identifier", teamIdentifier);

                            callback.OnCallback(null);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e(TAG, "Sentence Permission Set: Failure: " + e.toString());
                        }
                    }
                });
            }
        });
        thread.start();
    }
}
