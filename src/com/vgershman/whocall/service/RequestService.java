package com.vgershman.whocall.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import com.vgershman.whocall.R;
import com.vgershman.whocall.activity.ResultActivity;
import com.vgershman.whocall.app.AppInfo;
import com.vgershman.whocall.connection.*;
import com.vgershman.whocall.connection.RequestGetCallback;
import com.vgershman.whocall.database.PhonesManager;
import com.vgershman.whocall.dto.NotFoundInfo;
import com.vgershman.whocall.dto.PhoneUserInfo;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: vgershman
 * Date: 12.01.13
 * Time: 15:24
 * To change this template use File | Settings | File Templates.
 */
public class RequestService extends BroadcastReceiver {


    @Override
    public void onReceive(final Context context, Intent intent) {
        final PhonesManager phonesManager = new PhonesManager(context);
        List<String>phones = phonesManager.readPhoneNumbers();
        for (String phone:phones){
            Request.getInfoByNumber(phone,"push",new RequestGetCallback() {
                @Override
                public void onInfoFound(PhoneUserInfo response) {
                      sendNotification(response,context);
                      phonesManager.deletePhone(response.getPhone());
                }

                @Override
                public void onNotFound(NotFoundInfo response) {
                    Log.i(AppInfo.SERVICE_NAME,"NotFound");
                }

                @Override
                public void onFailure() {
                    Log.e(AppInfo.SERVICE_NAME, "OnFailure");
                }
            });
        }

        Log.v(this.getClass().getName(), "Timed alarm onReceive() started at time: " + new java.sql.Timestamp(System.currentTimeMillis()).toString());
    }

    private void sendNotification(PhoneUserInfo response,Context context) {
        NotificationCompat.Builder mBuilder;
        mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(context.getString(R.string.notification) + response.getPhone())
                .setContentText(String.valueOf(response.getInfo().get("name")));
        Intent found = new Intent(context, ResultActivity.class);
        found.putExtra("phone", response.getPhone());
        Map<String,Object>info = response.getInfo();
        int i = 0;
        for(String keyName : info.keySet()){
            found.putExtra("key"+i,keyName);
            found.putExtra(keyName,(String)info.get(keyName));
            i++;
        }

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(ResultActivity.class);
        stackBuilder.addNextIntent(found);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setAutoCancel(true);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());
    }

}
