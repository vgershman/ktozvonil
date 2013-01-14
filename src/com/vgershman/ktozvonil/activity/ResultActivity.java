package com.vgershman.ktozvonil.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vgershman.ktozvonil.database.PhonesManager;
import com.vgershman.ktozvonil.service.PushService;
import com.vgershman.ktozvonil.R;
import com.vgershman.ktozvonil.app.*;


/**
 * Created with IntelliJ IDEA.
 * User: vgershman
 * Date: 06.01.13
 * Time: 15:36
 * To change this template use File | Settings | File Templates.
 */
public class ResultActivity extends Activity {

    String phoneRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_activity);

        int actionType = getIntent().getIntExtra("actionType", 0);
        phoneRequest = getIntent().getStringExtra("phone");

        switch (actionType){
            case 0:showNotFound();break;
            case 1:showFound();break;
            case 2:showSuccessFullyAdded();break;

        }

        Button anotherRequest = (Button)findViewById(R.id.another);
        anotherRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResultActivity.this, RequestPhoneActivity.class);
                startActivity(intent);
            }
        });

        boolean addedInfo = getSharedPreferences(AppInfo.PREFERENCES_NAME,MODE_PRIVATE).getBoolean("addedInfo",false);

        Button tellAboutMyself = (Button)findViewById(R.id.aboutMyself);
        if(!addedInfo){
            tellAboutMyself.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ResultActivity.this,TellAboutMyselfActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            tellAboutMyself.setVisibility(View.GONE);
        }

    }

    private void showSuccessFullyAdded() {
        TextView resultText = (TextView)findViewById(R.id.resultText);
        resultText.setText("Информация о Вас успешно добавлена. Спасибо!");
    }

    private void showNotFound() {
        TextView resultText = (TextView)findViewById(R.id.resultText);
        resultText.setText("Информация пока не найдена."+ '\n' +" Как только найдем - обязательно сообщим Вам об этом.");
        new PhonesManager(this).addPhone(phoneRequest);
        Intent intent = new Intent(ResultActivity.this, PushService.class);
        startService(intent);
    }

    private void showFound() {
        TextView resultText = (TextView)findViewById(R.id.resultText);
        resultText.setText("Информация успешно найдена.");
        LinearLayout resultLayout = (LinearLayout)findViewById(R.id.resultLayout);
        resultLayout.setVisibility(View.VISIBLE);
        TextView phoneResult = (TextView)resultLayout.findViewById(R.id.resultPhone);
        TextView nameResult = (TextView)resultLayout.findViewById(R.id.resultName);
        TextView emailResult = (TextView)resultLayout.findViewById(R.id.resultEmail);
        TextView operatorResult = (TextView)resultLayout.findViewById(R.id.resultOperator);
        TextView reqionResult = (TextView)resultLayout.findViewById(R.id.resultReqion);
        Bundle data = getIntent().getExtras();

         final   String phone = data.getString("phone");
            if(phone!=null){
                phoneResult.setText("Телефон: "+ phone);
            }
         final   String name = data.getString("name");
            if(name!=null){
                nameResult.setText("ФИО: "+ name);
            }
            String email = data.getString("email");
            if(email!=null){
                emailResult.setText("Email: "+ email);
            }
            String reqion = data.getString("reqion");
            if(reqion!=null){
                reqionResult.setText("Регион: "+ reqion);
            }
            String operator = data.getString("operator");
            if(operator!=null){
                operatorResult.setText("Оператор: "+ operator);
            }
        Button addContact = (Button)findViewById(R.id.addContact);
        addContact.setVisibility(View.VISIBLE);
        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_INSERT);
                intent.setType(ContactsContract.Contacts.CONTENT_TYPE);


                intent.putExtra(ContactsContract.Intents.Insert.NAME, name);
                intent.putExtra(ContactsContract.Intents.Insert.PHONE, "+"+phone);

              startActivity(intent);

            }
        });


    }
}
