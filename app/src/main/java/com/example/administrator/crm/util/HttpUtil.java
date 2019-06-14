package com.example.administrator.crm.util;


import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * 网络请求工具
 *
 * @author Kayll
 * @date 2019/1/7 9:12
 */

/**
 * Created by Administrator
 * Date 2019/6/9
 */
public class HttpUtil {




    public static String get(String serverUrl,String token) {

        try {
            URL url = new URL(serverUrl);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(5000);// 设置超时的时间
            conn.setConnectTimeout(5000);// 设置链接超时的时间
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("X-Auth-Token", token);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            int code = conn.getResponseCode();

            if(code==401){
                return "unAuthed";
            }else {
                InputStream inputStream = null;
                if (code != HttpURLConnection.HTTP_OK && code != HttpURLConnection.HTTP_CREATED && code != HttpURLConnection.HTTP_ACCEPTED) {
                    inputStream = conn.getErrorStream();
                } else {
                    inputStream = conn.getInputStream();
                }
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader br = new BufferedReader(inputStreamReader);
                String line;
                StringBuilder sb = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                conn.disconnect();
                return sb.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String post(String serverUrl, String json,String token)  {
        try {
            URL url = new URL(serverUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(5000);// 设置超时的时间
            conn.setConnectTimeout(5000);// 设置链接超时的时间
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("X-Auth-Token", token);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.connect();
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(json);
            wr.flush();
            wr.close();
            int code = conn.getResponseCode();
            if(code==401){
                return "unAuthed";
            }else {
                InputStream inputStream = null;
                if (code != HttpURLConnection.HTTP_OK && code != HttpURLConnection.HTTP_CREATED && code != HttpURLConnection.HTTP_ACCEPTED) {
                    inputStream = conn.getErrorStream();
                } else {
                    inputStream = conn.getInputStream();
                }
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader br = new BufferedReader(inputStreamReader);
                String line;
                StringBuilder sb = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                conn.disconnect();
                return sb.toString();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String put(String serverUrl, String json,String token)  {
        try {

            URL url = new URL(serverUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(5000);// 设置超时的时间
            conn.setConnectTimeout(5000);// 设置链接超时的时间
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("X-Auth-Token", token);
            conn.setRequestMethod("PUT");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.connect();
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(json);
            wr.flush();
            wr.close();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            conn.disconnect();
            return sb.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
