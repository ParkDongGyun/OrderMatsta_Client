package com.sbsj.ordermatsta_client.Activity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sbsj.ordermatsta_client.Login.Kakao.GlobalApplication;
import com.sbsj.ordermatsta_client.Network.GoodsInfoColumn;
import com.sbsj.ordermatsta_client.Network.MemberInfoColumn;
import com.sbsj.ordermatsta_client.Network.MenuCateInfo;
import com.sbsj.ordermatsta_client.Network.MenuCateInfoColumn;
import com.sbsj.ordermatsta_client.Network.OrderDetailInfoColumn;
import com.sbsj.ordermatsta_client.Network.OrderInfoColumn;
import com.sbsj.ordermatsta_client.Network.RetrofitOrderDB;
import com.sbsj.ordermatsta_client.Network.RetrofitService;
import com.sbsj.ordermatsta_client.Network.StoreInfoColumn;
import com.sbsj.ordermatsta_client.R;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class BaseActivity extends AppCompatActivity {

    private RetrofitService orderDB;
    private final String tempDateName = "profile";

    static public MemberInfoColumn memberInfo;
    static public boolean login = false;
    static public String b_dir = "";
    static public String b_sns = "";
    static public StoreInfoColumn storeInfo;
    static public ArrayList<MenuCateInfoColumn> menucatelist = new ArrayList<>();
    static public ArrayList<GoodsInfoColumn> goodslist = new ArrayList<>();
    static public ArrayList<OrderInfoColumn> orderlist = new ArrayList<>();
    static public ArrayList<OrderDetailInfoColumn> orderdetaillist = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        orderDB = RetrofitOrderDB.getClient().create(RetrofitService.class);
    }

    public RetrofitService getDBService() {
        return orderDB;
    }

    public void saveTempLogin(String directory, String sns_id) {
        SharedPreferences tempDate = getSharedPreferences(tempDateName, MODE_PRIVATE);
        SharedPreferences.Editor tempEdit = tempDate.edit();
        tempEdit.putBoolean("login", true);
        tempEdit.putString(getString(R.string.directory), directory);
        tempEdit.putString(getString(R.string.sns_id),sns_id);
        BaseActivity.login = true;
        tempEdit.apply();
    }

    /*쉐어드값 불러오기*/
    public boolean loadShared() {
        SharedPreferences pref = getSharedPreferences(tempDateName, MODE_PRIVATE);
        BaseActivity.login = pref.getBoolean("login", false);
        BaseActivity.b_dir= pref.getString(getString(R.string.directory),"");
        BaseActivity.b_sns = pref.getString(getString(R.string.sns_id),"");
        return BaseActivity.login;
    }

    public Bitmap StringtoBitmap(String image, String TAG) {
        try {
            byte[] encodeByte = Base64.decode(image, Base64.NO_WRAP);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            Log.i(TAG, "Error StringtoBitmap: " + e.getMessage());
            return null;
        }
    }

    public int calculateOnePrice(int price, int amount) {
        int onePrice = price * amount;

        return onePrice;
    }

    public String printPrice(String priceForm, int price) {
        DecimalFormat myFormatter = new DecimalFormat("###,###");
        return String.format(priceForm, myFormatter.format(price));
    }

    public void progressON() {
        GlobalApplication.getInstance().progressON(this, null);
    }

    public void progressON(String message) {
        GlobalApplication.getInstance().progressON(this, message);
    }

    public void progressOFF() {
        GlobalApplication.getInstance().progressOFF();
    }
}
