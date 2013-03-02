package com.vgershman.whocall.activity;

import android.app.Activity;
import com.vgershman.whocall.app.AppInfo;

/**
 * Created with IntelliJ IDEA.
 * User: User
 * Date: 26.02.13
 * Time: 17:02
 * To change this template use File | Settings | File Templates.
 */
public abstract class BaseActivity extends Activity {
    @Override
    protected void onResume() {
        super.onResume();
        AppInfo.setContext(this);
    }


}
