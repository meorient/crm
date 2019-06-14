package com.example.administrator.crm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.administrator.crm.customer.CustomerActivity;
import com.example.administrator.crm.util.SharedHelper;

/**
 * Created by Administrator
 * Date 2019/6/9
 */
public class StartActivity extends AppCompatActivity {

    private Context context;
    private final int SPLASH_DISPLAY_LENGHT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.activity_start);

        new android.os.Handler().postDelayed(() -> {
            Intent mainIntent;
            if(SharedHelper.getInstance("my_sp").contains(context,"login")) {
                mainIntent = new Intent(StartActivity.this,
                        CustomerActivity.class);
            }else{
                mainIntent = new Intent(StartActivity.this,
                        LoginActivity.class);
            }
            StartActivity.this.startActivity(mainIntent);
            StartActivity.this.finish();
        }, SPLASH_DISPLAY_LENGHT);
    }


}
