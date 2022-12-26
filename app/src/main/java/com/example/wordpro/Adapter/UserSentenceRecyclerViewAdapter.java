package com.example.wordpro.Adapter;

import android.graphics.Color;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wordpro.R;
import com.example.wordpro.database.Sentence;

import java.util.ArrayList;
import java.util.List;


public class UserSentenceRecyclerViewAdapter extends RecyclerView.Adapter<UserSentenceRecyclerViewAdapter.UserSentenceViewHolder>{

    List<Sentence> sentences;

    public UserSentenceRecyclerViewAdapter(List<Sentence> sentences) {
        this.sentences = sentences;
    }

    @NonNull
    @Override
    public UserSentenceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_sentence, parent, false);
        return new UserSentenceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserSentenceViewHolder holder, int position) {

        Sentence sentence = sentences.get(position);
        holder.sentenceTextView.setText(sentence.sentence);
        holder.categoryTextView.setText(sentence.path);
        holder.timeTextView.setText(sentence.register_day);


        Spannable span = (Spannable)holder.sentenceTextView.getText();
        String ingredient = sentence.word_index;
        for(String ing: ingredient.split("/")){
            if(ing.equals("") || ing.isEmpty()){
                continue;
            }
            String[] in = ing.split(",");
            int start = Integer.parseInt(in[0]);
            int end = Integer.parseInt(in[1]);
            span.setSpan(new BackgroundColorSpan(Color.parseColor("#FFBD12")), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    @Override
    public int getItemCount() {
        return sentences.size();
    }

    public class UserSentenceViewHolder extends RecyclerView.ViewHolder{
        TextView sentenceTextView;
        TextView categoryTextView;
        TextView timeTextView;
        public UserSentenceViewHolder(@NonNull View itemView) {
            super(itemView);
            sentenceTextView = itemView.findViewById(R.id.itemUserSentenceSentenceTextView);
            categoryTextView = itemView.findViewById(R.id.itemUserSentenceSentenceCategoryTextView);
            timeTextView = itemView.findViewById(R.id.itemUserSentenceSentenceTimeTextView);
        }
    }
}
