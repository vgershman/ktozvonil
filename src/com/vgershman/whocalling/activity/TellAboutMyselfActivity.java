package com.vgershman.whocalling.activity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.vgershman.whocalling.R;
import com.vgershman.whocalling.app.AppInfo;
import com.vgershman.whocalling.connection.Request;
import com.vgershman.whocalling.connection.RequestPostCallback;
import com.vgershman.whocalling.util.ReverseGeoCoding;
import com.vgershman.whocalling.util.ReverseGeoCodingListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: vgershman
 * Date: 06.01.13
 * Time: 19:02
 * To change this template use File | Settings | File Templates.
 */
public class TellAboutMyselfActivity extends Activity {

    EditText phoneEdit;
    EditText emailEdit;
    EditText nameEdit;
    EditText optEdit;
    String locationText;
    String point;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tell_avout_myself_activity);
        initView();
        LocationManager locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if(location==null){
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        }else{
            point=location.toString();
        }
        if(location!=null){

           new ReverseGeoCoding(this, location, new ReverseGeoCodingListener() {
               @Override
               public void OnSuccess(String s) {
                   locationText=s;
               }

               @Override
               public void OnFailure() {
                   locationText="";

               }
           });
        }else{
            locationText="";
        }


        String number=getMyPhoneNumber();
        if(number!=null){
            phoneEdit.setText(number);
        }
        String myEmail = getMyEmail();
        if(myEmail!=null){
            emailEdit.setText(myEmail);
        }
        Button send = (Button)findViewById(R.id.aboutMyself);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkFields()){
                    Map<String,String> data = new HashMap<String, String>();
                    Map<String,String> adds = new HashMap<String, String>();

                    data.put("phone",phoneEdit.getText().toString());
                    data.put("name",nameEdit.getText().toString());
                    data.put("description", optEdit.getText().toString());
                    data.put("image","");
                    data.put("email",emailEdit.getText().toString());

                    adds.put("type","personal");
                    adds.put("location",locationText);
                    adds.put("point",point);


                    Request.postInfo(data, adds, new RequestPostCallback() {
                        @Override
                        public void onSuccess() {

                            getSharedPreferences(AppInfo.PREFERENCES_NAME, MODE_PRIVATE).edit().putBoolean("addedInfo", true).commit();

                            Intent intent = new Intent(TellAboutMyselfActivity.this, ResultActivity.class);
                            intent.putExtra("actionType", 2);
                            intent.putExtra("phone", phoneEdit.getText());
                            startActivity(intent);
                        }

                        @Override
                        public void onFailure() {
                            Toast.makeText(TellAboutMyselfActivity.this, "Ошибка сервера", 2000).show();
                        }
                    });}
            }
        });

    }

    private void initView() {
        nameEdit = (EditText)findViewById(R.id.nameEdit);
        optEdit = (EditText)findViewById(R.id.optEdit);
        phoneEdit=(EditText)findViewById(R.id.phoneEdit);
        emailEdit = (EditText)findViewById(R.id.emailEdit);
    }

    private boolean checkFields(){
        Toast toast = Toast.makeText(this,"Неверно введен телефон и/или имя",2000);
        if(phoneEdit.getText().toString().equals("")){toast.show();return false;}
        if(phoneEdit.getText().toString().charAt(0)!='7'){toast.show();return false;}
        if(phoneEdit.getText().toString().length()!=11){toast.show();return false;}
        if(nameEdit.getText().toString().equals("")){toast.show();return false;}
        return true;
    }
    private String getMyPhoneNumber(){
        TelephonyManager tm = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        String number = tm.getLine1Number();
        return number;
    }

    private String getMyEmail(){
        AccountManager am = AccountManager.get(this);
        Account[] accounts = am.getAccounts();
        ArrayList<String> googleAccounts = new ArrayList<String>();
        for (Account ac : accounts) {
            String acname = ac.name;
            String actype = ac.type;

            if(ac.type.equals("com.google")) {
                googleAccounts.add(ac.name);
            }


        }
        if(googleAccounts.size()==0){
            return null;
        } else {
        return googleAccounts.get(0);}
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();    //To change body of overridden methods use File | Settings | File Templates.
        overridePendingTransition(0,android.R.anim.slide_out_right);
    }
}
