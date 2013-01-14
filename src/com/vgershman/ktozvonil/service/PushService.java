package com.vgershman.ktozvonil.service;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import com.vgershman.ktozvonil.R;
import com.vgershman.ktozvonil.activity.ResultActivity;
import com.vgershman.ktozvonil.connection.Request;
import com.vgershman.ktozvonil.connection.RequestGetCallback;
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

    //public static final int INTERVAL = 10000; // 10 sec
    public static final int FIRST_RUN = 5000; // 5 seconds
    int REQUEST_CODE = 11223344;

    AlarmManager alarmManager;
    Boolean unlimited=true;


    public int onStartCommand(Intent intent, int flags, int startId) {
        unlimited = getSharedPreferences("ktozvonil",MODE_PRIVATE).getBoolean("unlimited_connection",true);
        start();
        return super.onStartCommand(intent, flags, startId);
    }

    private void start() {
        Intent intent = new Intent(this, RequestService.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, REQUEST_CODE, intent, 0);
        int interval=60*1000;
        if(!unlimited){
            interval = 60*60*1000;
        }


        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + FIRST_RUN,
                interval,
                pendingIntent);

    }

    public IBinder onBind(Intent intent) {
        return null;
    }

        @Override
        public void onDestroy() {
            if (alarmManager != null) {
                Intent intent = new Intent(this, RequestService.class);
                alarmManager.cancel(PendingIntent.getBroadcast(this, REQUEST_CODE, intent, 0));
            }

        }

}
