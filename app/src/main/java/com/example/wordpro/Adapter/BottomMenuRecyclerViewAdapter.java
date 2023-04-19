package com.example.wordpro.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wordpro.AddSentenceActivity;
import com.example.wordpro.MakeTeamActivity;
import com.example.wordpro.R;
import com.example.wordpro.TeamListActivity;
import com.example.wordpro.UserSentenceActivity;
import com.example.wordpro.tool.Callback;

public class BottomMenuRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public static int MAIN_ACTIVITY = 0;
    public static int TEAM_LIST_ACTIVITY = 1;

    Context context;
    Callback callback;

    int recyclerViewType;

    public BottomMenuRecyclerViewAdapter(Context context, int recyclerViewType, Callback callback) {
        this.context = context;
        this.callback = callback;
        this.recyclerViewType = recyclerViewType;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(recyclerViewType == MAIN_ACTIVITY){
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bottom_menu_for_main_activity, parent, false);
            return new ViewHolderForMainActivity(itemView);
        }else if(recyclerViewType == TEAM_LIST_ACTIVITY){
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bottom_menu_for_team_list_activity, parent, false);
            return new ViewHolderForMakeTeamActivity(itemView);
        }else{
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch(getRecyclerViewType()){
            case 0: // MainActivity
                ViewHolderForMainActivity viewHolderForMainActivity = (ViewHolderForMainActivity) holder;
                switch(holder.getAdapterPosition()){
                    case 0:
                        viewHolderForMainActivity.frameLayout.setVisibility(View.INVISIBLE);
                        callback.OnCallback(viewHolderForMainActivity.relativeLayout);
                        break;
                    case 1:
                        viewHolderForMainActivity.addSentenceButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(context, AddSentenceActivity.class);
                                context.startActivity(intent);
                            }
                        });

                        viewHolderForMainActivity.userSentenceButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(context, UserSentenceActivity.class);
                                context.startActivity(intent);
                            }
                        });

                        viewHolderForMainActivity.teamButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(context, TeamListActivity.class);
                                context.startActivity(intent);
                            }
                        });
                        break;
                    default:
                        break;
                }
                break;
            case 1: // TeamListActivity
                ViewHolderForMakeTeamActivity viewHolderForMakeTeamActivity = (ViewHolderForMakeTeamActivity) holder;
                switch(holder.getAdapterPosition()){
                    case 0:
                        viewHolderForMakeTeamActivity.frameLayout.setVisibility(View.INVISIBLE);
                        callback.OnCallback(viewHolderForMakeTeamActivity.relativeLayout);
                        break;
                    case 1:
                        viewHolderForMakeTeamActivity.makeTeamButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(context, MakeTeamActivity.class);
                                context.startActivity(intent);
                            }
                        });
                        break;
                    default:
                        break;
                }
                break;
            default:

                break;
        }


    }

    public int getRecyclerViewType(){
        return recyclerViewType;
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public class ViewHolderForMainActivity extends RecyclerView.ViewHolder{
        RelativeLayout relativeLayout;
        FrameLayout frameLayout;
        LinearLayout addSentenceButton;
        LinearLayout userSentenceButton;
        LinearLayout teamButton;
        public ViewHolderForMainActivity(@NonNull View itemView) {
            super(itemView);
            this.relativeLayout = itemView.findViewById(R.id.itemBottomMenuRelativeLayout);
            this.frameLayout = itemView.findViewById(R.id.itemBottomMenuFrameLayout);
            this.addSentenceButton = itemView.findViewById(R.id.itemBottomMenuAddSentenceButton);
            this.userSentenceButton = itemView.findViewById(R.id.itemBottomMenuUserSentenceButton);
            this.teamButton = itemView.findViewById(R.id.itemBottomMenuTeamButton);
        }
    }

    public class ViewHolderForMakeTeamActivity extends RecyclerView.ViewHolder{
        RelativeLayout relativeLayout;
        FrameLayout frameLayout;
        LinearLayout makeTeamButton;
        public ViewHolderForMakeTeamActivity(@NonNull View itemView) {
            super(itemView);
            this.relativeLayout = itemView.findViewById(R.id.itemBottomMenuForTeamListRelativeLayout);
            this.frameLayout = itemView.findViewById(R.id.itemBottomMenuForTeamListFrameLayout);
            this.makeTeamButton = itemView.findViewById(R.id.itemBottomMenuForTeamListMakeTeamButton);
        }
    }


}
