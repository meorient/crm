package com.example.administrator.crm.util;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * Created by Administrator
 * Date 2019/6/13
 */
public class ApiUtil {


    public static void login(Context context){
        JSONObject json = new JSONObject();
        json.put("username",(String)SharedHelper.getInstance("my_sp").get(context,"email",null));
        json.put("password",(String)SharedHelper.getInstance("my_sp").get(context,"password",null));
        String response = HttpUtil.post("http://54.223.132.178:50000/api/login",json.toString(),null);
        if(response != null){
            if(JSON.isValidObject(response)){
                JSONObject data = JSON.parseObject(response);
                String tokennew = data.getString("access_token");
                if(tokennew!=null) {
                    SharedHelper.getInstance("my_sp").put(context, "login", tokennew);
                }
            }
        }
    }

    public static String get(String url,Context context){

        String token = (String)SharedHelper.getInstance("my_sp").get(context,"login",null);
        String response = HttpUtil.get(url,token);

        if(response!=null){
            if(response.equals("unAuthed")){
                login(context);
                String tokennew = (String)SharedHelper.getInstance("my_sp").get(context,"login",null);
                return HttpUtil.get(url,tokennew);
            }else{
                return response;
            }
        }
        return null;
    }

    public static String post(String url,String json ,Context context){
        String token = (String)SharedHelper.getInstance("my_sp").get(context,"login",null);
        String response = HttpUtil.post(url,json,token);

        if(response!=null){
            if(response.equals("unAuthed")){
                login(context);
                String tokennew = (String)SharedHelper.getInstance("my_sp").get(context,"login",null);
                return HttpUtil.post(url,json,tokennew);
            }else{
                return response;
            }
        }
        return null;
    }
}
