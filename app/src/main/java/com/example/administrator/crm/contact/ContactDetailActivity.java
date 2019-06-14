package com.example.administrator.crm.contact;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.administrator.crm.call.Call;
import com.example.administrator.crm.util.ApiUtil;
import com.example.administrator.crm.util.HttpUtil;
import com.example.administrator.crm.R;
import com.example.administrator.crm.util.SharedHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator
 * Date 2019/6/9
 */
public class ContactDetailActivity extends AppCompatActivity {
    private TextView textView;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;
    private Context context;
    private LocalBroadcastManager localBroadcastManager;
    private Handler handler = new Handler();

    private final String REAL_CALL_ACTION = "com.example.broadcasttest.LOCAL_BROADCAST";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);
        context=getApplicationContext();
        Toolbar toolbar = findViewById(R.id.activity_contactdetail_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);//设置Navigation 图标
        toolbar.setNavigationOnClickListener((v) ->finish());

        Date date = new Date();

        localBroadcastManager = LocalBroadcastManager.getInstance(this);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");


        textView = findViewById(R.id.contactname);
        textView2 = findViewById(R.id.contactname2);
        textView3 = findViewById(R.id.contactname3);
        textView4 = findViewById(R.id.contactname4);
        Intent intent = getIntent();
        long id = intent.getLongExtra("id",0);
        String str = intent.getStringExtra("name");
        String mobile = intent.getStringExtra("mobile");
        String phone = intent.getStringExtra("phone");
        String homephone = intent.getStringExtra("homephone");
        textView.setText(str);
        textView2.setText(mobile);
        textView3.setText(phone);
        textView4.setText(homephone);
        textView2.setOnClickListener(view -> {
            Uri uri = Uri.parse("tel:"+textView2.getText());
            send(new Call(id,sdf.format(date),textView2.getText().toString(),str));
            Intent intent1 = new Intent(Intent.ACTION_DIAL, uri);
            startActivity(intent1);
        });
        textView3.setOnClickListener(view -> {
            Uri uri = Uri.parse("tel:"+textView3.getText());
            send(new Call(id,sdf.format(date),textView3.getText().toString(),str));
            Intent intent12 = new Intent(Intent.ACTION_DIAL, uri);
            startActivity(intent12);
        });
        textView4.setOnClickListener(view -> {
            Uri uri = Uri.parse("tel:"+textView4.getText());
            send(new Call(id,sdf.format(date),textView4.getText().toString(),str));
            Intent intent13 = new Intent(Intent.ACTION_DIAL, uri);
            startActivity(intent13);
        });

    }

    private void send(Call call){
        new Thread(() -> {
            long userid = (long)SharedHelper.getInstance("my_sp").get(context,"userid",(long)1232132);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("contactId",call.getId());
            jsonObject.put("startedAt",call.getDate());
            jsonObject.put("contactName",call.getContactName());
            jsonObject.put("number",call.getNumber());
            jsonObject.put("userId",userid);

            System.out.println(jsonObject.toJSONString());
            System.out.println();

            final String response = ApiUtil.post("http://54.223.132.178:50000/mobile/phonecall",jsonObject.toString(),context);
            handler.post(() -> {
                if(response==null){
//                            Toast.makeText(context, "网络出问题啦~~~ ", Toast.LENGTH_SHORT).show();
                }else {
//                            Toast.makeText(context, "上传成功~~~ ", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();

    }
}

