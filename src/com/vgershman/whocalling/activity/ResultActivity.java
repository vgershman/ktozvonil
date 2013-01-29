package com.vgershman.whocalling.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vgershman.whocalling.database.PhonesManager;
import com.vgershman.whocalling.service.PushService;
import com.vgershman.whocalling.R;
import com.vgershman.whocalling.app.*;

import java.util.ArrayList;
import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: vgershman
 * Date: 06.01.13
 * Time: 15:36
 * To change this template use File | Settings | File Templates.
 */
public class ResultActivity extends Activity {

    String phoneRequest;
    LinearLayout additionalLayout;
    Button showMore;
    Button tellAboutMyself;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_activity);

        additionalLayout = (LinearLayout) findViewById(R.id.addResultLayout);
        int actionType = getIntent().getIntExtra("actionType", 0);
        phoneRequest = getIntent().getStringExtra("phone");
        showMore = (Button) findViewById(R.id.showMore);

        boolean addedInfo = getSharedPreferences(AppInfo.PREFERENCES_NAME, MODE_PRIVATE).getBoolean("addedInfo", false);

        tellAboutMyself = (Button) findViewById(R.id.aboutMyself);
        if (!addedInfo) {
            tellAboutMyself.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ResultActivity.this, TypeActivity.class);
                    intent.putExtra("self",true);
                    startActivity(intent);
                }
            });
        } else {
            tellAboutMyself.setVisibility(View.GONE);
        }
        switch (actionType) {
            case 0:
                showNotFound();
                break;
            case 1:
                showFound();
                break;
            case 2:
                showSuccessFullyAdded();
                break;
        }

        Button anotherRequest = (Button) findViewById(R.id.another);
        anotherRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResultActivity.this, RequestPhoneActivity.class);
                startActivity(intent);
            }
        });

        showMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (additionalLayout.getVisibility() == View.GONE) {
                    additionalLayout.setVisibility(View.VISIBLE);
                    showMore.setText("Скрыть");
                } else {
                    additionalLayout.setVisibility(View.GONE);
                    showMore.setText("Подробнее");
                }
            }
        });
    }

    private void showSuccessFullyAdded() {
        TextView resultText = (TextView) findViewById(R.id.resultText);
        resultText.setText("Спасибо! Вы успешно добавили информацию по телефону");
    }

    private void showNotFound() {
        TextView resultText = (TextView) findViewById(R.id.resultText);
        resultText.setText("Помогите другим! \n Вы будете первым кто добавит информацию о контакте." );
        tellAboutMyself.setVisibility(View.VISIBLE);
        tellAboutMyself.setText("Добавить комментарии");
        tellAboutMyself.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResultActivity.this, TypeActivity.class);
                intent.putExtra("self",false);
                intent.putExtra("phone",phoneRequest);
                startActivity(intent);
            }
        });

        PhonesManager phonesManager = new PhonesManager(this);
                phonesManager.addPhone(phoneRequest);
        Intent intent = new Intent(ResultActivity.this, PushService.class);
        startService(intent);
    }

    private void showFound() {
        showMore.setVisibility(View.VISIBLE);

        TextView resultText = (TextView) findViewById(R.id.resultText);
        resultText.setVisibility(View.GONE);
        LinearLayout resultLayout = (LinearLayout) findViewById(R.id.resultLayout);
        resultLayout.setVisibility(View.VISIBLE);


        TextView phoneResult = (TextView) resultLayout.findViewById(R.id.resultPhone);
        Bundle data = getIntent().getExtras();
        final String phone = data.getString("phone");
        final String name = data.getString("name");
        final String email = data.getString("email");
        phoneResult.setText(phone);

        List<String> main = new ArrayList<String>();
        main.add("name");
        main.add("type");
        data.keySet().removeAll(main);

        for (String key : main) {
            TextView textView = (TextView)getLayoutInflater().inflate(R.layout.text_view,null,false);
            textView.setText(data.getString(key));
            resultLayout.addView(textView);
        }
        for (String key : data.keySet()) {
            TextView textView = (TextView)getLayoutInflater().inflate(R.layout.text_view,null,false);
            textView.setText(data.getString(key));
            additionalLayout.addView(textView);
        }

        Button addContact = (Button) findViewById(R.id.addContact);
        addContact.setVisibility(View.VISIBLE);
        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_INSERT);
                intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
                intent.putExtra(ContactsContract.Intents.Insert.NAME, name);
                intent.putExtra(ContactsContract.Intents.Insert.PHONE, phone);
                if(email!=null){
                    intent.putExtra(ContactsContract.Intents.Insert.EMAIL, email);
                }
                startActivity(intent);

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (additionalLayout.getVisibility() == View.VISIBLE) {
            additionalLayout.setVisibility(View.GONE);
            showMore.setText("Подробнее");
        } else {
            super.onBackPressed();
        }

    }
}
