package com.example.wordpro.rds;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Query;

import com.example.wordpro.tool.Callback;
import com.google.android.datatransport.runtime.dagger.Component;
import com.google.android.datatransport.runtime.dagger.Subcomponent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class RdsConnect {

    public static String TAG = RdsConnect.class.getName();

    public static String INSERT = "insert";
    public static String DELETE = "delete";
    public static String SELECT = "select";
    public static String UPDATE = "update";

    public static String TABLE_SENTENCES = "sentences";
    public static String TABLE_TEAMS = "teams";
    public static String TABLE_USERS = "users";

    /**
    * RDS data type
    * sentences - (id, sentence, word, word_index, cheat_sheet, path, uid)
    * users - (id, nickname, device_token)
    * teams - (id, team_name, nicknames, path)
    * INSERT, DELETE, SELECT
    * */

    // query_type, sql_query, data

    public RdsConnect() {

    }


    public void request(String flag, String table, String myQuery, String myData, Callback callback){

        String query = "https://iy99dqtiie.execute-api.ap-northeast-2.amazonaws.com/rds_connect_wordpro_stage/transactions?";

        String query_type = "query_type=" + flag;
        String table_name = "table_name=" + table;
        String sql_query = "sql_query=" + myQuery;
        String data = "data=" + myData;

        query += query_type + "&" + table_name + "&" + sql_query + "&" + data;
        Log.d(TAG, "connection query: " + query);

        try{
            BufferedReader bufferedReader;
            StringBuilder stringBuilder = new StringBuilder();

            URL url = new URL(query);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            if (conn != null) {
                conn.setConnectTimeout(3000);
                conn.setReadTimeout(3000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);

                int responseCode = conn.getResponseCode();
                Log.d(TAG, "response code: " + String.valueOf(responseCode));
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

                String result = stringBuilder.toString();
                callback.OnCallback(result);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void requestPost(JSONArray myData, Callback callback){

        String query = "https://iy99dqtiie.execute-api.ap-northeast-2.amazonaws.com/rds_connect_wordpro_stage/transactions";
        Log.d(TAG, "connection query: " + query);

        try {
            BufferedReader bufferedReader;
            StringBuilder stringBuilder = new StringBuilder();

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("queries", myData);

            String json = jsonObject.toString();
            Log.d(TAG, json);

            URL url = new URL(query);
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
