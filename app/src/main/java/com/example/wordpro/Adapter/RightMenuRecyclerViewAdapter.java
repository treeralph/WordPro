package com.example.wordpro.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wordpro.R;
import com.example.wordpro.tool.Callback;


public class RightMenuRecyclerViewAdapter extends RecyclerView.Adapter<RightMenuRecyclerViewAdapter.MyViewHolder> {

    String cheatSheet = "";
    Callback callback;

    public RightMenuRecyclerViewAdapter(Callback callback) {
        this.callback = callback;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_right_menu, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        switch (holder.getAdapterPosition()){
            case 0:
                holder.cardView.setVisibility(View.INVISIBLE);
                holder.textView.setText("");
                holder.cardView.setCardBackgroundColor(Color.TRANSPARENT);
                callback.OnCallback(holder.relativeLayout);
                break;
            case 1:
                holder.textView.setText(cheatSheet);
                break;
            default:
                break;
        }
    }

    public void setCheatSheet(String cheatSheet){
        this.cheatSheet = cheatSheet;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout relativeLayout;
        CardView cardView;
        TextView textView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.relativeLayout = itemView.findViewById(R.id.itemRightMenuRelativeLayout);
            this.cardView = itemView.findViewById(R.id.itemRightMenuCardView);
            this.textView = itemView.findViewById(R.id.itemRightMenuTextView);
        }
    }

}
