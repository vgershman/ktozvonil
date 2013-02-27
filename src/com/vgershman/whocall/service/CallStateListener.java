package com.vgershman.whocall.service;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;
import com.vgershman.whocall.R;
import com.vgershman.whocall.app.AppInfo;
import com.vgershman.whocall.connection.*;
import com.vgershman.whocall.connection.RequestGetCallback;
import com.vgershman.whocall.database.FieldsNameStorage;
import com.vgershman.whocall.dto.NotFoundInfo;
import com.vgershman.whocall.dto.PhoneUserInfo;

/**
 * Created with IntelliJ IDEA.
 * User: User
 * Date: 02.02.13
 * Time: 21:16
 * To change this template use File | Settings | File Templates.
 */
public class CallStateListener extends PhoneStateListener {


    private boolean showInfo=false;
    private Toast current;

    public CallStateListener(Context context) {
        this.context = context;
        Thread t = new Thread() {
            public void run() {
                while (true){
                try {
                    if (showInfo&current!=null) {
                        current.show();
                        sleep(3000);
                    }
                } catch (Exception e) {
                }
            }
            }
        };
        t.start();
    }

   final private Context context;

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        if(context.getSharedPreferences(AppInfo.PREFERENCES_NAME,Context.MODE_PRIVATE).getInt("callInfo",0)==1){super.onCallStateChanged(state,incomingNumber);return;};
        if(context.getSharedPreferences(AppInfo.PREFERENCES_NAME,Context.MODE_PRIVATE).getInt("callInfo",0)==2){
             if(AppInfo.isKnown(incomingNumber)){super.onCallStateChanged(state,incomingNumber);return;};
        }
        switch (state) {

            case TelephonyManager.CALL_STATE_RINGING: {
                if(Request.isOnline(context)){

                Request.getInfoByNumber(incomingNumber,"call",new RequestGetCallback() {
                    @Override
                    public void onInfoFound(PhoneUserInfo response) {
                        StringBuilder builder = new StringBuilder();
                        for (String key:response.getInfo().keySet()){
                            if(response.getInfo().get(key)!=null){
                                builder.append(FieldsNameStorage.getFieldName(key)+": "+response.getInfo().get(key)+"\n");
                            }
                        }
                        showInfo = true;
                        current = Toast.makeText(context,builder.toString(),Toast.LENGTH_LONG);
                    }

                    @Override
                    public void onNotFound(NotFoundInfo info) {
                        StringBuilder builder = new StringBuilder();
                        if(info.getRegion()!=null){
                            builder.append(context.getText(R.string.region)+ " "+ info.getRegion()+"\n");
                        }
                        if(info.getOperator()!=null){
                            builder.append(context.getText(R.string.operator)+" "+ info.getOperator()+"\n");
                        }
                        showInfo = true;
                        current = Toast.makeText(context,builder.toString(),Toast.LENGTH_LONG);
                    }

                    @Override
                    public void onFailure() {
                        showInfo = true;
                        current = Toast.makeText(context,"Ошибка сервера",Toast.LENGTH_LONG);
                    }
                });

                break; }
            }
            case TelephonyManager.CALL_STATE_IDLE: {showInfo=false; current=null; break;}
        }
    }
}