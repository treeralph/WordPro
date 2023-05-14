package com.example.wordpro.Adapter;

import android.content.Context;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wordpro.R;
import com.example.wordpro.database.Sentence;
import com.example.wordpro.tool.Callback;

import java.util.ArrayList;
import java.util.List;

public class NormalRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public String TAG = NormalRecyclerViewAdapter.class.getName();

    public static int SENTENCE = 0;
    public static int USER = 1;
    public static int NICKNAME = 2;
    public static int PATH = 3;

    int type;
    List<Object> dataList;
    Context context;

    Callback callback;

    private int clicked = -1;

    public NormalRecyclerViewAdapter(Context context, int type, List<Object> dataList) {
        this.type = type;
        this.context = context;

        if(dataList == null){
            this.dataList = new ArrayList<>();
        }else {
            this.dataList = dataList;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(type == SENTENCE){
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sentence_for_normal, parent, false);
            return new SentenceViewHolder(itemView);
        }else if(type == NICKNAME){
            if(viewType == 0) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nickname_for_normal, parent, false);
                return new NicknameViewHolder(itemView);
            }else{
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nickname_clicked_for_normal, parent, false);
                return new NicknameClickedViewHolder(itemView);
            }
        }else if(type == PATH){
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dialog_select, parent, false);
            return new PathViewHolder(itemView);
        }else if(type == USER){

        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        int currentIndex = holder.getAdapterPosition();

        if(type == SENTENCE){
            SentenceViewHolder sentenceViewHolder = (SentenceViewHolder) holder;

            Sentence sentence = (Sentence) dataList.get(currentIndex);
            sentenceViewHolder.textView.setText(sentence.sentence);
            sentenceViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(clicked == currentIndex){
                        clicked = -1;
                        notifyDataSetChanged();
                    }else{
                        clicked = currentIndex;
                        notifyDataSetChanged();
                    }
                }
            });

            if(currentIndex == clicked){
                sentenceViewHolder.textView.setSingleLine(false);
            }

        }else if(type == NICKNAME){
            String nickname = (String) dataList.get(currentIndex);

            if(getItemViewType(holder.getAdapterPosition()) == 0){
                NicknameViewHolder nicknameViewHolder = (NicknameViewHolder) holder;
                nicknameViewHolder.textView.setText(nickname);
                nicknameViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clicked = holder.getAdapterPosition();
                        notifyDataSetChanged();
                    }
                });
            }else{
                NicknameClickedViewHolder nicknameClickedViewHolder = (NicknameClickedViewHolder) holder;
                nicknameClickedViewHolder.textView.setText(nickname);
                nicknameClickedViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clicked = -1;
                        notifyDataSetChanged();
                    }
                });
                nicknameClickedViewHolder.cancelImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clicked = -1;
                        dataList.remove(holder.getAdapterPosition());
                        notifyDataSetChanged();
                    }
                });
            }



        }else if(type == PATH){
            PathViewHolder pathViewHolder = (PathViewHolder) holder;

            String path = (String) dataList.get(holder.getAdapterPosition());
            pathViewHolder.textView.setText(path);
            pathViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.OnCallback(path);
                }
            });

        }else if(type == USER){

        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public List<Object> getDataList(){
        return dataList;
    }
    public void setDataList(List<Object> dataList){
        this.dataList = dataList;
        notifyDataSetChanged();
    }
    public void addData(Object object) {
        dataList.add(object);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if(position == clicked){
            return 1;
        }else{
            return 0;
        }
    }

    public boolean addDataWithOutDuplicate(Object object) {
        if(dataList.contains(object)){
            return false;
        }else{
            dataList.add(object);
            notifyDataSetChanged();
            return true;
        }
    }

    public void setCallbackForPath(Callback callback){
        this.callback = callback;
    }

    public class SentenceViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView textView;
        public SentenceViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.itemSentenceForNormalCardView);
            textView = itemView.findViewById(R.id.itemSentenceForNormalTextView);
        }
    }

    public class NicknameViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView textView;
        public NicknameViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.itemNicknameCardView);
            textView = itemView.findViewById(R.id.itemNicknameTextView);
        }
    }

    public class NicknameClickedViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView textView;
        ImageView cancelImageView;
        public NicknameClickedViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.itemNicknameClickedCardView);
            textView = itemView.findViewById(R.id.itemNicknameClickedTextView);
            cancelImageView = itemView.findViewById(R.id.itemNicknameClickedCancelImageView);
        }
    }

    public class PathViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView textView;
        public PathViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.itemDialogSelectCardView);
            textView = itemView.findViewById(R.id.itemDialogSelectTextView);
        }
    }

    public class UserViewHolder extends RecyclerView.ViewHolder{
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
