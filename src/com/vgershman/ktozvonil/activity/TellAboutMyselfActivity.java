package com.vgershman.ktozvonil.activity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.vgershman.ktozvonil.R;
import com.vgershman.ktozvonil.app.AppInfo;
import com.vgershman.ktozvonil.connection.Request;
import com.vgershman.ktozvonil.connection.RequestPostCallback;

import java.util.ArrayList;
import java.util.StringTokenizer;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tell_avout_myself_activity);
        initView();

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
                    String phone =  phoneEdit.getText().toString();
                    String email = emailEdit.getText().toString();
                    String description = optEdit.getText().toString();
                    String image = "";
                    String name = nameEdit.getText().toString();

                    Request.postInfo(phone,name,description,image,email,new RequestPostCallback() {
                        @Override
                        public void onSuccess() {

                            getSharedPreferences(AppInfo.PREFERENCES_NAME, MODE_PRIVATE).edit().putBoolean("addedInfo",true).commit();

                            Intent intent = new Intent(TellAboutMyselfActivity.this, ResultActivity.class);
                            intent.putExtra("actionType",2);
                            startActivity(intent);
                        }

                        @Override
                        public void onFailure() {
                            Toast.makeText(TellAboutMyselfActivity.this,"Ошибка сервера",2000).show();
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
        Toast toast = Toast.makeText(this,"Неверно введен телефон",2000);
        if(phoneEdit.getText().toString().equals("")){toast.show();return false;}
        if(phoneEdit.getText().toString().charAt(0)!='7'){toast.show();return false;}
        if(phoneEdit.getText().toString().length()!=11){toast.show();return false;}

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
}
