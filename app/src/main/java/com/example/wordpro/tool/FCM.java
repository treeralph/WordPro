package com.example.wordpro.tool;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.pinpoint.PinpointConfiguration;
import com.amazonaws.mobileconnectors.pinpoint.PinpointManager;
import com.amazonaws.mobileconnectors.pinpoint.targeting.notification.NotificationClient;
import com.amazonaws.mobileconnectors.pinpoint.targeting.notification.NotificationDetails;
import com.example.wordpro.MainActivity;
import com.example.wordpro.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.util.HashMap;

public class FCM extends FirebaseMessagingService {

    public static final String TAG = FCM.class.getSimpleName();

    // Intent action used in local broadcast
    public static final String ACTION_PUSH_NOTIFICATION = "push-notification";
    // Intent keys
    public static final String INTENT_SNS_NOTIFICATION_FROM = "from";
    public static final String INTENT_SNS_NOTIFICATION_DATA = "data";

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.d(TAG, "Registering push notifications token: " + token);
        MainActivity.getPinpointManager(getApplicationContext()).getNotificationClient().registerDeviceToken(token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "Message: " + remoteMessage.getData());

        final NotificationClient notificationClient = MainActivity.getPinpointManager(getApplicationContext()).getNotificationClient();

        final NotificationDetails notificationDetails = NotificationDetails.builder()
                .from(remoteMessage.getFrom())
                .mapData(remoteMessage.getData())
                .intentAction(NotificationClient.FCM_INTENT_ACTION)
                .build();

        NotificationClient.CampaignPushResult pushResult = notificationClient.handleCampaignPush(notificationDetails);

        if (!NotificationClient.CampaignPushResult.NOT_HANDLED.equals(pushResult)) {
            /**
             The push message was due to a Pinpoint campaign.
             If the app was in the background, a local notification was added
             in the notification center. If the app was in the foreground, an
             event was recorded indicating the app was in the foreground,
             for the demo, you will broadcast the notification to let the main
             activity display it in a dialog.
             */
            if (NotificationClient.CampaignPushResult.APP_IN_FOREGROUND.equals(pushResult)) {
                /* Create a message that will display the raw data of the campaign push in a dialog. */
                final HashMap<String, String> dataMap = new HashMap<>(remoteMessage.getData());
                broadcast(remoteMessage.getFrom(), dataMap);
            }
            return;
        }
    }

    private void broadcast(final String from, final HashMap<String, String> dataMap) {
        Intent intent = new Intent(ACTION_PUSH_NOTIFICATION);
        intent.putExtra(INTENT_SNS_NOTIFICATION_FROM, from);
        intent.putExtra(INTENT_SNS_NOTIFICATION_DATA, dataMap);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    /**
     * Helper method to extract push message from bundle.
     *
     * @param data bundle
     * @return message string from push notification
     */
    public static String getMessage(Bundle data) {
        return ((HashMap) data.get("data")).toString();
    }
}
