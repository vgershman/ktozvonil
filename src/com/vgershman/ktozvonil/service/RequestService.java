package com.vgershman.ktozvonil.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import com.vgershman.ktozvonil.R;
import com.vgershman.ktozvonil.activity.ResultActivity;
import com.vgershman.ktozvonil.app.AppInfo;
import com.vgershman.ktozvonil.connection.Request;
import com.vgershman.ktozvonil.connection.RequestGetCallback;
import com.vgershman.ktozvonil.database.PhonesManager;

import java.util.List;

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
            Request.getInfoByNumber(phone,new RequestGetCallback() {
                @Override
                public void onInfoFound(PhoneUserInfo response) {
                      sendNotification(response,context);
                      phonesManager.deletePhone(response.getPhone());
                }

                @Override
                public void onNotFound() {
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
                .setContentTitle("Вы искали "+response.getPhone())
                .setContentText(response.getName() + '\n' + response.getEmail());
        Intent found = new Intent(context, ResultActivity.class);
        found.putExtra("actionType",1);
        found.putExtra("phone", response.getPhone());
        found.putExtra("name", response.getName());
        found.putExtra("email", response.getEmail());
        found.putExtra("operator", response.getOperator());
        found.putExtra("reqion", response.getRegion());


        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(ResultActivity.class);
        stackBuilder.addNextIntent(found);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());
    }

}
