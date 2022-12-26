package com.example.wordpro.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wordpro.R;
import com.example.wordpro.tool.Callback;

import java.util.ArrayList;
import java.util.List;

public class TeamActivityRecyclerViewAdapter extends RecyclerView.Adapter<TeamActivityRecyclerViewAdapter.ItemTeamStudyViewHolder>{

    List<Integer> itemList;
    Callback callback;

    public TeamActivityRecyclerViewAdapter(ArrayList itemList, Callback callback) {
        this.itemList = itemList;
        this.callback = callback;
    }

    @NonNull
    @Override
    public ItemTeamStudyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_team_study, parent, false);
        return new ItemTeamStudyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemTeamStudyViewHolder holder, int position) {

        holder.mainLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callback.OnCallback(null);
            }
        });


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ItemTeamStudyViewHolder extends RecyclerView.ViewHolder{
        LinearLayout mainLinearLayout;
        TextView numberTextView;
        TextView nicknameTextView;
        public ItemTeamStudyViewHolder(@NonNull View itemView) {
            super(itemView);
            mainLinearLayout = itemView.findViewById(R.id.itemTeamStudyLinearLayout);
            numberTextView = itemView.findViewById(R.id.itemTeamStudyNumberTextView);
            nicknameTextView = itemView.findViewById(R.id.itemTeamStudyNicknameTextView);
        }
    }
}
