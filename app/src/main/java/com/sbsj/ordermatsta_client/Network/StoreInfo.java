package com.sbsj.ordermatsta_client.Network;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class StoreInfo {
    @SerializedName("storelist")
    private ArrayList<StoreInfoColumn> storelist = new ArrayList<>();

    public ArrayList<StoreInfoColumn> getStorelist() {
        return storelist;
    }
}
