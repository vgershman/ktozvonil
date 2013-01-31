package com.vgershman.whocalling.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import com.vgershman.whocalling.R;
import com.vgershman.whocalling.activity.ResultActivity;
import com.vgershman.whocalling.app.AppInfo;
import com.vgershman.whocalling.connection.Request;
import com.vgershman.whocalling.connection.RequestGetCallback;
import com.vgershman.whocalling.database.PhonesManager;
import com.vgershman.whocalling.dto.NotFoundInfo;
import com.vgershman.whocalling.dto.PhoneUserInfo;

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
            Request.getInfoByNumber(phone,new RequestGetCallback() {
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
                .setContentTitle("Вы искали "+response.getPhone())
                .setContentText(String.valueOf(response.getInfo().get("name")));
        Intent found = new Intent(context, ResultActivity.class);
        found.putExtra("actionType",1);
        found.putExtra("phone", response.getPhone());
        Map<String,Object> info = response.getInfo();
        for(String key:info.keySet()){
            Object value = info.get(key);
            if(value instanceof List<?>){
                List<String> strings =(List<String>)value;
                StringBuilder builder=new StringBuilder();
                for(String s:strings){
                    builder.append(s);
                    builder.append(" ,");
                }
                builder.deleteCharAt(builder.length()-1);
                found.putExtra(key, builder.toString());
            }else{
                found.putExtra(key,String.valueOf(value));
            }
        }


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
