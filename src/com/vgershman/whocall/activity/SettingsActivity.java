package com.vgershman.whocall.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
        options.add("Да");
        options.add("Нет");
        options.add("Для неизвестных");

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

                DialogsUtil.showDropdownDialog(SettingsActivity.this, "Показывать во время звонка", options, selector);
            }
        });

        final TextView filterSelection = (TextView)findViewById(R.id.itemFilterSelection);
        FrameLayout itemFilterSetting = (FrameLayout)findViewById(R.id.itemFilterSetting);
        final List<String>options1 = new ArrayList<String>();
        options1.add("Все");
        options1.add("Только неизвестные");
        int filter = getSharedPreferences(AppInfo.PREFERENCES_NAME,MODE_PRIVATE).getInt("filter",0);
        filterSelection.setText(options1.get(filter));
        itemFilterSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogsUtil.showDropdownDialog(SettingsActivity.this,"Показывать",options1,new DialogInterface.OnClickListener() {
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
        options2.add("Экономия траффика");
        options2.add("Актуальная информация");
        int pushSetting = getSharedPreferences(AppInfo.PREFERENCES_NAME,MODE_PRIVATE).getInt("pushSetting",0);
        pushSelection.setText(options2.get(pushSetting));
        itemPushSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogsUtil.showDropdownDialog(SettingsActivity.this,"Что важнее?",options2,new DialogInterface.OnClickListener() {
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
