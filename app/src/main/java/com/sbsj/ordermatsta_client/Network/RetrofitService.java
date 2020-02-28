package com.sbsj.ordermatsta_client.Network;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RetrofitService {
    @FormUrlEncoded
    @POST("get_memberinfo.php")
    Call<MemberInfo> getMemberInfo(@Field("directory") String directory, @Field("sns_id") String sns_id);

    @FormUrlEncoded
    @POST("insert_memberinfo.php")
    Call<String> insertMemberInfo(@Field("directory") String directory, @Field("sns_id") String sns_id);

    @FormUrlEncoded
    @POST("get_storeinfo_client.php")
    Call<StoreInfo> getStoreInfo(@Field("id") String id);

//    @FormUrlEncoded
//    @POST("get_menucateinfo.php")
//    Call<MenuCateInfo> getCategoryInfo(@Field("store_id") String store_id);

    @FormUrlEncoded
    @POST("get_goodsinfo.php")
    Call<GoodsInfo> getGoodsInfo(@Field("store_id") int store_id);

    @FormUrlEncoded
    @POST("insert_orderinfo.php")
    Call<String> insertOrderInfo(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("insert_orderdetailinfo.php")
    Call<String> insertOrderDetailInfo(@Field("order_id") int order_id, @Field("goods_id[]") ArrayList<Integer> goods_id, @Field("amount[]") ArrayList<Integer> amount, @Field("price[]") ArrayList<Integer> price);

    @FormUrlEncoded
    @POST("get_orderdetailinfo.php")
    Call<OrderDetailInfo> getOrderDetailInfo(@Field("order_id") int order_id);

    @FormUrlEncoded
    @POST("get_masterdeviceinfo.php")
    Call<MasterDeviceInfo> getMasterDeviceInfo(@Field("master_id") int master_id);
}