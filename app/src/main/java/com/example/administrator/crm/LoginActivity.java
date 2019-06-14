package com.example.administrator.crm;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.administrator.crm.customer.CustomerActivity;
import com.example.administrator.crm.util.ApiUtil;
import com.example.administrator.crm.util.HttpUtil;
import com.example.administrator.crm.util.SharedHelper;

public class LoginActivity extends AppCompatActivity {

    private Context context;

    private EditText account;
    private EditText password;
    private Button button;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context=getApplicationContext();
        Toolbar toolbar = findViewById(R.id.activity_login_toolbar);
        setSupportActionBar(toolbar);

        account = findViewById(R.id.email);
        password = findViewById(R.id.password);
        button = findViewById(R.id.email_sign_in_button);


        button.setOnClickListener(view -> new Thread(() -> {

            JSONObject json = new JSONObject();
            json.put("username",account.getText().toString());
            json.put("password",password.getText().toString());
            final String response = HttpUtil.post("http://54.223.132.178:50000/api/login",json.toString(),null);

                    if(response==null){
                        //Toast.makeText(context, "网络出问题啦~~~ ", Toast.LENGTH_SHORT).show();
                    }else {
                        if(JSON.isValidObject(response)){
                            JSONObject data = JSON.parseObject(response);
                            String token = data.getString("access_token");
                            if(token!=null){
                                SharedHelper.getInstance("my_sp").put(context,"login", token);
                                String user = ApiUtil.get(" http://54.223.132.178:50000/mobile/employee?email="+account.getText().toString(),context);
                                if(user==null){
                                    //Toast.makeText(context, "获取用户id失败~~~ ", Toast.LENGTH_SHORT).show();
                                }else{
                                    JSONObject userinfo = JSON.parseObject(user);
                                    long userid = userinfo.getLong("id");
                                    SharedHelper.getInstance("my_sp").put(context,"email", account.getText().toString());
                                    SharedHelper.getInstance("my_sp").put(context,"password", password.getText().toString());
                                    SharedHelper.getInstance("my_sp").put(context,"userid", userid);
                                    //Toast.makeText(context, "获取用户id成功~~~ ", Toast.LENGTH_SHORT).show();
                                }
                                Intent intent=new Intent(LoginActivity.this,CustomerActivity.class);
                                startActivity(intent);
                                LoginActivity.this.finish();
                            }else{
                                //Toast.makeText(context,"请检查账号密码有没有错噢",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
        }).start());
    }
}
