package com.sbsj.ordermatsta_client.Network;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class OrderDetailInfo {
    @SerializedName("orderdetaillist")
    private ArrayList<OrderDetailInfoColumn> orderDetailInfoColumnslist = new ArrayList<>();

    public ArrayList<OrderDetailInfoColumn> getOrderDetailInfoColumnslist() {
        return orderDetailInfoColumnslist;
    }
}
