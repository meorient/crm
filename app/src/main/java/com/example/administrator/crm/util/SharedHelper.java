package com.example.administrator.crm.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

/**
 * Created by Kayll on 2018/3/17.
 */

public class SharedHelper {

    private String filename ;
    private Context context;

    private static SharedHelper sharedHelper;

    private SharedHelper(String filename){
        this.filename=filename;
    }

    public static SharedHelper getInstance(String filename){
        if(sharedHelper==null)
            sharedHelper = new SharedHelper(filename);
        return sharedHelper;
    }


    /**
     * 保存数据
     */
    public  void put(Context context, String key, Object obj) {
        SharedPreferences sp = context.getSharedPreferences(filename, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (obj instanceof Boolean) {
            editor.putBoolean(key, (Boolean) obj);
        } else if (obj instanceof Float) {
            editor.putFloat(key, (Float) obj);
        } else if (obj instanceof Integer) {
            editor.putInt(key, (Integer) obj);
        } else if (obj instanceof Long) {
            editor.putLong(key, (Long) obj);
        } else {
            String string = "";
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = null;
            try {
                objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                objectOutputStream.writeObject(obj);
                string = new String(Base64.encode(byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
                objectOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            editor.putString(key, string);
        }
        editor.commit();
    }


    /**
     * 获取指定数据
     */
    public  Object get(Context context, String key, Object defaultObj) {
        SharedPreferences sp = context.getSharedPreferences(filename, context.MODE_PRIVATE);
        if (defaultObj instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObj);
        } else if (defaultObj instanceof Float) {
            return sp.getFloat(key, (Float) defaultObj);
        } else if (defaultObj instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObj);
        } else if (defaultObj instanceof Long) {
            return sp.getLong(key, (Long) defaultObj);
        } else if (defaultObj instanceof String) {
            return sp.getString(key, (String) defaultObj);
        }else{
            String string = sp.getString(key, null);
            if (string != null) {
                byte[] mobileBytes = Base64.decode(string.getBytes(), Base64.DEFAULT);
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(mobileBytes);
                ObjectInputStream objectInputStream = null;
                try {
                    objectInputStream = new ObjectInputStream(byteArrayInputStream);
                    Object object = objectInputStream.readObject();
                    objectInputStream.close();
                    return object;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            } else {
                return null;
            }
        }
    }

    /**
     * 删除指定数据
     */
    public  void remove(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(filename, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.commit();
    }

    /**
     * 返回所有键值对
     */
    public  Map<String, ?> getAll(Context context) {
        SharedPreferences sp = context.getSharedPreferences(filename, context.MODE_PRIVATE);
        Map<String, ?> map = sp.getAll();
        return map;
    }

    /**
     * 删除所有数据
     */
    public  void clear(Context context) {
        SharedPreferences sp = context.getSharedPreferences(filename, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }

    /**
     * 检查key对应的数据是否存在
     */
    public  boolean contains(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(filename, context.MODE_PRIVATE);
        return sp.contains(key);
    }


}
