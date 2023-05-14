package com.example.wordpro.ec2;

import android.util.Log;

import com.example.wordpro.tool.Callback;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class EC2RESTApi {

    private static String TAG = EC2RESTApi.class.getName();
    public static String API = "http://54.180.94.51:3001/api";

    public static void makeTeamAPI(String team_name, List<String> users, List<String> sql_queries, String uid, Callback callback){

        Log.e(TAG, "Team Name: " + team_name);
        Log.e(TAG, "users: " + users.toString());
        Log.e(TAG, "sql_queries: " + sql_queries.toString());
        Log.e(TAG, "uid: " + uid);

        String makeTeamQuery = API + "/team/make";
        Log.d(TAG, "EC2RESTApi makeTeam API make method called");
        try {
            BufferedReader bufferedReader;
            StringBuilder stringBuilder = new StringBuilder();

            JSONObject jsonQueries = new JSONObject();
            for(int i=0; i<sql_queries.size(); i++){
                jsonQueries.put(String.valueOf(i), sql_queries.get(i));
            }

            JSONObject jsonNicknames = new JSONObject();
            for(int i=0; i<users.size(); i++){
                jsonNicknames.put(String.valueOf(i), users.get(i));
            }

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("team_name", team_name);
            jsonObject.put("sql_queries", jsonQueries);
            jsonObject.put("users", jsonNicknames);
            jsonObject.put("make_team_request_user", uid);

            String json = jsonObject.toString();

            URL url = new URL(makeTeamQuery);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            os.write(json.getBytes(StandardCharsets.UTF_8));
            os.flush();
            os.close();

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Do something with the response
                bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                // Handle the error
                bufferedReader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }

            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                Log.w(TAG, line + "\n");
                stringBuilder.append(line + "\n");
            }

            String result = stringBuilder.toString();
            callback.OnCallback(result);

        } catch(Exception e){
            e.printStackTrace();
        }
    }

}
