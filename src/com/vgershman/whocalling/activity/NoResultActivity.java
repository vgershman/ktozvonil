package com.vgershman.whocalling.activity;

import android.app.Activity;
import android.os.Bundle;
import com.vgershman.whocalling.R;

/**
 * Created with IntelliJ IDEA.
 * User: vkosarev
 * Date: 25.02.13
 * Time: 17:51
 * To change this template use File | Settings | File Templates.
 */
public class NoResultActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.no_result_activity);
    }

    @Override
    protected void onResume() {
        super.onResume();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();    //To change body of overridden methods use File | Settings | File Templates.
    }
}
