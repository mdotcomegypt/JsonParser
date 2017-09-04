package com.BuzzKora;

/**
 * Created by mhosam on 8/6/17.
 */

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;



/**
 * Created by Ravi Tamada on 08/08/16.
 * www.androidhive.info
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    private NotificationUtils notificationUtils;

    String nid = null;
    String tab = null;

    Intent resultIntent;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage == null)
            return;

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getBody());
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        }else{
            // If the app is in background, firebase itself handles the notification
        }
    }

    private void handleDataMessage(JSONObject json) {
        //Log.e(TAG, "push json: " + json.toString());

        try {


                JSONObject data = json.getJSONObject("data");

                String title = data.getString("title");
                String message = data.getString("message");
                boolean isBackground = data.getBoolean("is_background");
                String imageUrl = data.getString("image");
                String timestamp = data.getString("timestamp");

                JSONObject payload = data.getJSONObject("payload");


                Log.e(TAG, "title: " + title);
                Log.e(TAG, "message: " + message);
                Log.e(TAG, "isBackground: " + isBackground);
                Log.e(TAG, "payload: " + payload.toString());
                Log.e(TAG, "imageUrl: " + imageUrl);
                Log.e(TAG, "timestamp: " + timestamp);

                String type = payload.getString("type");
                nid = payload.getString("nid");


                Log.e(TAG, "type: " + type);

            if (checkSettings(type)) {


                if (!nid.equals(null)) {

                    Bundle bundle = new Bundle();


                switch (type) {
                    case "goal":
                        resultIntent = new Intent(getApplicationContext(), MatchDetailsActivity.class);
                        bundle.putString("ref", nid);
                        break;

                    case "start":
                        resultIntent = new Intent(getApplicationContext(), MatchDetailsActivity.class);
                        bundle.putString("ref", nid);
                        break;

                    case "end":
                        resultIntent = new Intent(getApplicationContext(), MatchDetailsActivity.class);
                        bundle.putString("ref", nid);
                        break;

                    case "lineup":
                        resultIntent = new Intent(getApplicationContext(), MatchDetailsActivity.class);
                        bundle.putString("ref", nid);
                        break;

                    case "matchvideo":
                        resultIntent = new Intent(getApplicationContext(), MatchDetailsActivity.class);
                        bundle.putString("ref", nid);
                        break;

                    case "video":
                        resultIntent = new Intent(getApplicationContext(), VideoActivity.class);
                        bundle.putString("nid", nid);
                        break;

                    case "news":
                        resultIntent = new Intent(getApplicationContext(), ArticleActivity.class);
                        bundle.putString("nid", nid);
                        break;

                    case "web":
                        resultIntent = new Intent(getApplicationContext(), BrowserActivity.class);
                        bundle.putString("nid", nid);
                        break;
                    default:
                        resultIntent = new Intent(getApplicationContext(), MainActivity.class);

                }





                    resultIntent.putExtras(bundle);
                }


                //resultIntent.putExtra("nid", nid);

                // check for image attachment


                if (TextUtils.isEmpty(imageUrl)) {
                    showNotificationMessage(getApplicationContext(), title, message, type, timestamp, resultIntent);
                } else {
                    // image is present, show notification with image
                    showNotificationMessageWithBigImage(getApplicationContext(), title, message, type, timestamp, resultIntent, imageUrl);
                }
            }


        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }


    /**
     * Showing notification with text only
     */
    private boolean checkSettings(String type) {

        final SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        if(type.equals("master")){
            return true;
        }

        if(!SP.getBoolean("key_notifications", true)){
            return false;
        }

        switch (type){

            case "super":
                return true;

            case "goal":
                return SP.getBoolean("key_notification_goals", true);

            case "start":
                return SP.getBoolean("key_notification_start", true);

            case "end":
                return SP.getBoolean("key_notification_end", true);

            case "lineup":
                return SP.getBoolean("key_notification_lineup", true);

            case "matchvideo":
                return SP.getBoolean("key_notification_highlights", true);

            case "video":
                return SP.getBoolean("key_notification_videos", true);

            case "news":
                return SP.getBoolean("key_notification_breakingnews", true);

            case "web":
                return SP.getBoolean("key_notification_breakingnews", true);
        }

        return true;
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String type, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, type, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String type, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, type, timeStamp, intent, imageUrl);
    }


}
