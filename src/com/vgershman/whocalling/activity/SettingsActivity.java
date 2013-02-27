package com.vgershman.whocalling.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.vgershman.whocalling.R;
import com.vgershman.whocalling.util.DialogsUtil;

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
    private FrameLayout languageSetting;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
//        addPreferencesFromResource(R.xml.preferences);

        tellAbout = (FrameLayout) findViewById(R.id.contacts);
        tellAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, TellAboutActivity.class);
                startActivity(intent);
            }
        });

        languageSetting = (FrameLayout) findViewById(R.id.languageSetting);
        languageSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final List<String> options = new ArrayList<String>();
                options.add("English");
                options.add("Русский");
                options.add("Deutsch");

                DialogInterface.OnClickListener selector = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        TextView languageSelection = (TextView) findViewById(R.id.itemLanguageSelection);
                        languageSelection.setText(options.get(i));
                    }
                };

                DialogsUtil.showDropdownDialog(SettingsActivity.this, "Выберите язык", options, selector);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
