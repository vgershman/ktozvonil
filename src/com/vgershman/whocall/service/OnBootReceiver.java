package com.vgershman.whocall.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.vgershman.whocall.app.AppInfo;

/**
 * Created with IntelliJ IDEA.
 * User: vgershman
 * Date: 12.01.13
 * Time: 15:31
 * To change this template use File | Settings | File Templates.
 */
public class OnBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {

             TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
             tm.listen(new CallStateListener(context), PhoneStateListener.LISTEN_CALL_STATE);

            Intent serviceLauncher = new Intent(context, PushService.class);
            context.startService(serviceLauncher);
            Log.v(AppInfo.SERVICE_NAME, "Service loaded while device boot.");
        }
    }
}

