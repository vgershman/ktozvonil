package com.vgershman.whocalling.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.vgershman.whocalling.R;

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
                Intent intent = new Intent(TypeActivity.this, TellAboutActivity.class);
                intent.putExtra("type","personal");
                intent.putExtra("self",getIntent().getBooleanExtra("self",true));
                intent.putExtra("phone",getIntent().getStringExtra("phone"));
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left,0);
                finish();
            }
        });
        Button company = (Button)findViewById(R.id.company);
        company.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TypeActivity.this, TellAboutActivity.class);
                intent.putExtra("type","company");
                intent.putExtra("self",getIntent().getBooleanExtra("self",true));
                intent.putExtra("phone",getIntent().getStringExtra("phone"));
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left,0);
                finish();
            }
        });
        Button rascal = (Button)findViewById(R.id.rascal);
        rascal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TypeActivity.this, TellAboutActivity.class);
                intent.putExtra("type","rascal");
                intent.putExtra("self",getIntent().getBooleanExtra("self",true));
                intent.putExtra("phone",getIntent().getStringExtra("phone"));
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left,0);
                finish();
            }
        });

        boolean self = getIntent().getBooleanExtra("self",false);
        if(self){
            rascal.setVisibility(View.GONE);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();    //To change body of overridden methods use File | Settings | File Templates.
        overridePendingTransition(0,android.R.anim.slide_out_right);
        finish();
    }
}
