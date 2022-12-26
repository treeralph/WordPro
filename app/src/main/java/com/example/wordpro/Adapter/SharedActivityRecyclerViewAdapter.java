package com.example.wordpro.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wordpro.R;
import com.example.wordpro.database.Content;

import java.util.ArrayList;
import java.util.List;

public class SharedActivityRecyclerViewAdapter extends RecyclerView.Adapter<SharedActivityRecyclerViewAdapter.MainViewHolder> {

    private List<Content> dataList;

    public SharedActivityRecyclerViewAdapter(List<Content> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shared_recyclerview, parent, false);
        return new MainViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {

        Content current = dataList.get(position);
        holder.titleTextView.setText(current.content_title);
        holder.categoryTextView.setText(current.content_category);
        holder.timeTextView.setText(current.register_time);
        holder.nicknameTextView.setText(current.register_user);
        holder.downloadsNumTextView.setText(current.downloads_num);

        // todo: intent - sharedActivity -> contentActivity
        holder.mainLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MainViewHolder extends RecyclerView.ViewHolder {

        LinearLayout mainLinearLayout;
        TextView titleTextView;
        TextView categoryTextView;
        TextView timeTextView;
        TextView nicknameTextView;
        TextView downloadsNumTextView;

        public MainViewHolder(@NonNull View itemView) {
            super(itemView);

            mainLinearLayout = itemView.findViewById(R.id.itemSharedLinearLayout);
            titleTextView = itemView.findViewById(R.id.itemSharedContentTitleTextView);
            categoryTextView = itemView.findViewById(R.id.itemSharedContentCategoryTextView);
            timeTextView = itemView.findViewById(R.id.itemSharedTimeTextView);
            nicknameTextView = itemView.findViewById(R.id.itemSharedNicknameTextView);
            downloadsNumTextView = itemView.findViewById(R.id.itemSharedDownloadsNumTextView);
        }
    }
}
