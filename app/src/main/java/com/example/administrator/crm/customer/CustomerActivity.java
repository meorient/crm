package com.example.administrator.crm.customer;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Handler;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.administrator.crm.call.Call;
import com.example.administrator.crm.call.CallRecords;
import com.example.administrator.crm.util.ApiUtil;
import com.example.administrator.crm.util.CustomRecyclerAdapter;
import com.example.administrator.crm.util.HttpUtil;
import com.example.administrator.crm.util.MyDivider;
import com.example.administrator.crm.R;
import com.example.administrator.crm.util.SharedHelper;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class CustomerActivity extends AppCompatActivity {

    private Context context;
    private ArrayList<Customer> mData ;
    ArrayList<Customer> mData2;

    private CustomRecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private XRecyclerView mRecyclerView;
    private IntentFilter intentFilter;

    private LocalBroadcastManager localBroadcastManager;
    private Handler handler = new Handler();

    private ArrayList<Call> calls;
    private SearchView searchView;

    private ArrayList<CallRecords> recordsData ;
    private List<String> notupdated ;

    //当前页数
    private int pageNo = 1;
    //每页显示数
    private int pageSize = 15;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
        context=getApplicationContext();
        Toolbar toolbar = findViewById(R.id.activity_customer_toolbar);
        setSupportActionBar(toolbar);
        mData = new ArrayList<>();
        calls = new ArrayList<>();
        ContentResolver cr = this.getContentResolver();
        recordsData = new ArrayList<>();
        searchView = findViewById(R.id.searchView);

        mLayoutManager=new LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false);
        mAdapter= new CustomRecyclerAdapter<Customer>(mData, R.layout.customer_item) {
            @Override
            public boolean onLongClick(View view) {
                return false;
            }

            @Override
            public void onClick(View view) {

            }
            @Override
            protected void displayContents(ViewHolder holder, Customer itemData) {
                holder.setText(R.id.customer_name,itemData.getName());
            }
        };

        mAdapter.setOnItemClickListener(new CustomRecyclerAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(View view, int position) {
                Intent intent=new Intent(CustomerActivity.this,CustomerDetailActivity.class);
                intent.putExtra("name",mData2.get(position-1).getName());
                intent.putExtra("id",mData2.get(position-1).getId());
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

        mRecyclerView = findViewById(R.id.customer_view);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new MyDivider(context, LinearLayoutManager.VERTICAL));


        /**
         * 列表下拉刷新和上拉加载的监听方法
         * 下拉刷新时要将页数重新设置为1 并且将数据清空 还要将适配器清理掉 并且要将搜索文字清理掉
         *
         */
//        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
//            @Override
//            public void onRefresh() {
//                //逻辑2:下拉刷新时，将SearchView中的数据清空，并让SearchView失去焦点。
//                searchView.setQuery("", false);
//                searchView.clearFocus();
//
//                pageNo = 1;
//                if (mData != null)
//                    mData.clear();
//                mAdapter = null;
//                GetPageListData(pageNo, pageSize);
//            }
//
//            @Override
//            public void onLoadMore() {
//                pageNo++;
//                GetPageListData(pageNo, pageSize);
//
//            }
//        });
//        mRecyclerView.setRefreshing(true);

        //earchView的监听输入内容事件：监听查询内容，onQueryTextSubmit是提交监听，不符合我的需求 onQueryTextChange是实时监听，符合我们的需求
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String searchText) {
                if (!"".equals(searchView.getQuery().toString().trim())) {
                    mRecyclerView.setLoadingMoreEnabled(false);
                } else {
                    mRecyclerView.setLoadingMoreEnabled(true);
                }
                mData2  = filter(mData, searchText);
                mAdapter.setFilter(mData2);
                return true;
            }
        });
        searchView.setOnCloseListener(() -> {
            searchView.setQuery("", false);
            searchView.clearFocus();
            return true;
        });

        getCallHistoryList(context,cr);

        new Thread(() -> {
            long userid = (long)SharedHelper.getInstance("my_sp").get(context,"userid",(long)1232132);
            final String response = ApiUtil.get("http://54.223.132.178:50000/mobile/customer?userId="+userid,context);
            handler.post(() -> {
                if(response==null){
                    Toast.makeText(context, "获取客户信息失败，请检查网路", Toast.LENGTH_SHORT).show();
                }else {
                    if(JSON.isValidArray(response)){
                        JSONArray customers = JSON.parseArray(response);
                        for (int i = 0; i < customers.size(); i++) {
                            Customer customer = new Customer(customers.getJSONObject(i).getString("name"));
                            customer.setId(customers.getJSONObject(i).getLong("id"));
                            mData.add(customer);
                        }
                        mData2=mData;
                        mAdapter.updateData(mData);
                    }
                }
            });
        }).start();
    }

//    private void GetPageListData(int pageNo, int pageSize) {
//
//
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                long userid = (long)SharedHelper.getInstance("my_sp").get(context,"userid",(long)1232132);
//                String token = (String)SharedHelper.getInstance("my_sp").get(context,"login",null);
//                final String response = HttpUtil.get("http://54.223.132.178:50000/mobile/customer?userId="+userid,token,context);
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        if(response==null){
//                            Toast.makeText(context, "获取客户信息失败，请检查网路", Toast.LENGTH_SHORT).show();
//                        }else {
//                            JSONArray customers = JSON.parseArray(response);
//                            for (int i = 0; i < customers.size(); i++) {
//                                Customer customer = new Customer(customers.getJSONObject(i).getString("name"));
//                                customer.setId(customers.getJSONObject(i).getLong("id"));
//                                mData.add(customer);
//                            }
//                            mData2=mData;
//
//                            if (mAdapter == null) {
//                                mAdapter= new CustomRecyclerAdapter<Customer>(mData, R.layout.customer_item) {
//                                    @Override
//                                    public boolean onLongClick(View view) {
//                                        return false;
//                                    }
//
//                                    @Override
//                                    public void onClick(View view) {
//
//                                    }
//                                    @Override
//                                    protected void displayContents(ViewHolder holder, Customer itemData) {
//                                        holder.setText(R.id.customer_name,itemData.getName());
//                                    }
//                                };
//                                mRecyclerView.setAdapter(mAdapter);
//                                mRecyclerView.refreshComplete();
//                            } else {
//                                //将获取的元素全部加入到列表的尾部
//                                mAdapter.updateData(mData);
//                                mRecyclerView.loadMoreComplete();
//                            }
//                        }
//                    }
//                });
//            }
//        }).start();
//    }


    private void getCallHistoryList(Context context, ContentResolver cr)  {


        Cursor cs;
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CALL_LOG}, 100);
        }
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS}, 100);
        }
        cs = cr.query(CallLog.Calls.CONTENT_URI, new String[]{
                CallLog.Calls.CACHED_NAME,
                CallLog.Calls.NUMBER,
                CallLog.Calls.TYPE,
                CallLog.Calls.DATE,
                CallLog.Calls.DURATION,
        }, null, null, CallLog.Calls.DEFAULT_SORT_ORDER);
        int i = 0;
        if (cs != null && cs.getCount() > 0) {
            for (cs.moveToFirst(); !cs.isAfterLast() & i < 50; cs.moveToNext()) {
                String callName = cs.getString(0);
                String callNumber = cs.getString(1);
                if (callName == null || callName.equals("")) {
                    String[] cols = {ContactsContract.PhoneLookup.DISPLAY_NAME};
                    //设置查询条件
                    String selection = ContactsContract.CommonDataKinds.Phone.NUMBER + "='" + callNumber + "'";
                    Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            cols, selection, null, null);
                    int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        callName = cursor.getString(nameFieldColumnIndex);
                    }
                    cursor.close();
                }
                int callType = Integer.parseInt(cs.getString(2));
                int callTypeStr = -2;
                switch (callType) {
                    case CallLog.Calls.INCOMING_TYPE:
                        callTypeStr = 1;//"呼入"
                        break;
                    case CallLog.Calls.OUTGOING_TYPE:
                        callTypeStr = 2;//"呼出";
                        break;
                    case CallLog.Calls.MISSED_TYPE:
                        callTypeStr = 3;//"未接";
                        break;
                }
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date callDate = new Date(Long.parseLong(cs.getString(3)));
                int callDuration = Integer.parseInt(cs.getString(4));
                int min = callDuration / 60;
                int sec = callDuration % 60;
                String callDurationStr = min + "分" + sec + "秒";

                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
                Date today = new Date();

                if ((sdf2.format(callDate).equals(sdf2.format(today)))) {
                    send(callDuration,callNumber,callDate,callTypeStr);
                }
                i++;
            }
        }
    }

    private ArrayList<Customer> filter(ArrayList<Customer> models, String query) {
        query = query.toLowerCase();
        final ArrayList<Customer> filteredModelList = new ArrayList<>();
        for (Customer model : models) {
            final String text = model.getName();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }


    private void send(int duration,String number,Date date,int type){
        new Thread(() -> {
            long userid = (long)SharedHelper.getInstance("my_sp").get(context,"userid",(long)1232132);

                notupdated = new LinkedList<>();
                String response = ApiUtil.get("http://54.223.132.178:50000/mobile/phonecall?userId="+userid,context);
                if(response==null){

                }else {
                    if(JSON.isValidArray(response)){
                        JSONArray records = JSON.parseArray(response);
                        for (int i = 0; i < records.size(); i++) {
                            notupdated.add(records.getJSONObject(i).getString("number"));
                        }
                    }
                }

                if(notupdated.contains(number)){
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("duration",duration);
                    jsonObject.put("number",number);
                    jsonObject.put("startedAt",sdf.format(date));
                    jsonObject.put("userId",userid);
                    jsonObject.put("type",type);

                    final String response2 = ApiUtil.post("http://54.223.132.178:50000/mobile/localCallLog",jsonObject.toString(),context);
                    handler.post(() -> {
                        if(response2==null){
//                            Toast.makeText(context, "网络出问题啦~~~ ", Toast.LENGTH_SHORT).show();
                        }else {
                            System.out.println(response2);
                        }
                    });
                }

        }).start();

    }
}
