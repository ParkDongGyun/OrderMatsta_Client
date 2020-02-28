package com.sbsj.ordermatsta_client.Network;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MasterDeviceInfo {
    @SerializedName("masterdevicelist")
    private ArrayList<MasterDeviceInfoColumn> masterdevicelist = new ArrayList<>();

    public ArrayList<MasterDeviceInfoColumn> getMasterdevicelist() {
        return masterdevicelist;
    }
}