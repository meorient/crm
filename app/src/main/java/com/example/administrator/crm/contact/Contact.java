package com.example.administrator.crm.contact;

import java.io.Serializable;


/**
 * Created by Administrator
 * Date 2019/6/9
 */

public class Contact implements Serializable {
    private long id;
    private String name;
    private String mobile;
    private String tel;
    private String homephone;

    public Contact(){}

    public Contact(String name, String mobile){
        this.name=name;
        this.mobile=mobile;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getHomephone() {
        return homephone;
    }

    public void setHomephone(String homephone) {
        this.homephone = homephone;
    }
}
