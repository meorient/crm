package com.example.administrator.crm.customer;


/**
 * Created by Administrator
 * Date 2019/6/9
 */

public class Customer {
    private String name;
    private long id;

    public Customer(String name){
        this.name =name;
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
}
