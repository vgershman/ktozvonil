package com.vgershman.whocalling.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.vgershman.whocalling.R;
import com.vgershman.whocalling.app.*;
import com.vgershman.whocalling.connection.Request;
import com.vgershman.whocalling.connection.RequestGetCallback;
import com.vgershman.whocalling.dto.PhoneUserInfo;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: vgershman
 * Date: 05.01.13
 * Time: 22:53
 * To change this template use File | Settings | File Templates.
 */
public class RequestPhoneActivity extends Activity {

    EditText phoneInput;
    Button sendButton;

    @Override
    protected void onResume() {
        super.onResume();
        sendButton.setEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_phone_activity);
        initView();
        showAddInfoDialog();
    }

    private void showAddInfoDialog() {
        final SharedPreferences sharedPreferences = getSharedPreferences(AppInfo.PREFERENCES_NAME,MODE_PRIVATE);
        boolean addedInfo = sharedPreferences.getBoolean("addedInfo",false);
        if(!addedInfo){
             int startCounter = sharedPreferences.getInt("startCounter",0);
             if(startCounter==2){
                 startCounter = 0;
                 sharedPreferences.edit().putInt("startCounter",startCounter).commit();
                 AlertDialog.Builder builder = new AlertDialog.Builder(this);
                 builder.setMessage("Пожалуйста, добавьте информацию о себе");
                 builder.setNegativeButton("Никогда",new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialogInterface, int i) {
                         sharedPreferences.edit().putBoolean("addedInfo",true);
                     }
                 });
                 builder.setNeutralButton("Позже", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialogInterface, int i) {
                         dialogInterface.dismiss();
                     }
                 });
                 builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialogInterface, int i) {
                         Intent intent = new Intent(RequestPhoneActivity.this, TellAboutMyselfActivity.class);
                         startActivity(intent);
                     }
                 });
                 builder.create().show();

             }else{
                 sharedPreferences.edit().putInt("startCounter",startCounter+1).commit();
             }

        }
    }

    private void initView() {
        phoneInput = (EditText)findViewById(R.id.phoneInput);
        sendButton = (Button)findViewById(R.id.btnSend);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkPhoneInput()){sendRequest();} else{
                    showWrongPhone();
                }
            }

            
        });

        Button fromCallLog = (Button)findViewById(R.id.fromCallLog);
        fromCallLog.setOnClickListener(new View.OnClickListener() {
            @Override

                    public void onClick(View v) {
                        String[] strFields = {android.provider.CallLog.Calls._ID,
                                android.provider.CallLog.Calls.NUMBER,
                                android.provider.CallLog.Calls.CACHED_NAME,};
                        String strOrder = android.provider.CallLog.Calls.DATE + " DESC";
                        final Cursor cursorCall = getContentResolver().query(
                                android.provider.CallLog.Calls.CONTENT_URI, strFields,
                                null, null, strOrder);


                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                RequestPhoneActivity.this);

                        android.content.DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface,
                                                int item) {
                                cursorCall.moveToPosition(item);
                                Toast.makeText(
                                        RequestPhoneActivity.this,
                                        cursorCall.getString(cursorCall
                                                .getColumnIndex(android.provider.CallLog.Calls.NUMBER)),
                                        Toast.LENGTH_LONG).show();
                                cursorCall.close();
                                return;
                            }
                        };
                        builder.setCursor(cursorCall, listener,
                                android.provider.CallLog.Calls.CACHED_NAME);
                        builder.create().show();
                    }
                });

    }



    private void showWrongPhone() {
        Toast.makeText(this,"Неверно введен номер!",2000).show();
    }

    private void sendRequest() {
        sendButton.setEnabled(false);
        final String phone = phoneInput.getText().toString();
        Request.getInfoByNumber(phone,new RequestGetCallback() {
            @Override
            public void onInfoFound(PhoneUserInfo response) {
                Intent found = new Intent(RequestPhoneActivity.this,ResultActivity.class);
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

                startActivity(found);
            }

            @Override
            public void onNotFound() {
                Intent notFound = new Intent(RequestPhoneActivity.this, ResultActivity.class);
                notFound.putExtra("actionType", 0);
                notFound.putExtra("phone",phone);
                startActivity(notFound);
            }

            @Override
            public void onFailure() {
                Toast.makeText(RequestPhoneActivity.this,"Ошибка сервера", 2000);
            }
        });

    }


    private boolean checkPhoneInput() {
        String phone = phoneInput.getText().toString();
        if(phone.equals("")){return false;}
        if(phone.charAt(0)!='7'){return false;}
        if(phone.length()<5){return false;}
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem mi = menu.add(0, 1, 0, "Настройки");
        mi.setIntent(new Intent(this, SettingsActivity.class));
        return super.onCreateOptionsMenu(menu);
    }
}
