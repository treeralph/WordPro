package com.example.wordpro.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wordpro.R;

import java.util.ArrayList;
import java.util.List;

public class NicknameRecyclerViewAdapter extends RecyclerView.Adapter<NicknameRecyclerViewAdapter.NicknameViewHolder>{

    private String TAG = "NicknameRecyclerView";

    List<String> dataList;

    public NicknameRecyclerViewAdapter() {
        dataList = new ArrayList<>();
    }

    @NonNull
    @Override
    public NicknameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_word_select, parent, false);
        return new NicknameViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NicknameViewHolder holder, int position) {
        holder.mainCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "remove #" + String.valueOf(position) + " of recyclerView dataList");
                dataList.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void addItem(String data){
        dataList.add(data);
        notifyDataSetChanged();
    }

    public class NicknameViewHolder extends RecyclerView.ViewHolder{
        CardView mainCardView;
        TextView mainTextView;
        public NicknameViewHolder(@NonNull View itemView) {
            super(itemView);
            mainCardView = itemView.findViewById(R.id.itemWordSelectCardView);
            mainTextView = itemView.findViewById(R.id.itemWordSelectTextView);
        }
    }
}
