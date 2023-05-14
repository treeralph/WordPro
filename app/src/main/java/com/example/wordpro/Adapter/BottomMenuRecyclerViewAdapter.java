package com.example.wordpro.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
import com.example.wordpro.view.VacantLinearLayout;

public class BottomMenuRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public static int MAIN_ACTIVITY = 0;
    public static int TEAM_LIST_ACTIVITY = 1;

    public String TAG = BottomMenuRecyclerViewAdapter.class.getName();

    Context context;
    Callback callback;

    String uid;

    int recyclerViewType;

    public BottomMenuRecyclerViewAdapter(Context context, String uid, int recyclerViewType, Callback callback) {
        this.context = context;
        this.callback = callback;
        this.recyclerViewType = recyclerViewType;
        this.uid = uid;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(recyclerViewType == MAIN_ACTIVITY){
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bottom_menu_for_main_activity, parent, false);
            return new ViewHolderForMainActivity(itemView);
        }else if(recyclerViewType == TEAM_LIST_ACTIVITY){
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bottom_menu_for_team_list_activity, parent, false);
            return new ViewHolderForTeamListActivity(itemView);
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
                        viewHolderForMainActivity.relativeLayout.removeAllViews();

                        VacantLinearLayout vacantLinearLayout = new VacantLinearLayout(context);
                        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT);
                        vacantLinearLayout.setLayoutParams(params);
                        vacantLinearLayout.setCallback(callback);
                        vacantLinearLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.e(TAG, "vacantLinearLayout: clickListener");
                            }
                        });

                        vacantLinearLayout.setBackgroundColor(context.getResources().getColor(R.color.transparent));
                        viewHolderForMainActivity.relativeLayout.addView(vacantLinearLayout);

                        break;
                    case 1:
                        viewHolderForMainActivity.addSentenceButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(context, AddSentenceActivity.class);
                                intent.putExtra("uid", uid);
                                context.startActivity(intent);
                            }
                        });

                        viewHolderForMainActivity.userSentenceButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(context, UserSentenceActivity.class);
                                intent.putExtra("uid", uid);
                                context.startActivity(intent);
                            }
                        });

                        viewHolderForMainActivity.teamButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(context, TeamListActivity.class);
                                intent.putExtra("uid", uid);
                                context.startActivity(intent);
                            }
                        });
                        break;
                    default:
                        break;
                }
                break;
            case 1: // TeamListActivity
                ViewHolderForTeamListActivity viewHolderForTeamListActivity = (ViewHolderForTeamListActivity) holder;
                switch(holder.getAdapterPosition()){
                    case 0:
                        viewHolderForTeamListActivity.relativeLayout.removeAllViews();

                        VacantLinearLayout vacantLinearLayout = new VacantLinearLayout(context);
                        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT);
                        vacantLinearLayout.setLayoutParams(params);
                        vacantLinearLayout.setCallback(callback);
                        vacantLinearLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.e(TAG, "vacantLinearLayout: clickListener");
                            }
                        });

                        vacantLinearLayout.setBackgroundColor(context.getResources().getColor(R.color.transparent));
                        viewHolderForTeamListActivity.relativeLayout.addView(vacantLinearLayout);
                        break;
                    case 1:
                        viewHolderForTeamListActivity.makeTeamButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(context, MakeTeamActivity.class);
                                intent.putExtra("uid", uid);
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

    public class ViewHolderForTeamListActivity extends RecyclerView.ViewHolder{
        RelativeLayout relativeLayout;
        FrameLayout frameLayout;
        LinearLayout makeTeamButton;
        public ViewHolderForTeamListActivity(@NonNull View itemView) {
            super(itemView);
            this.relativeLayout = itemView.findViewById(R.id.itemBottomMenuForTeamListRelativeLayout);
            this.frameLayout = itemView.findViewById(R.id.itemBottomMenuForTeamListFrameLayout);
            this.makeTeamButton = itemView.findViewById(R.id.itemBottomMenuForTeamListMakeTeamButton);
        }
    }


}
