package com.vgershman.whocalling.activity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.view.View;
import android.widget.*;
import com.vgershman.whocalling.R;
import com.vgershman.whocalling.app.AppInfo;
import com.vgershman.whocalling.connection.Request;
import com.vgershman.whocalling.connection.RequestPostCallback;
import com.vgershman.whocalling.util.DialogsUtil;
import com.vgershman.whocalling.util.OnInputChangedListener;
import com.vgershman.whocalling.util.ReverseGeoCoding;
import com.vgershman.whocalling.util.ReverseGeoCodingListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: User
 * Date: 29.01.13
 * Time: 0:22
 * To change this template use File | Settings | File Templates.
 */
public class TellAboutActivity extends Activity {

    EditText phoneEdit;
    EditText emailEdit;
    EditText nameEdit;
    EditText optEdit;
    EditText wtEdit;
    EditText urlEdit;
    String locationText;
    String point;
    Button send;
    boolean self;
    String type = "";
    LinearLayout addit;
    Button showMore;
    boolean showMoreVar;
    FrameLayout fioSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tell_avout_myself_activity);
//        self = getIntent().getBooleanExtra("self",true);
//        type = getIntent().getStringExtra("type");
//
//        initView();
//        getCurrentLocation();
//        String number = getIntent().getStringExtra("phone");
//        if(number!=null){
//            phoneEdit.setText(number);
//        }
//
//        String myEmail = getMyEmail();
//        if(myEmail!=null){
//            emailEdit.setText(myEmail);
//        }
//
//        send.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(checkFields()){
//                    Map<String,String> data = new HashMap<String, String>();
//                    Map<String,String> adds = new HashMap<String, String>();
//
//                    data.put("phone",phoneEdit.getText().toString());
//                    data.put("name",nameEdit.getText().toString());
//                    data.put("description", optEdit.getText().toString());
//                    data.put("image","");
//                    data.put("email",emailEdit.getText().toString());
//
//                    adds.put("type",type);
//                    adds.put("location",locationText);
//                    adds.put("point",point);
//                    adds.put("url", urlEdit.getText().toString());
//                    adds.put("worktime",wtEdit.getText().toString());
//
//                    Request.postInfo(data, adds, new RequestPostCallback() {
//                        @Override
//                        public void onSuccess() {
//                            if(self){
//                                getSharedPreferences(AppInfo.PREFERENCES_NAME, MODE_PRIVATE).edit().putBoolean("addedInfo", true).commit();
//                            }
//                            Intent intent = new Intent(TellAboutActivity.this, ResultActivity.class);
//                            intent.putExtra("actionType", 2);
//                            intent.putExtra("phone", phoneEdit.getText());
//                            startActivity(intent);
//                            finish();
//                        }
//
//                        @Override
//                        public void onFailure() {
//                            Toast.makeText(TellAboutActivity.this, "Ошибка сервера", 2000).show();
//                        }
//                    });}
//            }
//        });
//
        fioSetting = (FrameLayout) findViewById(R.id.fioSetting);
        fioSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogsUtil.showInputDialog(TellAboutActivity.this, "Введите ваши Ф.И.О.", InputType.TYPE_CLASS_TEXT, new OnInputChangedListener() {
                    @Override
                    public void onInputChanged(String input) {
                        TextView fioSelection = (TextView) findViewById(R.id.itemFIOSelection);
                        fioSelection.setText(input);
                    }
                });
            }
        });
    }

    private void getCurrentLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (location == null) {
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        if (location != null) {
            point = "Lat:" + location.getLatitude() + ", Lon:" + location.getLongitude();
            new ReverseGeoCoding(this, location, new ReverseGeoCodingListener() {
                @Override
                public void OnSuccess(String s) {
                    locationText = s;
                }

                @Override
                public void OnFailure() {
                    locationText = "";

                }
            });
        } else {
            point = "";
            locationText = "";
        }
    }

    private void initView() {
        addit = (LinearLayout) findViewById(R.id.addFields);
        showMore = (Button) findViewById(R.id.showMore);
        showMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!showMoreVar) {
                    addit.setVisibility(View.VISIBLE);
                    showMoreVar = true;
                    showMore.setText("Скрыть");
                } else {
                    addit.setVisibility(View.GONE);
                    showMoreVar = false;
                    showMore.setText("Подробнее");
                }
            }
        });
        send = (Button) findViewById(R.id.aboutMyself);
        nameEdit = (EditText) findViewById(R.id.nameEdit);
        optEdit = (EditText) findViewById(R.id.optEdit);
        phoneEdit = (EditText) findViewById(R.id.phoneEdit);
        emailEdit = (EditText) findViewById(R.id.emailEdit);
        urlEdit = (EditText) findViewById(R.id.urlEdit);
        wtEdit = (EditText) findViewById(R.id.wtEdit);
    }

    private boolean checkFields() {
        Toast toast = Toast.makeText(this, "Неверно введен телефон и/или название", 2000);
        if (phoneEdit.getText().toString().length() < 5) {
            toast.show();
            return false;
        }
        if (nameEdit.getText().toString().equals("")) {
            toast.show();
            return false;
        }
        return true;
    }

    private String getMyPhoneNumber() {
        TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        String number = tm.getLine1Number();
        return number;
    }

    private String getMyEmail() {
        AccountManager am = AccountManager.get(this);
        Account[] accounts = am.getAccounts();
        ArrayList<String> googleAccounts = new ArrayList<String>();
        for (Account ac : accounts) {
            String acname = ac.name;
            String actype = ac.type;

            if (ac.type.equals("com.google")) {
                googleAccounts.add(ac.name);
            }

        }
        if (googleAccounts.size() == 0) {
            return null;
        } else {
            return googleAccounts.get(0);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();    //To change body of overridden methods use File | Settings | File Templates.
        overridePendingTransition(0, android.R.anim.slide_out_right);
    }
}
