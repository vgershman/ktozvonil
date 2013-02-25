package com.vgershman.whocalling.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import com.vgershman.whocalling.R;

/**
 * Created with IntelliJ IDEA.
 * User: vgershman
 * Date: 08.01.13
 * Time: 1:34
 * To change this template use File | Settings | File Templates.
 */
public class SettingsActivity extends Activity {

    private FrameLayout tellAbout;

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
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
