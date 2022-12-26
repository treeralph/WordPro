package com.example.wordpro.Adapter;

import android.app.Dialog;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wordpro.R;
import com.example.wordpro.tool.Callback;

import java.util.List;
import java.util.zip.Inflater;

public class SelectDialogRecyclerViewAdapter extends RecyclerView.Adapter<SelectDialogRecyclerViewAdapter.MainViewHolder>{

    String[] dataList;
    Callback callback;
    Dialog dialog;

    public SelectDialogRecyclerViewAdapter(String[] dataList, Callback callback, Dialog dialog) {
        this.dataList = dataList;
        this.callback = callback;
        this.dialog = dialog;
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dialog_select, parent, false);
        return new MainViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        String target = dataList[position];
        holder.textView.setText(target);
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.OnCallback(target);
                dialog.dismiss();
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.length;
    }

    public class MainViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        public MainViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.itemDialogSelectTextView);
        }
    }
}
