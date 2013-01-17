package com.vgershman.ktozvonil.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.vgershman.ktozvonil.R;

/**
 * Created with IntelliJ IDEA.
 * User: Вадим
 * Date: 17.01.13
 * Time: 13:40
 * To change this template use File | Settings | File Templates.
 */
public class TypeActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.type_activity);
        Button personal = (Button)findViewById(R.id.personal);
        personal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TypeActivity.this, TellAboutMyselfActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_out_right,android.R.anim.slide_in_left);
            }
        });
        Button company = (Button)findViewById(R.id.company);
        company.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TypeActivity.this, TellAboutMyselfActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_out_right,android.R.anim.slide_in_left);
            }
        });

    }
}
