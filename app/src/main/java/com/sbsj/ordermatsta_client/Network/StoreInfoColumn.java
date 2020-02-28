package com.sbsj.ordermatsta_client.Network;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class StoreInfoColumn implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("master_id")
    private int master_id;
    @SerializedName("name")
    private String name;
    @SerializedName("phone")
    private String phone;
    @SerializedName("category")
    private String category;
    @SerializedName("address")
    private String address;
    @SerializedName("image")
    private String image;

    public int getId() {
        return id;
    }

    public int getMaster_id() {
        return master_id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getCategory() {
        return category;
    }

    public String getAddress() {
        return address;
    }

    public String getImage() {
        return image;
    }
}
