package com.vgershman.whocall.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import com.vgershman.whocall.R;
import com.vgershman.whocall.app.AppInfo;
import com.vgershman.whocall.service.PushService;
import com.vgershman.whocall.util.DialogsUtil;


import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vgershman
 * Date: 08.01.13
 * Time: 1:34
 * To change this template use File | Settings | File Templates.
 */
public class SettingsActivity extends Activity {

    private FrameLayout tellAbout;
    private FrameLayout callInfoSetting;
    private TextView languageSelection;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);


        findViewById(R.id.btnSendResponse).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.vgershman.whocall")));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=com.vgershman.whocall")));
                }
            }
        });

        ImageButton save = (ImageButton)findViewById(R.id.saveButton);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ImageButton backNav = (ImageButton)findViewById(R.id.nav_back);
        backNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        tellAbout = (FrameLayout) findViewById(R.id.contacts);
        tellAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, TellAboutActivity.class);
                startActivity(intent);
            }
        });


        int callInfo = getSharedPreferences(AppInfo.PREFERENCES_NAME,MODE_PRIVATE).getInt("callInfo",0);

        final List<String> options = new ArrayList<String>();
        options.add(getString(R.string.setting_call_yes));
        options.add(getString(R.string.setting_call_no));
        options.add(getString(R.string.setting_call_an));

        languageSelection = (TextView) findViewById(R.id.itemLanguageSelection);
        languageSelection.setText(options.get(callInfo));

        callInfoSetting = (FrameLayout) findViewById(R.id.callInfoSetting);
        callInfoSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogInterface.OnClickListener selector = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        languageSelection.setText(options.get(i));
                        getSharedPreferences(AppInfo.PREFERENCES_NAME,MODE_PRIVATE).edit().putInt("callInfo",i).commit();
                    }
                };

                DialogsUtil.showDropdownDialog(SettingsActivity.this, getString(R.string.setting_call_title), options, selector);
            }
        });

        final TextView filterSelection = (TextView)findViewById(R.id.itemFilterSelection);
        FrameLayout itemFilterSetting = (FrameLayout)findViewById(R.id.itemFilterSetting);
        final List<String>options1 = new ArrayList<String>();
        options1.add(getString(R.string.setting_call_log_filter_all));
        options1.add(getString(R.string.setting_call_log_filter_an));
        int filter = getSharedPreferences(AppInfo.PREFERENCES_NAME,MODE_PRIVATE).getInt("filter",0);
        filterSelection.setText(options1.get(filter));
        itemFilterSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogsUtil.showDropdownDialog(SettingsActivity.this,getString(R.string.setting_call_log_filter_title),options1,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        filterSelection.setText(options1.get(i));
                        getSharedPreferences(AppInfo.PREFERENCES_NAME,MODE_PRIVATE).edit().putInt("filter",i).commit();
                    }
                });
            }
        });

        final TextView pushSelection = (TextView)findViewById(R.id.itemNotificationSelection);
        FrameLayout itemPushSetting = (FrameLayout)findViewById(R.id.itemPushSettings);
        final List<String>options2 = new ArrayList<String>();
        options2.add(getString(R.string.settings_notifications_ec));
        options2.add(getString(R.string.settings_notifications_ac));
        options2.add(getString(R.string.settings_notifications_ni));
        int pushSetting = getSharedPreferences(AppInfo.PREFERENCES_NAME,MODE_PRIVATE).getInt("pushSetting",0);
        pushSelection.setText(options2.get(pushSetting));
        itemPushSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogsUtil.showDropdownDialog(SettingsActivity.this,getString(R.string.settings_notifications_title),options2,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        pushSelection.setText(options2.get(i));
                        getSharedPreferences(AppInfo.PREFERENCES_NAME,MODE_PRIVATE).edit().putInt("pushSetting",i).commit();
                        startService(new Intent(SettingsActivity.this,PushService.class));
                    }
                });
            }
        });



    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
