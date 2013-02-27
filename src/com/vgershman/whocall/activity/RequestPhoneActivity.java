package com.vgershman.whocall.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.*;
import android.widget.*;
import com.vgershman.whocall.R;
import com.vgershman.whocall.adapter.LastCallsAdapter;
import com.vgershman.whocall.app.AppInfo;
import com.vgershman.whocall.connection.Request;
import com.vgershman.whocall.connection.RequestGetCallback;
import com.vgershman.whocall.dto.Call;
import com.vgershman.whocall.dto.NotFoundInfo;
import com.vgershman.whocall.dto.PhoneUserInfo;
import com.vgershman.whocall.service.CallStateListener;
import com.vgershman.whocall.service.PushService;


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
public class RequestPhoneActivity extends BaseActivity {

    EditText phoneInput;
    Button sendButton;
    ListView lastCalls;
    ImageButton settingsButton;
    LastCallsAdapter callsAdapter = new LastCallsAdapter(this);

    @Override
    protected void onResume() {
        super.onResume();
        sendButton.setEnabled(true);
        startService(new Intent(RequestPhoneActivity.this, PushService.class));

        List<Call> calls = AppInfo.getCallHistory();
        callsAdapter.setCalls(calls);
        callsAdapter.notifyDataSetChanged();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_phone_activity);
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        tm.listen(new CallStateListener(this), PhoneStateListener.LISTEN_CALL_STATE);
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
                builder.setMessage(getResources().getString(R.string.dialog_add_info_message));
                builder.setNegativeButton(getText(R.string.never_btn), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sharedPreferences.edit().putBoolean("addedInfo", true);
                    }
                });
                builder.setNeutralButton(getText(R.string.later_btn), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.setPositiveButton(getText(R.string.yes_btn), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(RequestPhoneActivity.this, TellAboutActivity.class);
                        intent.putExtra("myself", true);
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

        lastCalls = (ListView) findViewById(R.id.lastCalls);
        lastCalls.setAdapter(callsAdapter);
        lastCalls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                Request.getInfoByNumber(callsAdapter.getItem(i).getNumber(), new RequestGetCallback() {
                    @Override
                    public void onInfoFound(PhoneUserInfo response) {
                        Intent found = new Intent(RequestPhoneActivity.this, ResultActivity.class);
                        found.putExtra("phone", callsAdapter.getItem(i).getNumber());
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
                        Intent notFound = new Intent(RequestPhoneActivity.this, NoResultActivity.class);
                        notFound.putExtra("count", response.getCount() + "");
                        notFound.putExtra("operator", response.getOperator());
                        notFound.putExtra("region", response.getRegion());
                        notFound.putExtra("phone", callsAdapter.getItem(i).getNumber());
                        startActivity(notFound);
                    }

                    @Override
                    public void onFailure() {
                        Toast.makeText(RequestPhoneActivity.this, getText(R.string.server_error), 2000).show();
                        sendButton.setEnabled(true);
                    }
                });
            }
        });



        settingsButton = (ImageButton) findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RequestPhoneActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }



    private void showWrongPhone() {
        Toast.makeText(this, getText(R.string.wrongNumber), 2000).show();
    }

    private void sendRequest() {
        sendButton.setEnabled(false);
        final String phone = phoneInput.getText().toString();
        Request.getInfoByNumber(phone, new RequestGetCallback() {
            @Override
            public void onInfoFound(PhoneUserInfo response) {
                Intent found = new Intent(RequestPhoneActivity.this, ResultActivity.class);
                found.putExtra("phone", phone);
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
                Intent notFound = new Intent(RequestPhoneActivity.this, NoResultActivity.class);
                notFound.putExtra("count",response.getCount()+"");
                notFound.putExtra("operator", response.getOperator());
                notFound.putExtra("region", response.getRegion());
                notFound.putExtra("phone", phone);
                startActivity(notFound);
            }

            @Override
            public void onFailure() {
                Toast.makeText(RequestPhoneActivity.this, getText(R.string.server_error), 2000).show();
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
        MenuItem mi = menu.add(0, 1, 0, getText(R.string.settings));
        mi.setIntent(new Intent(this, SettingsActivity.class));
        return super.onCreateOptionsMenu(menu);
    }


}
