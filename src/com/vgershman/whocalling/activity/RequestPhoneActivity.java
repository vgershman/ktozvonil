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
import com.vgershman.whocalling.adapter.LastCallsAdapter;
import com.vgershman.whocalling.app.*;
import com.vgershman.whocalling.connection.Request;
import com.vgershman.whocalling.connection.RequestGetCallback;
import com.vgershman.whocalling.dao.Call;
import com.vgershman.whocalling.dto.NotFoundInfo;
import com.vgershman.whocalling.dto.PhoneUserInfo;

import java.util.ArrayList;
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
    ListView lastCalls;
    ImageButton settingsButton;
    LastCallsAdapter callsAdapter = new LastCallsAdapter(this);

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
                        intent.putExtra("self", true);
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
//                    showWrongPhone();
                    Intent intent = new Intent(RequestPhoneActivity.this, NoResultActivity.class);
                    startActivity(intent);
                }
            }


        });

        boolean addedInfo = getSharedPreferences(AppInfo.PREFERENCES_NAME, MODE_PRIVATE).getBoolean("addedInfo", false);

        lastCalls = (ListView) findViewById(R.id.lastCalls);
        List<Call> calls = getCallHistory();
        if (calls.size() == 1) {
            lastCalls.addHeaderView(getSingleCallItem(calls.get(0)));
        } else if (calls.size() == 2) {
            lastCalls.addHeaderView(getHeaderCallItem(calls.get(0)));
            lastCalls.addFooterView(getFooterCallItem(calls.get(1)));
        } else if (calls.size() > 2) {
            lastCalls.addHeaderView(getHeaderCallItem(calls.get(0)));
            lastCalls.addFooterView(getFooterCallItem(calls.get(calls.size() - 1)));
            callsAdapter.setCalls(calls.subList(1,calls.size()-1));
        }

        lastCalls.setAdapter(callsAdapter);
        callsAdapter.notifyDataSetChanged();

        settingsButton = (ImageButton) findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RequestPhoneActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }

    private View getSingleCallItem(Call call) {
        View header = getLayoutInflater().inflate(R.layout.last_call_single_item, null, false);
        header = initHeader(call, header);

        return header;
    }

    private View getHeaderCallItem(Call call) {
        View header = getLayoutInflater().inflate(R.layout.last_call_header, null, false);
        header = initHeader(call, header);

        return header;
    }

    private View getFooterCallItem(Call call) {
        View footer = getLayoutInflater().inflate(R.layout.last_call_footer, null, false);
        footer = initHeader(call, footer);

        return footer;
    }

    private View initHeader(Call call, View header) {
        ImageView itemCallType = (ImageView) header.findViewById(R.id.itemIcCallState);
        TextView itemPhoneNumber = (TextView) header.findViewById(R.id.itemNumber);
        ImageView itemCallerIcon = (ImageView) header.findViewById(R.id.itemIcPerson);
        TextView itemCallerName = (TextView) header.findViewById(R.id.itemName);
        ImageView itemTextDivider = (ImageView) header.findViewById(R.id.itemIcDivider);
        TextView itemCallTime = (TextView) header.findViewById(R.id.itemTime);
        FrameLayout itemBackground = (FrameLayout) header.findViewById(R.id.itemBackground);

        if (call.isOut()) {
            itemCallType.setImageDrawable(getResources().getDrawable(R.drawable.ic_call_out));
        } else {
            itemCallType.setImageDrawable(getResources().getDrawable(R.drawable.ic_call_in));
        }

        itemPhoneNumber.setText(call.getNumber());
        if (call.hasName()) {
            itemCallerIcon.setVisibility(View.VISIBLE);
            itemCallerName.setVisibility(View.VISIBLE);
            itemTextDivider.setVisibility(View.VISIBLE);
            itemCallerName.setText(call.getName());
        } else {
            itemCallerIcon.setVisibility(View.GONE);
            itemCallerName.setVisibility(View.GONE);
            itemTextDivider.setVisibility(View.GONE);
        }

        itemCallTime.setText(call.getTimeString());

        return header;
    }

    private List<Call> getCallHistory() {
        String[] strFields = {android.provider.CallLog.Calls._ID,
                android.provider.CallLog.Calls.NUMBER,
                android.provider.CallLog.Calls.CACHED_NAME, CallLog.Calls.TYPE,
                CallLog.Calls.DATE};
        String strOrder = android.provider.CallLog.Calls.DATE + " DESC";
        final Cursor cursorCall = getContentResolver().query(
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

            callList.add(call);
        }
        cursorCall.close();
        return callList;
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
            public void onNotFound(NotFoundInfo response) {
                Intent notFound = new Intent(RequestPhoneActivity.this, ResultActivity.class);
                notFound.putExtra("actionType", 0);
                notFound.putExtra("operator", response.getOperator());
                notFound.putExtra("region", response.getRegion());
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
