package com.smartsense.taxinearyou.push;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.mpt.storage.SharedPreferenceUtil;
import com.onesignal.OneSignal;
import com.smartsense.taxinearyou.ActivitySplash;
import com.smartsense.taxinearyou.R;
import com.smartsense.taxinearyou.TaxiNearYouApp;
import com.smartsense.taxinearyou.utill.CommonUtil;
import com.smartsense.taxinearyou.utill.Constants;

import org.json.JSONObject;

public class GcmIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;
    private static final String TAG = "Gcm";
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            Bundle extras = intent.getExtras();
            GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

            String messageType = gcm.getMessageType(intent);

            if (!extras.isEmpty()) {
                JSONObject pushDataObj = new JSONObject(extras.get("custom").toString());
                if (pushDataObj.optJSONObject("a").has("user")) {
                    CommonUtil.storeUserData(pushDataObj.optJSONObject("a").optJSONObject("user"));
                } else if (pushDataObj.optJSONObject("a").optInt("eventId") == Constants.Events.UPDATE_EMAIL) {
                    if (pushDataObj.optJSONObject("a").optInt("reqType") == 1) {
                        SharedPreferenceUtil.clear();
                        SharedPreferenceUtil.save();
                        OneSignal.sendTag("emailId", "");
                    }
                }
                Intent intentBroadCast;
                intentBroadCast = new Intent(pushDataObj.optJSONObject("a").optString("eventId"));
                intentBroadCast.putExtra(Constants.EXTRAS, pushDataObj.optJSONObject("a").toString());
                TaxiNearYouApp.getAppContext().sendBroadcast(intentBroadCast);
//                sendNotification(extras.getString("alert"));
                Log.i(TAG, "Received: " + extras.toString());
//            }
            }
            GcmBroadcastReceiver.completeWakefulIntent(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendNotification(String msg) {
        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, ActivitySplash.class), 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("GCM Notification").setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}