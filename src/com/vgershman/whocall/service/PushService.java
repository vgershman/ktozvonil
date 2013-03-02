package com.vgershman.whocall.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import com.vgershman.whocall.app.AppInfo;

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
        unlimited = getSharedPreferences(AppInfo.PREFERENCES_NAME,MODE_PRIVATE).getInt("pushSetting",1)==1;
        start();

        return super.onStartCommand(intent, flags, startId);
    }

    private void start() {

        Intent intent = new Intent(this, RequestService.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, REQUEST_CODE, intent, 0);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        int interval=60*1000;
        if(!unlimited){
            interval = 60*60*1000;
        }


        alarmManager.setRepeating(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + FIRST_RUN,
                interval,
                pendingIntent);

        //alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,SystemClock.elapsedRealtime()+interval,pendingIntent);
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
