package com.example.wordpro.tool;

import android.util.Log;

import com.example.wordpro.database.Sentence;
import com.example.wordpro.database.TeamMate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class RdsConnection {

    private String TAG = "RdsConnection";

    public void getTeamMatesOfRDS(int id, Callback callback){
        try{
            BufferedReader bufferedReader;
            StringBuilder stringBuilder = new StringBuilder();

            // todo: make REST API for TeamMates
            String query = "https://w7ubdmxlsf.execute-api.ap-northeast-2.amazonaws.com/test/transactions?id=" + id ;

            URL url = new URL(query);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            if (conn != null) {

                conn.setConnectTimeout(3000);
                conn.setReadTimeout(3000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);

                int responseCode = conn.getResponseCode();
                if (responseCode == 200) {
                    bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                } else {
                    bufferedReader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                }

                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    Log.w(TAG, line + "\n");
                    stringBuilder.append(line + "\n");
                }
                callback.OnCallback(string2sentences(stringBuilder.toString()));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    // todo: get Contents of RDS
    public void getContentOfRDS(String search, Callback callback){
        try{
            BufferedReader bufferedReader;
            StringBuilder stringBuilder = new StringBuilder();

            String query = "https://w7ubdmxlsf.execute-api.ap-northeast-2.amazonaws.com/test/transactions?uid=" + search ;

            URL url = new URL(query);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            if (conn != null) {

                conn.setConnectTimeout(3000);
                conn.setReadTimeout(3000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);

                int responseCode = conn.getResponseCode();
                if (responseCode == 200) {
                    bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                } else {
                    bufferedReader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                }

                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    Log.w(TAG, line + "\n");
                    stringBuilder.append(line + "\n");
                }
                callback.OnCallback(string2sentences(stringBuilder.toString()));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void getSentenceOfRDS(String uid, Callback callback){
        try{
            BufferedReader bufferedReader;
            StringBuilder stringBuilder = new StringBuilder();

            String query = "https://w7ubdmxlsf.execute-api.ap-northeast-2.amazonaws.com/test/transactions?uid=" + uid ;

            URL url = new URL(query);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            if (conn != null) {

                conn.setConnectTimeout(3000);
                conn.setReadTimeout(3000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);

                int responseCode = conn.getResponseCode();
                if (responseCode == 200) {
                    bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                } else {
                    bufferedReader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                }

                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    Log.w(TAG, line + "\n");
                    stringBuilder.append(line + "\n");
                }
                callback.OnCallback(string2sentences(stringBuilder.toString()));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void makeTeamRequest(String teamName, List<String> nicknames, String path, Callback callback){

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("team_name", teamName);
            jsonObject.put("nicknames", new JSONArray(nicknames));
            jsonObject.put("path", path);

            BufferedReader bufferedReader;
            StringBuilder stringBuilder = new StringBuilder();

            String query = "https://xzhnfxwlyc.execute-api.ap-northeast-2.amazonaws.com/execute/transactions?team=" + jsonObject.toString();

            URL url = new URL(query);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            if (conn != null) {

                conn.setConnectTimeout(3000);
                conn.setReadTimeout(3000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);

                int responseCode = conn.getResponseCode();
                if (responseCode == 200) {
                    bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                } else {
                    bufferedReader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                }

                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    Log.w(TAG, line + "\n");
                    stringBuilder.append(line + "\n");
                }
                callback.OnCallback(stringBuilder.toString());
            }
        } catch(Exception e){
            e.printStackTrace();
        }



    }

    public TeamMate[] string2teamMates(String input){
        if(input == null || input.isEmpty()){
            return null;
        }
        JSONArray jsonArray;
        try {
            JSONObject jsonObject = new JSONObject(input);
            jsonArray = jsonObject.getJSONArray("team_mates");

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        LocalDate today = LocalDate.now();
        String today2string = today.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        LocalDate next = today.plusDays(1);
        String next2string = next.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        TeamMate[] teamMates = new TeamMate[jsonArray.length()];
        for(int i=0; i< jsonArray.length(); i++){
            try {
                JSONObject stuff = jsonArray.getJSONObject(i);
                TeamMate teamMate = new TeamMate();
                teamMate.team_study_id = stuff.getInt("team_study_id");
                teamMate.nickname = stuff.getString("nickname");
                teamMate.progress = stuff.getInt("progress");
                teamMates[i] = teamMate;
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return teamMates;
    }

    public Sentence[] string2sentences(String input){
        if(input == null || input.isEmpty()){
            return null;
        }
        JSONArray jsonArray;
        try {
            JSONObject jsonObject = new JSONObject(input);
            jsonArray = jsonObject.getJSONArray("sentences");

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
