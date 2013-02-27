package com.vgershman.whocall.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.*;
import com.vgershman.whocall.R;
import com.vgershman.whocall.adapter.CommentsAdapter;
import com.vgershman.whocall.app.AppInfo;
import com.vgershman.whocall.connection.CommentCallback;
import com.vgershman.whocall.connection.Request;
import com.vgershman.whocall.connection.RequestGetCallback;
import com.vgershman.whocall.connection.RequestPostCallback;
import com.vgershman.whocall.database.FieldsNameStorage;
import com.vgershman.whocall.dto.Call;
import com.vgershman.whocall.dto.Comment;
import com.vgershman.whocall.dto.NotFoundInfo;
import com.vgershman.whocall.dto.PhoneUserInfo;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created with IntelliJ IDEA.
 * User: vgershman
 * Date: 06.01.13
 * Time: 15:36
 * To change this template use File | Settings | File Templates.
 */
public class ResultActivity extends BaseActivity {

    EditText phoneEdit;
    TextView searchNumber;
    FrameLayout addContactButton;
    LinearLayout infoContainer;
    Button sendButton;


    TextView lastCallText;
    TextView lastCallTime;
    ImageView lastCallType;
    LinearLayout lastCallLayout;


    FrameLayout likeButton;
    FrameLayout dislikeButton;

    CommentsAdapter adapter;

    EditText userNameInput;
    EditText commentInput;
    FrameLayout sendComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_activity);
        initView();
        bindInfo();
}

    private void initView() {
        ImageButton navBack = (ImageButton)findViewById(R.id.nav_back);
        navBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ImageButton settingsButton = (ImageButton)findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResultActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });


        ListView list = (ListView) findViewById(R.id.resultList);
        View header = getLayoutInflater().inflate(R.layout.result_activity_header, null, false);
        lastCallText = (TextView)header.findViewById(R.id.lastCallText);
        lastCallLayout= (LinearLayout)header.findViewById(R.id.lastCallLayout);
        lastCallTime = (TextView)header.findViewById(R.id.lastCallTime);
        lastCallType = (ImageView)header.findViewById(R.id.lastCallType);
        searchNumber = (TextView)header.findViewById(R.id.searchNumber);
        addContactButton = (FrameLayout)header.findViewById(R.id.addContactButton);
        infoContainer = (LinearLayout)header.findViewById(R.id.infoContainer);
        likeButton = (FrameLayout)header.findViewById(R.id.thumbUpButton);
        dislikeButton = (FrameLayout)header.findViewById(R.id.thumbDownButton);

        userNameInput = (EditText)header.findViewById(R.id.userNameInput);
        commentInput = (EditText)header.findViewById(R.id.commentInput);
        sendComment = (FrameLayout)header.findViewById(R.id.sendComment);

        sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(commentInput.getText().toString().length()> 3){
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("user_name", userNameInput.getText().toString());
                    params.put("phone",getIntent().getStringExtra("phone"));
                    params.put("user_imei",AppInfo.getIMEI());
                    params.put("comment",commentInput.getText().toString());

                    Request.postInfo(params,new RequestPostCallback() {
                        @Override
                        public void onSuccess() {
                              updateComments();
                        }

                        @Override
                        public void onFailure() {
                           updateComments();
                        }
                    });
                }else {
                    Toast.makeText(ResultActivity.this, getText(R.string.wrongComment),1000).show();
                }
            }
        });



        View footer = getLayoutInflater().inflate(R.layout.result_activity_footer, null, false);
        phoneEdit = (EditText)footer.findViewById(R.id.phoneInput);
        sendButton = (Button)footer.findViewById(R.id.btnSend);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (checkPhoneInput()) {
                            sendRequest();
                        } else {
                            showWrongPhone();
                        }
                    }


                });
            }
        });


        list.addHeaderView(header);
        list.addFooterView(footer);
        adapter = new CommentsAdapter(this);
        list.setAdapter(adapter);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void bindInfo() {

        updateComments();

        List<Call>history = AppInfo.getCallHistory();
        Call last=null;
        for(Call callRecord:history){
            if(callRecord.getNumber().contains(getIntent().getStringExtra("phone")) || getIntent().getStringExtra("phone").contains(callRecord.getNumber())){
                if (last==null) {
                    last=callRecord;}
                else{
                  if(last.getTime() < callRecord.getTime()){
                      last = callRecord;
                  }
                }
            }
        }

        if(last==null){
            lastCallLayout.setVisibility(View.GONE);
            lastCallText.setVisibility(View.GONE);
        }else {
             lastCallTime.setText(last.getTimeString());

             if(!last.isOut()){
                 lastCallType.setImageBitmap(BitmapFactory.decodeResource(this.getResources(),R.drawable.ic_call_in));
             }else {
                lastCallType.setImageBitmap(BitmapFactory.decodeResource(this.getResources(),R.drawable.ic_call_out));
             }
        }

        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String,String> params = new HashMap<String, String>();
                params.put("phone",getIntent().getStringExtra("phone"));
                params.put("like","2");
                params.put("user_imei",AppInfo.getIMEI());
                Request.postInfo(params,new RequestPostCallback() {
                    @Override
                    public void onSuccess() {
                        //To change body of implemented methods use File | Settings | File Templates.
                    }

                    @Override
                    public void onFailure() {
                        //To change body of implemented methods use File | Settings | File Templates.
                    }
                });
            }
        });

        dislikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String,String> params = new HashMap<String, String>();
                params.put("phone",getIntent().getStringExtra("phone"));
                params.put("like","1");
                params.put("user_imei",AppInfo.getIMEI());
                Request.postInfo(params,new RequestPostCallback() {
                    @Override
                    public void onSuccess() {
                        //To change body of implemented methods use File | Settings | File Templates.
                    }

                    @Override
                    public void onFailure() {
                        //To change body of implemented methods use File | Settings | File Templates.
                    }
                });
            }
        });


        addContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_INSERT);
                intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
                intent.putExtra(ContactsContract.Intents.Insert.NAME, getIntent().getStringExtra("name"));
                intent.putExtra(ContactsContract.Intents.Insert.PHONE,getIntent().getStringExtra("phone"));
                if (getIntent().getStringExtra("email") != null) {
                    intent.putExtra(ContactsContract.Intents.Insert.EMAIL, getIntent().getStringExtra("email"));
                }
                startActivity(intent);
            }
        });

        searchNumber.setText(getIntent().getStringExtra("phone"));

        Bundle data = getIntent().getExtras();
        final String typeText = data.getString("type");
        data.keySet().remove("type");

        for (String key : data.keySet()) {

            if(key.equals("location")){
                LinearLayout itemLayout =  (LinearLayout) getLayoutInflater().inflate(R.layout.result_location_item, null, false);
                TextView title = (TextView)itemLayout.findViewById(R.id.infoTitle);
                title.setText("Aдрес".toUpperCase());
                TextView entry = (TextView)itemLayout.findViewById(R.id.infoEntry);
                entry.setText(data.getString(key));
                final String loc = data.getString(key);
                FrameLayout mapButton = (FrameLayout)itemLayout.findViewById(R.id.showMapButton);
                mapButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                Uri.parse("http://maps.google.com/maps?q="+loc));
                        startActivity(intent);
                    }
                });
                infoContainer.addView(itemLayout);
                continue;
            }

            if(key.equals("name")){
                LinearLayout itemLayout =  (LinearLayout) getLayoutInflater().inflate(R.layout.result_owner_item, null, false);
                TextView title = (TextView)itemLayout.findViewById(R.id.infoTitle);
                title.setText(getString(R.string.owner).toUpperCase());
                TextView entry = (TextView)itemLayout.findViewById(R.id.infoEntry);
                entry.setText(data.getString(key));
                TextView type = (TextView)itemLayout.findViewById(R.id.infoType);
                type.setText(typeText);
                infoContainer.addView(itemLayout);
                continue;
            }
                LinearLayout itemLayout =  (LinearLayout) getLayoutInflater().inflate(R.layout.result_item, null, false);
                TextView title = (TextView)itemLayout.findViewById(R.id.infoTitle);
                title.setText(FieldsNameStorage.getFieldName(key).toUpperCase());
                TextView entry = (TextView)itemLayout.findViewById(R.id.infoEntry);
                entry.setText(data.getString(key));
                infoContainer.addView(itemLayout);

        }

    }

    private void updateComments() {

        Request.getCommentsByNumber(getIntent().getStringExtra("phone"),new CommentCallback() {
            @Override
            public void onFound(List<Comment> commentList) {
                adapter.setComments(commentList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNotFound() {

            }

            @Override
            public void onError() {
                Toast.makeText(ResultActivity.this,getText(R.string.server_error),1000).show();
            }
        });

    }

    private boolean checkPhoneInput() {
        String phone = phoneEdit.getText().toString();
        if (phone.length() < 5) {
            return false;
        }
        return true;
    }

    private void showWrongPhone() {
        Toast.makeText(this, getText(R.string.wrongNumber), 2000).show();
    }

    private void sendRequest() {
        sendButton.setEnabled(false);
        final String phone = phoneEdit.getText().toString();
        Request.getInfoByNumber(phone, new RequestGetCallback() {
            @Override
            public void onInfoFound(PhoneUserInfo response) {
                Intent found = new Intent(ResultActivity.this, ResultActivity.class);
                found.putExtra("phone", phone);
                Map<String, Object> info = response.getInfo();
                for (String key : info.keySet()) {
                    Object value = info.get(key);
                    if (value instanceof List<?>) {
                        List<String> strings = (List<String>) value;
                        StringBuilder builder = new StringBuilder();
                        for (String s : strings) {
                            builder.append(s);
                            builder.append(" ,");
                        }
                        builder.deleteCharAt(builder.length() - 1);
                        found.putExtra(key, builder.toString());
                    } else {
                        found.putExtra(key, String.valueOf(value));
                    }
                }
                startActivity(found);
            }

            @Override
            public void onNotFound(NotFoundInfo response) {
                Intent notFound = new Intent(ResultActivity.this, NoResultActivity.class);
                notFound.putExtra("count", response.getCount() + "");
                notFound.putExtra("operator", response.getOperator());
                notFound.putExtra("region", response.getRegion());
                notFound.putExtra("phone", phone);
                startActivity(notFound);
            }

            @Override
            public void onFailure() {
                Toast.makeText(ResultActivity.this, getText(R.string.server_error), 2000).show();
                sendButton.setEnabled(true);
            }
        });
    }


}
