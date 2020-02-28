package com.sbsj.ordermatsta_client.Network;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MenuCateInfo {
    @SerializedName("menucatelist")
    private ArrayList<MenuCateInfoColumn> menucatelist = new ArrayList<>();

    public ArrayList<MenuCateInfoColumn> getMenuCatelist() {
        return menucatelist;
    }
}
