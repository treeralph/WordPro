package com.example.wordpro.tool;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.wordpro.database.Sentence;

public class SpanSentenceTool {

    private String TAG = "SpanSentenceTool";

    Context context;

    public SpanSentenceTool(Context context) {
        this.context = context;
    }

    public void inputSS2TextView(TextView textView, Sentence sentence){

        textView.setText(sentence.sentence);

        String words = sentence.word;
        String wordIndexes = sentence.word_index;

        String[] wordsList = words.split("/");
        String[] wordIndexesList = wordIndexes.split("/");

        Spannable span = (Spannable) textView.getText();
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                TextView tempTextView = (TextView) widget;
                String tempString = tempTextView.getText().toString();
                Log.d(TAG, "onClick: " + tempString);
            }
        };

        for(int i=0; i<wordsList.length; i++){
            String word = wordsList[i];
            if(word.isEmpty() || word.equals("")){
                continue;
            }
            String[] wordIndexList = wordIndexesList[i].split(",");
            int start = Integer.parseInt(wordIndexList[0]);
            int end = Integer.parseInt(wordIndexList[1]);
            span.setSpan(new BackgroundColorSpan(Color.parseColor("#FFBD12")), start, end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            span.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

        }
    }


}
