package com.vgershman.ktozvonil.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import com.vgershman.ktozvonil.R;
import com.vgershman.ktozvonil.activity.ResultActivity;
import com.vgershman.ktozvonil.connection.Request;
import com.vgershman.ktozvonil.connection.RequestCallback;
import com.vgershman.ktozvonil.dao.PhoneUserInfo;
import com.vgershman.ktozvonil.database.PhonesManager;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vgershman
 * Date: 07.01.13
 * Time: 22:57
 * To change this template use File | Settings | File Templates.
 */
public class PushService extends Service {

    PhonesManager phonesManager;
    Boolean unlimited=true;


    public int onStartCommand(Intent intent, int flags, int startId) {
        phonesManager = new PhonesManager(this);
        unlimited = getSharedPreferences("ktozvonil",MODE_PRIVATE).getBoolean("unlimited_connection",true);
        requestTask();
        return super.onStartCommand(intent, flags, startId);
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    void requestTask() {


        List<String>phones =  phonesManager.readPhoneNumbers();
        for (String phone :phones){

        Request.getInfoByNumber(phone,new RequestCallback() {
            @Override
            public void onInfoFound(PhoneUserInfo response) {
                stopSelf();
                sendNotification(response);
                phonesManager.deletePhone(response.getPhone());
            }



            @Override
            public void onNotFound() {
                requestTask();
                int gap;
                if (unlimited){
                    gap=10000;
                } else {
                    gap=60*60*1000;
                }

                try {
                    wait(gap);
                } catch (InterruptedException e) {
                    Log.e("KtoZvonilService",e.getMessage());
                }
            }

            @Override
            public void onFailure() {
                requestTask();
            }
        });
      }
    }

    private void sendNotification(PhoneUserInfo response) {
        NotificationCompat.Builder mBuilder;
        mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Вы искали "+response.getPhone())
                .setContentText(response.getName() + '\n' + response.getEmail());
        Intent found = new Intent(this, ResultActivity.class);
        found.putExtra("found",true);
        found.putExtra("phone", response.getPhone());
        found.putExtra("name", response.getName());
        found.putExtra("email", response.getEmail());
        found.putExtra("operator", response.getOperator());
        found.putExtra("reqion", response.getRegion());


        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(ResultActivity.class);
        stackBuilder.addNextIntent(found);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());
    }


}
