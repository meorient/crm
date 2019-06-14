package com.example.administrator.crm.customer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.example.administrator.crm.util.ApiUtil;
import com.example.administrator.crm.util.CustomRecyclerAdapter;
import com.example.administrator.crm.util.HttpUtil;
import com.example.administrator.crm.util.MyDivider;
import com.example.administrator.crm.R;
import com.example.administrator.crm.util.SharedHelper;
import com.example.administrator.crm.contact.Contact;
import com.example.administrator.crm.contact.ContactDetailActivity;

import java.util.ArrayList;

/**
 * Created by Administrator
 * Date 2019/6/9
 */
public class CustomerDetailActivity extends AppCompatActivity {

    private Context context;
    private ArrayList<Contact> mData ;
    private CustomRecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private Handler handler = new Handler();
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_detail);
        context=getApplicationContext();
        Toolbar toolbar = findViewById(R.id.activity_customerdetail_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);//设置Navigation 图标
        toolbar.setNavigationOnClickListener((v) ->finish());

        textView = findViewById(R.id.customername);
        Intent intent = getIntent();
        String str = intent.getStringExtra("name");
        long companyid = intent.getLongExtra("id",0);
        System.out.println(companyid);
        textView.setText(str);


        mData=new ArrayList<>();
        new Thread(() -> {
            final String response = ApiUtil.get("http://54.223.132.178:50000/mobile/contact?customerId="+companyid,context);
            handler.post(() -> {
                if(response==null){
                    Toast.makeText(context, "获取联系人信息失败，请检查网络连接 ", Toast.LENGTH_SHORT).show();
                }else {
                    if(JSON.isValidArray(response)){
                        JSONArray contacts = JSON.parseArray(response);
                        for (int i = 0; i < contacts.size(); i++) {
                            Contact contact = new Contact(contacts.getJSONObject(i).getString("name"), contacts.getJSONObject(i).getString("mobile1"));
                            contact.setHomephone(contacts.getJSONObject(i).getString("phone1"));
                            contact.setId(contacts.getJSONObject(i).getLong("id"));
                            contact.setTel(contacts.getJSONObject(i).getString("phone2"));
                            mData.add(contact);
                        }
                        mAdapter.updateData(mData);
                    }

                }
            });
        }).start();


        mLayoutManager=new LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false);
        mAdapter= new CustomRecyclerAdapter<Contact>(mData, R.layout.contact_item) {
            @Override
            public boolean onLongClick(View view) {
                return false;
            }

            @Override
            public void onClick(View view) {

            }

            @Override
            protected void displayContents(ViewHolder holder, Contact itemData) {
                holder.setText(R.id.contact_name,itemData.getName());
            }
        };

        mAdapter.setOnItemClickListener(new CustomRecyclerAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(View view, int position) {
                //Toast.makeText(context,"点击了第"+position,Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(CustomerDetailActivity.this,ContactDetailActivity.class);
                intent.putExtra("id",mData.get(position).getId());
                intent.putExtra("name",mData.get(position).getName());
                intent.putExtra("mobile",mData.get(position).getMobile());
                intent.putExtra("phone",mData.get(position).getTel());
                intent.putExtra("homephone",mData.get(position).getHomephone());
                startActivity(intent);

            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

        mRecyclerView = findViewById(R.id.contact_view);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new MyDivider(context, LinearLayoutManager.VERTICAL));


    }
}
