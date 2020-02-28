package com.sbsj.ordermatsta_client.Network;

import com.google.gson.annotations.SerializedName;
import com.sbsj.ordermatsta_client.Activity.BaseActivity;

import java.io.Serializable;

public class OrderInfoColumn implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("member_id")
    private int member_id;
    @SerializedName("store_id")
    private int store_id;
    @SerializedName("receipt_id")
    private String receipt_id;
    @SerializedName("paymethod")
    private String paymethod;
    @SerializedName("total_price")
    private int total_price;
    @SerializedName("coupon")
    private int coupon;
    @SerializedName("wrapping")
    private int wrapping;
    @SerializedName("date")
    private String date;

    public OrderInfoColumn(int store_id, int total_price, String date) {
        this.id = -1;
        this.member_id = BaseActivity.memberInfo.getId();
        this.store_id = store_id;
        this.receipt_id = "-1";
        this.paymethod = "-1";
        this.total_price = total_price;
        this.coupon = -1;
        this.wrapping = 0;
        this.date = date;


    }

    public int getId() {
        return id;
    }

    public int getMember_id() {
        return member_id;
    }

    public int getStore_id() {
        return store_id;
    }

    public String getReceipt_id() {
        return receipt_id;
    }

    public String getPaymethod() {
        return paymethod;
    }

    public int getTotal_price() {
        return total_price;
    }

    public int getCoupon() {
        return coupon;
    }

    public int getWrapping() {
        return wrapping;
    }

    public String getDate() {
        return date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMember_id(int member_id) {
        this.member_id = member_id;
    }

    public void setStore_id(int store_id) {
        this.store_id = store_id;
    }

    public void setReceipt_id(String receipt_id) {
        this.receipt_id = receipt_id;
    }

    public void setPaymethod(String paymethod) {
        this.paymethod = paymethod;
    }

    public void setTotal_price(int total_price) {
        this.total_price = total_price;
    }

    public void setCoupon(int coupon) {
        this.coupon = coupon;
    }

    public void setWrapping(int wrapping) {
        this.wrapping = wrapping;
    }

    public void setDate(String date) {
        this.date = date;
    }
}