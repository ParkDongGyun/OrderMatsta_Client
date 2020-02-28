package com.sbsj.ordermatsta_client.Network;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class OrderInfo {
    @SerializedName("orderlist")
    private ArrayList<OrderInfoColumn> orderlist = new ArrayList<>();

    public ArrayList<OrderInfoColumn> getMenuCatelist() {
        return orderlist;
    }
}
