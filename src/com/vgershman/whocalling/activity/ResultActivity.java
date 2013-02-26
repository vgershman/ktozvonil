package com.vgershman.whocalling.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.vgershman.whocalling.adapter.CommentsAdapter;
import com.vgershman.whocalling.dao.Comment;
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

        ListView list = (ListView) findViewById(R.id.resultList);
        View header = getLayoutInflater().inflate(R.layout.result_activity_header, null, false);
        View footer = getLayoutInflater().inflate(R.layout.result_activity_footer, null, false);
        list.addHeaderView(header);
        list.addFooterView(footer);
        CommentsAdapter adapter = new CommentsAdapter(this);
        list.setAdapter(adapter);

        List<Comment> comments = new ArrayList<Comment>();
        Comment c1 = new Comment();
        Comment c2 = new Comment();
        comments.add(c1);
        comments.add(c2);
        adapter.setComments(comments);
        adapter.notifyDataSetChanged();
//        additionalLayout = (LinearLayout) findViewById(R.id.addResultLayout);
//        int actionType = getIntent().getIntExtra("actionType", 0);
//        phoneRequest = getIntent().getStringExtra("phone");
//        showMore = (Button) findViewById(R.id.showMore);
//
//        boolean addedInfo = getSharedPreferences(AppInfo.PREFERENCES_NAME, MODE_PRIVATE).getBoolean("addedInfo", false);
//
//        tellAboutMyself = (Button) findViewById(R.id.aboutMyself);
//        if (!addedInfo) {
//            tellAboutMyself.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(ResultActivity.this, TypeActivity.class);
//                    intent.putExtra("self",true);
//                    startActivity(intent);
//                }
//            });
//        } else {
//            tellAboutMyself.setVisibility(View.GONE);
//        }
//        switch (actionType) {
//            case 0:
//                showNotFound();
//                break;
//            case 1:
//                showFound();
//                break;
//            case 2:
//                showSuccessFullyAdded();
//                break;
//        }
//
//        Button anotherRequest = (Button) findViewById(R.id.another);
//        anotherRequest.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(ResultActivity.this, RequestPhoneActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//                startActivity(intent);
//            }
//        });
//
//        showMore.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (additionalLayout.getVisibility() == View.GONE) {
//                    additionalLayout.setVisibility(View.VISIBLE);
//                    showMore.setText("Скрыть");
//                } else {
//                    additionalLayout.setVisibility(View.GONE);
//                    showMore.setText("Подробнее");
//                }
//            }
//        });
    }

//    private void showSuccessFullyAdded() {
//        TextView resultText = (TextView) findViewById(R.id.resultText);
//        resultText.setText("Спасибо! Вы успешно добавили информацию по телефону");
//    }

    //    private void showNotFound() {
//        TextView resultText = (TextView) findViewById(R.id.resultText);
//        resultText.setText("Вы искали " + getIntent().getStringExtra("phone"));
//        LinearLayout resultLayout = (LinearLayout) findViewById(R.id.resultLayout);
//        resultLayout.setVisibility(View.VISIBLE);
//
//        TextView textView = (TextView) getLayoutInflater().inflate(R.layout.text_view, null, false);
//        textView.setText("Оператор:" + getIntent().getStringExtra("operator"));
//        TextView textView1 = (TextView) getLayoutInflater().inflate(R.layout.text_view, null, false);
//        textView1.setText("Регион:" + getIntent().getStringExtra("region"));
//        TextView textView2 = (TextView) getLayoutInflater().inflate(R.layout.text_view2, null, false);
//        textView2.setText("Помогите другим! \n" +
//                "Ты первый кто добавит информацию о владельце " + getIntent().getStringExtra("phone"));
//
//        resultLayout.addView(textView);
//        resultLayout.addView(textView1);
//        resultLayout.addView(textView2);
//
//
//        tellAboutMyself.setVisibility(View.VISIBLE);
//        tellAboutMyself.setText("Добавить комментарии");
//        tellAboutMyself.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(ResultActivity.this, TypeActivity.class);
//                intent.putExtra("self", false);
//                intent.putExtra("phone", phoneRequest);
//                startActivity(intent);
//                finish();
//            }
//        });
//
//        PhonesManager phonesManager = new PhonesManager(this);
//        phonesManager.addPhone(phoneRequest);
//        Intent intent = new Intent(ResultActivity.this, PushService.class);
//        startService(intent);
//    }
//
//    private void showFound() {
//        showMore.setVisibility(View.VISIBLE);
//
//        TextView resultText = (TextView) findViewById(R.id.resultText);
//        resultText.setVisibility(View.GONE);
//        LinearLayout resultLayout = (LinearLayout) findViewById(R.id.resultLayout);
//        resultLayout.setVisibility(View.VISIBLE);
//
//
//        TextView phoneResult = (TextView) resultLayout.findViewById(R.id.resultPhone);
//        Bundle data = getIntent().getExtras();
//        final String phone = data.getString("phone");
//        final String name = data.getString("name");
//        final String email = data.getString("email");
//        phoneResult.setText(phone);
//
//        List<String> main = new ArrayList<String>();
//        main.add("name");
//        main.add("type");
//
//
//        for (String key : main) {
//            TextView textView = (TextView) getLayoutInflater().inflate(R.layout.text_view, null, false);
//            textView.setText(data.getString(key));
//            resultLayout.addView(textView);
//        }
//
//        data.keySet().removeAll(main);
//        for (String key : data.keySet()) {
//            TextView textView = (TextView) getLayoutInflater().inflate(R.layout.text_view, null, false);
//            textView.setText(data.getString(key));
//            additionalLayout.addView(textView);
//        }
//
//        Button addContact = (Button) findViewById(R.id.addContact);
//        addContact.setVisibility(View.VISIBLE);
//        addContact.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent intent = new Intent(Intent.ACTION_INSERT);
//                intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
//                intent.putExtra(ContactsContract.Intents.Insert.NAME, name);
//                intent.putExtra(ContactsContract.Intents.Insert.PHONE, phone);
//                if (email != null) {
//                    intent.putExtra(ContactsContract.Intents.Insert.EMAIL, email);
//                }
//                startActivity(intent);
//
//            }
//        });
//    }
//
    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        if (additionalLayout.getVisibility() == View.VISIBLE) {
//            additionalLayout.setVisibility(View.GONE);
//            showMore.setText("Подробнее");
//        } else {
//            super.onBackPressed();
//        }
    }
}
