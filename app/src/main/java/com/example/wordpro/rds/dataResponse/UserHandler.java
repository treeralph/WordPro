package com.example.wordpro.rds.dataResponse;

import com.example.wordpro.database.Sentence;
import com.example.wordpro.database.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class UserHandler {
    public static User[] string2users(String input){
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

        User[] users = new User[jsonArray.length()];
        for(int i=0; i< jsonArray.length(); i++){
            try {
                JSONObject stuff = jsonArray.getJSONObject(i);
                User user = new User();
                user.nickname = stuff.getString("nickname");
                user.device_token = stuff.getString("device_token");
                user.etc = stuff.getString("etc");
                users[i] = user;
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return users;
    }
}
