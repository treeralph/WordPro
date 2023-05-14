package com.example.wordpro.Adapter;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wordpro.R;
import com.example.wordpro.database.Sentence;

import java.util.ArrayList;
import java.util.List;


public class UserSentenceRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    List<Sentence> sentences;
    int clicked = -1;

    public UserSentenceRecyclerViewAdapter(List<Sentence> sentences) {
        this.sentences = sentences;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        switch(viewType){
            case 0:
                View itemView0 = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_sentence, parent, false);
                return new UserSentenceViewHolder(itemView0);
            case 1:
                View itemView1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_sentence_clicked, parent, false);
                return new UserSentenceClickViewHolder(itemView1);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Sentence sentence = sentences.get(holder.getAdapterPosition());

        switch (getItemViewType(holder.getAdapterPosition())){
            case 0:
                UserSentenceViewHolder holder0 = (UserSentenceViewHolder) holder;

                holder0.sentenceTextView.setText(sentence.sentence);
                SpannableStringBuilder span0 = new SpannableStringBuilder(holder0.sentenceTextView.getText());
                String ingredient0 = sentence.word_index;
                for(String ing: ingredient0.split("/")){
                    if(ing.equals("") || ing.isEmpty()){
                        continue;
                    }
                    String[] in = ing.split(",");
                    int start = Integer.parseInt(in[0]);
                    int end = Integer.parseInt(in[1]);
                    span0.setSpan(new ForegroundColorSpan(Color.parseColor("#FF6868")), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }

                holder0.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clicked = holder.getAdapterPosition();
                        notifyDataSetChanged();
                    }
                });
                break;
            case 1:
                UserSentenceClickViewHolder holder1 = (UserSentenceClickViewHolder) holder;

                holder1.sentenceTextView.setText(sentence.sentence);
                holder1.pathTextView.setText(sentence.path);
                holder1.historyTextView.setText(sentence.history);
                holder1.timeTextView.setText(sentence.register_day);
                SpannableStringBuilder span1 = new SpannableStringBuilder(holder1.sentenceTextView.getText());
                String ingredient1 = sentence.word_index;
                for(String ing: ingredient1.split("/")){
                    if(ing.equals("") || ing.isEmpty()){
                        continue;
                    }
                    String[] in = ing.split(",");
                    int start = Integer.parseInt(in[0]);
                    int end = Integer.parseInt(in[1]);
                    span1.setSpan(new ForegroundColorSpan(Color.parseColor("#FF6868")), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }

                holder1.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clicked = -1;
                        notifyDataSetChanged();
                    }
                });

                break;
            default:
                break;
        }



    }

    public void setDataList(List<Sentence> sentences){
        this.sentences = sentences;
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

    @Override
    public int getItemCount() {
        return sentences.size();
    }

    public class UserSentenceViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView sentenceTextView;
        public UserSentenceViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.itemUserSentenceCardView);
            sentenceTextView = itemView.findViewById(R.id.itemUserSentenceTextView);
        }
    }

    public class UserSentenceClickViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView sentenceTextView;
        TextView timeTextView;
        TextView pathTextView;
        TextView historyTextView;
        public UserSentenceClickViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.itemUserSentenceClickedCardView);
            sentenceTextView = itemView.findViewById(R.id.itemUserSentenceClickedTextView);
            timeTextView = itemView.findViewById(R.id.itemUserSentenceClickedTimeTextView);
            pathTextView = itemView.findViewById(R.id.itemUserSentenceClickedPathTextView);
            historyTextView = itemView.findViewById(R.id.itemUserSentenceClickedHistoryTextView);
        }
    }

}
