package com.vgershman.whocalling.activity;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import com.vgershman.whocalling.R;

/**
 * Created with IntelliJ IDEA.
 * User: vgershman
 * Date: 08.01.13
 * Time: 1:34
 * To change this template use File | Settings | File Templates.
 */
public class SettingsActivity extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
