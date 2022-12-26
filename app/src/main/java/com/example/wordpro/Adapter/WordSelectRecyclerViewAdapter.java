package com.example.wordpro.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wordpro.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class WordSelectRecyclerViewAdapter extends RecyclerView.Adapter<WordSelectRecyclerViewAdapter.WordSelectViewHolder>{

    List<String> wordList = new ArrayList<>();
    int[] checkList;

    public WordSelectRecyclerViewAdapter(List<String> wordList) {
        this.wordList = wordList;
        checkList = new int[wordList.size()];
        for(int i=0; i< wordList.size(); i++){
            checkList[i] = 0;
        }
    }

    @NonNull
    @Override
    public WordSelectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_word_select, parent, false);
        return new WordSelectViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WordSelectViewHolder holder, int position) {
        String targetWord = wordList.get(position);
        holder.wordTextView.setText(targetWord);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkList[position] == 0){
                    checkList[position] = 1;
                    holder.cardView.setCardBackgroundColor(Color.parseColor("#FF5512"));
                }else{
                    checkList[position] = 0;
                    holder.cardView.setCardBackgroundColor(Color.parseColor("#FFBD12"));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return wordList.size();
    }

    public String getWordsIndex(){
        String wordsIndex = "";
        int total = 0;
        for(int i=0; i< checkList.length; i++){
            if(checkList[i] == 1){
                String wordIndex = String.valueOf(total) + "," + String.valueOf(total + wordList.get(i).length()) + "/";
                wordsIndex += wordIndex;
            }
            total += wordList.get(i).length() + 1;
        }
        return wordsIndex;
    }
    public String getWords(){
        String words = "";
        for(int i=0; i< checkList.length; i++){
            if(checkList[i] == 1){
                words += wordList.get(i) + "/";
            }
        }
        return words;
    }

    public class WordSelectViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView wordTextView;
        public WordSelectViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.itemWordSelectCardView);
            wordTextView = itemView.findViewById(R.id.itemWordSelectTextView);
        }
    }
}
