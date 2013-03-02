package com.vgershman.whocall.app;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import com.vgershman.whocall.dto.Call;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vgershman
 * Date: 12.01.13
 * Time: 13:18
 * To change this template use File | Settings | File Templates.
 */
public class AppInfo{

    public final static String PREFERENCES_NAME = "WhoCallingPreferences";
    public final static String SERVICE_NAME= "WhoCallingPushService";
    public final static String APP_NAME = "WhoCalling";
    private static Context context;

    public static Context getContext(){
        return context;
    }

    public static void setContext(Context context1){
        context = context1;
    }

    public static List<Call> getCallHistory() {
        boolean filter = context.getSharedPreferences(PREFERENCES_NAME,Context.MODE_PRIVATE).getInt("filter",0)==1;
        String[] strFields = {android.provider.CallLog.Calls._ID,
                android.provider.CallLog.Calls.NUMBER,
                android.provider.CallLog.Calls.CACHED_NAME, CallLog.Calls.TYPE,
                CallLog.Calls.DATE};
        String strOrder = android.provider.CallLog.Calls.DATE + " DESC";
        final Cursor cursorCall = context.getContentResolver().query(
                android.provider.CallLog.Calls.CONTENT_URI, strFields,
                null, null, strOrder);

        List<Call> callList = new ArrayList<Call>();
        while (cursorCall.moveToNext()) {
            Call call = new Call();
            call.setNumber(cursorCall.getString(1));
            if (cursorCall.getInt(3) == CallLog.Calls.OUTGOING_TYPE) {
                call.setOut(true);
            } else {
                call.setOut(false);
            }

            call.setName(cursorCall.getString(2));
            call.setTime(cursorCall.getLong(4));

            if(filter && cursorCall.getString(2)!=null){
                continue;
            }
            callList.add(call);
        }
        cursorCall.close();
        return callList;
    }

    public static String getIMEI(){
        return ((TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
    }

    public static boolean isKnown(String number){

        Cursor phones = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
        while (phones.moveToNext())
        {
            String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            if(phoneNumber.contains(number) || number.contains(phoneNumber) || phoneNumber.equals(number)){
                phones.close();
                return true;
            }
        }
        phones.close();
        return false;
    }

}
