package com.sbsj.ordermatsta_client.Util;

import com.google.gson.annotations.SerializedName;
import com.sbsj.ordermatsta_client.Network.OrderDetailInfo;
import com.sbsj.ordermatsta_client.Network.OrderDetailInfoColumn;

import java.io.Serializable;
import java.util.ArrayList;

public class OrderInfoBoss implements Serializable {
    @SerializedName("order_id")
    private int order_id;
    @SerializedName("token")
    private String token;
    @SerializedName("orderdetail")
    private ArrayList<OrderDetailInfoColumn> orderdetail;

    public OrderInfoBoss(int order_id, String token, ArrayList<OrderDetailInfoColumn> orderdetail) {
        this.order_id = order_id;
        this.token = token;
        this.orderdetail = orderdetail;
    }

    public int getOrder_id() {
        return order_id;
    }

    public String getToken() {
        return token;
    }

    public ArrayList<OrderDetailInfoColumn> getOrderdetail() {
        return orderdetail;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setOrderdetail(ArrayList<OrderDetailInfoColumn> orderdetail) {
        this.orderdetail = orderdetail;
    }
}