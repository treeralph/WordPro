package com.example.wordpro.rds.dataResponse;

import com.example.wordpro.database.Sentence;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SentenceHandler {

    public static Sentence[] string2sentences(String input){
        if(input == null || input.isEmpty()){
            return null;
        }
        JSONArray jsonArray;
        try {
            JSONObject jsonObject = new JSONObject(input);
            jsonArray = jsonObject.getJSONArray("message");


        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        LocalDate today = LocalDate.now();
        String today2string = today.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        LocalDate next = today.plusDays(1);
        String next2string = next.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        Sentence[] sentences = new Sentence[jsonArray.length()];
        for(int i=0; i< jsonArray.length(); i++){
            try {
                JSONObject stuff = jsonArray.getJSONObject(i);
                Sentence sentence = new Sentence();
                sentence.path = stuff.getString("path");
                sentence.sentence = stuff.getString("sentence");
                sentence.word = stuff.getString("word");
                sentence.word_index = stuff.getString("word_index");
                sentence.cheat_sheet = stuff.getString("cheat_sheet");
                sentence.history = "";
                sentence.register_day = today2string;
                sentence.next = next2string;
                sentence.index = 0;
                sentences[i] = sentence;
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return sentences;
    }
}
