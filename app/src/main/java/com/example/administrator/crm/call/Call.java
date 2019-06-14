package com.example.administrator.crm.call;

import java.io.Serializable;

/**
 * Created by Administrator
 * Date 2019/6/11
 */

public class Call implements Serializable {
    private long id;
    private String date;
    private String number;
    private String contactName;

    public Call(long id,String date,String number,String name){
        this.id = id;
        this.date=date;
        this.number=number;
        this.contactName=name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }
}
