package com.example.administrator.crm.call;

import java.util.Date;


/**
 * Created by Administrator
 * Date 2019/6/9
 */


public class CallRecords {
    private long contactid;
    private String number;
    private int time;
    private Date date;
    private String name;
    private long id;
    private int type;

    @Override
    public String toString() {
        return "CallRecords{" +
                "contactid=" + contactid +
                ", number='" + number + '\'' +
                ", time=" + time +
                ", date=" + date +
                ", name='" + name + '\'' +
                ", id=" + id +
                ", type=" + type +
                '}';
    }

    public CallRecords(String number, String name, Date date){
        this.name=name;
        this.number=number;
        this.date=date;
    }
    public CallRecords(String number,  Date date){
        this.number=number;
        this.date=date;
    }

    public long getContactid() {
        return contactid;
    }

    public void setContactid(long contactid) {
        this.contactid = contactid;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
