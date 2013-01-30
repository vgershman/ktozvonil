package com.vgershman.whocalling.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.CallLog;
import android.view.*;
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
        final SharedPreferences sharedPreferences = getSharedPreferences(AppInfo.PREFERENCES_NAME, MODE_PRIVATE);
        boolean addedInfo = sharedPreferences.getBoolean("addedInfo", false);
        if (!addedInfo) {
            int startCounter = sharedPreferences.getInt("startCounter", 0);
            if (startCounter == 2) {
                startCounter = 0;
                sharedPreferences.edit().putInt("startCounter", startCounter).commit();
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Пожалуйста, добавьте информацию о себе");
                builder.setNegativeButton("Никогда", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sharedPreferences.edit().putBoolean("addedInfo", true);
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
                        Intent intent = new Intent(RequestPhoneActivity.this, TypeActivity.class);
                        intent.putExtra("self",true);
                        startActivity(intent);
                    }
                });
                builder.create().show();

            } else {
                sharedPreferences.edit().putInt("startCounter", startCounter + 1).commit();
            }

        }
    }

    private void initView() {
        phoneInput = (EditText) findViewById(R.id.phoneInput);
        sendButton = (Button) findViewById(R.id.btnSend);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPhoneInput()) {
                    sendRequest();
                } else {
                    showWrongPhone();
                }
            }


        });
        boolean addedInfo = getSharedPreferences(AppInfo.PREFERENCES_NAME, MODE_PRIVATE).getBoolean("addedInfo", false);

        if(!addedInfo){
            Button tellAboutMyself = (Button)findViewById(R.id.aboutMyself);
            tellAboutMyself.setVisibility(View.VISIBLE);
            tellAboutMyself.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(RequestPhoneActivity.this, TypeActivity.class);
                    intent.putExtra("self",true);
                    startActivity(intent);
                }
            });
        }

        Button fromCallLog = (Button) findViewById(R.id.fromCallLog);
        fromCallLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] strFields = {android.provider.CallLog.Calls._ID,
                        android.provider.CallLog.Calls.NUMBER,
                        android.provider.CallLog.Calls.CACHED_NAME, CallLog.Calls.TYPE};
                String strOrder = android.provider.CallLog.Calls.DATE + " DESC";
                final Cursor cursorCall = getContentResolver().query(
                        android.provider.CallLog.Calls.CONTENT_URI, strFields,
                        CallLog.Calls.CACHED_NAME+" is null", null, strOrder);

                AlertDialog.Builder builder = new AlertDialog.Builder(
                        RequestPhoneActivity.this);

                builder.setInverseBackgroundForced(false);
                final CustomCursorAdapter customCursorAdapter = new CustomCursorAdapter(RequestPhoneActivity.this, cursorCall);
                builder.setAdapter(customCursorAdapter,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String phone = ((Cursor)customCursorAdapter.getItem(i)).getString(1);
                        phoneInput.setText(phone);
                    }
                });


                builder.create().show();
            }
        });

    }

    class CustomCursorAdapter extends CursorAdapter{

        Bitmap incoming;
        Bitmap outgoing;
        Bitmap missed;


        CustomCursorAdapter(Context context, Cursor c) {
            super(context, c);
            missed = BitmapFactory.decodeResource(getResources(),android.R.drawable.sym_call_missed);
            outgoing = BitmapFactory.decodeResource(getResources(),android.R.drawable.sym_call_outgoing);
            incoming = BitmapFactory.decodeResource(getResources(),android.R.drawable.sym_call_incoming);
        }


        @Override
        public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
            final View view = LayoutInflater.from(context).inflate(R.layout.item, viewGroup, false);
            return view;
        }
        @Override
        public void bindView(View view, Context context, Cursor cursor) {
             TextView textItem = (TextView)view.findViewById(R.id.itemText);
             textItem.setText(cursor.getString(1));
             ImageView type = (ImageView)view.findViewById(R.id.type);
             switch (cursor.getInt(3)){
                 case CallLog.Calls.INCOMING_TYPE:type.setImageBitmap(incoming); break;
                 case CallLog.Calls.OUTGOING_TYPE:type.setImageBitmap(outgoing);break;
                 case CallLog.Calls.MISSED_TYPE:type.setImageBitmap(missed);break;

             }

        }
    }


    private void showWrongPhone() {
        Toast.makeText(this, "Неверно введен номер!", 2000).show();
    }

    private void sendRequest() {
        sendButton.setEnabled(false);
        final String phone = phoneInput.getText().toString();
        Request.getInfoByNumber(phone, new RequestGetCallback() {
            @Override
            public void onInfoFound(PhoneUserInfo response) {
                Intent found = new Intent(RequestPhoneActivity.this, ResultActivity.class);
                found.putExtra("actionType", 1);
                found.putExtra("phone", response.getPhone());
                Map<String, Object> info = response.getInfo();
                for (String key : info.keySet()) {
                    Object value = info.get(key);
                    if (value instanceof List<?>) {
                        List<String> strings = (List<String>) value;
                        StringBuilder builder = new StringBuilder();
                        for (String s : strings) {
                            builder.append(s);
                            builder.append(" ,");
                        }
                        builder.deleteCharAt(builder.length() - 1);
                        found.putExtra(key, builder.toString());
                    } else {
                        found.putExtra(key, String.valueOf(value));
                    }
                }

                startActivity(found);
            }

            @Override
            public void onNotFound() {
                Intent notFound = new Intent(RequestPhoneActivity.this, ResultActivity.class);
                notFound.putExtra("actionType", 0);
                notFound.putExtra("phone", phone);
                startActivity(notFound);
            }

            @Override
            public void onFailure() {
                Toast.makeText(RequestPhoneActivity.this, "Ошибка сервера", 2000).show();
                sendButton.setEnabled(true);
            }
        });

    }


    private boolean checkPhoneInput() {
        String phone = phoneInput.getText().toString();

        if (phone.length() < 5) {
            return false;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem mi = menu.add(0, 1, 0, "Настройки");
        mi.setIntent(new Intent(this, SettingsActivity.class));
        return super.onCreateOptionsMenu(menu);
    }



}
