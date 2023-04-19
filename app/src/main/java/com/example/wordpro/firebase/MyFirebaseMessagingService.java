package com.example.wordpro.firebase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationManagerCompat;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.example.wordpro.R;
import com.example.wordpro.database.AppDatabase;
import com.example.wordpro.database.TeamStudy;
import com.example.wordpro.rds.RdsConnect;
import com.example.wordpro.rds.dataRequest.UpdateQuery;
import com.example.wordpro.tool.Callback;
import com.example.wordpro.tool.Cognito;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Formatter;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public static String TAG = MyFirebaseMessagingService.class.getName();

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);

        Log.d(TAG, "onMessageReceived");

        String title = message.getNotification().getTitle();
        String body = message.getNotification().getBody();

        final String CHANNEL_ID = "HEADS_UP_NOTIFICATION";
        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "Heads Up Notification",
                NotificationManager.IMPORTANCE_HIGH
        );
        getSystemService(NotificationManager.class).createNotificationChannel(channel);

        Map<String, String> payload = message.getData();
        String wFCM = payload.get("wFCM");
        if(wFCM.equals("INVITE_TEAM")){
            String teamIdentifier = payload.get("team_identifier");
            String teamName = payload.get("team_name");
            String teamRequestUser = payload.get("make_team_request_user");
            String now = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

            TeamStudy teamStudy = new TeamStudy();
            teamStudy.db_identifier = teamIdentifier;
            teamStudy.team_study_name = teamName;
            teamStudy.permission_download = "false";
            teamStudy.register_day = now;
            teamStudy.make_team_request_user = teamRequestUser;

            AppDatabase db = AppDatabase.getDBInstance(this);
            db.teamStudyDao().insertTeamStudy(teamStudy);
        }else{

        }

        Notification.Builder notification = new Notification.Builder(this, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.star_filled)
                .setAutoCancel(true);
        NotificationManagerCompat.from(this).notify(1, notification.build());
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);

        RdsConnect rdsConnect = new RdsConnect();

        Cognito cognito = new Cognito(this);
        cognito.getCurrentUserNickname(new Callback() {
            @Override
            public void OnCallback(Object object) {
                String currentUserNickname = (String) object;
                UpdateQuery updateQuery = UpdateQuery.builder()
                        .tableName("users")
                        .columnName("device_token").columnNewValue("'" + token + "'")
                        .optionField("nickname").option("'" + currentUserNickname + "'")
                        .build();
                rdsConnect.request(RdsConnect.UPDATE, RdsConnect.TABLE_USERS, updateQuery.toString(), null, new Callback() {
                    @Override
                    public void OnCallback(Object object) {
                        JSONArray jsonArray;
                        try {
                            JSONObject jsonObject = new JSONObject((String) object);
                            jsonArray = jsonObject.getJSONArray("message");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

}
