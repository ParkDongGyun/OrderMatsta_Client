package com.sbsj.ordermatsta_client.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.sbsj.ordermatsta_client.Activity.QRCode.QRCode;
import com.sbsj.ordermatsta_client.Adapter.StoreAdapter;
import com.sbsj.ordermatsta_client.Network.GoodsInfo;
import com.sbsj.ordermatsta_client.Network.GoodsInfoColumn;
import com.sbsj.ordermatsta_client.Network.MenuCateInfo;
import com.sbsj.ordermatsta_client.Network.MenuCateInfoColumn;
import com.sbsj.ordermatsta_client.Network.OrderDetailInfo;
import com.sbsj.ordermatsta_client.Network.OrderDetailInfoColumn;
import com.sbsj.ordermatsta_client.Network.OrderInfoColumn;
import com.sbsj.ordermatsta_client.Util.OnItemClickListener;
import com.sbsj.ordermatsta_client.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreActivity extends BaseActivity implements View.OnClickListener {

    private final String TAG = "Store_Activity";

    private ScrollView scrollView;
    private final float maxScrollheight = 1500;

    private Toolbar toolbar;
    private ImageView iv_storeimage;
    private TextView tv_storename;
    private TextView tv_storeaddress;
    private TextView tv_storeexplain;
    private RelativeLayout rl_review;
    private TextView tv_review;

    private RecyclerView recyclerView;
    private TextView tv_nogoods;

    private TextView tv_sd_totalprice;
    private Button btn_order;

    private StoreAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_store);

        progressON("Loading...");

        initView();

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.btn_header_back);// 툴바 왼쪽 버튼 이미지
        toolbar.setBackgroundColor(getResources().getColor(R.color.tm_color));

        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                float cur_height = scrollView.getScrollY() / maxScrollheight;

                if (cur_height < 1) {
                    int colorres = ColorUtils.setAlphaComponent(getResources().getColor(R.color.matsta_color), (int) (255 * cur_height));
                    toolbar.setBackgroundColor(colorres);
                }
            }
        });

        tv_storename.setText(BaseActivity.storeInfo.getName());
        tv_storeaddress.setText(BaseActivity.storeInfo.getAddress());
        if (!BaseActivity.storeInfo.getImage().isEmpty())
            iv_storeimage.setImageBitmap(StringtoBitmap(BaseActivity.storeInfo.getImage(), TAG));

        final ArrayList<OrderDetailInfoColumn> arrayList = new ArrayList<>();
        adapter = new StoreAdapter(this, arrayList, new OnItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                OrderDetailInfoColumn column = adapter.getDatalist().get(position);
                int amount = column.getAmount();
                switch (v.getId()) {
                    case R.id.btn_goodsminus:
                        if (amount > 0) {
                            column.setAmount(--amount);
                            column.setPrice(amount * BaseActivity.goodslist.get(position).getPrice());

                            int total = 0;
                            for (int i=0;i<adapter.getDatalist().size();++i) {
                                total += adapter.getDatalist().get(i).getPrice();
                            }
                            tv_sd_totalprice.setText(printPrice(getString(R.string.oneprice_msg), total));

                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getApplicationContext(), "0개 이하로 주문 할 수 없습니다.", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.btn_goodsplus:
                        if (amount < 10) {
                            column.setAmount(++amount);
                            column.setPrice(amount * BaseActivity.goodslist.get(position).getPrice());

                            int total = 0;
                            for (int i=0;i<adapter.getDatalist().size();++i) {
                                total += adapter.getDatalist().get(i).getPrice();
                            }
                            tv_sd_totalprice.setText(printPrice(getString(R.string.oneprice_msg), total));

                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getApplicationContext(), "10개 이상으로 주문 할 수 없습니다.", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        getGoodsInfo(storeInfo.getId());
    }

    @Override
    protected void onResume() {
        super.onResume();

        initValue();
    }

    private void initValue() {
        adapter.getDatalist().clear();
        getGoodsInfo(storeInfo.getId());
        tv_sd_totalprice.setText(printPrice(getString(R.string.oneprice_msg), 0));
    }

    private void initView() {
        scrollView = findViewById(R.id.sv_sd);
        toolbar = findViewById(R.id.tb_storedetail);

        tv_storename = findViewById(R.id.tv_sd_name);
        tv_storeaddress = findViewById(R.id.tv_sd_address);
        iv_storeimage = findViewById(R.id.iv_sd_image);
        tv_storeexplain = findViewById(R.id.tv_sd_explain);

        rl_review = findViewById(R.id.rl_sd_review);
        rl_review.setOnClickListener(this);
        tv_review = findViewById(R.id.tv_sd_review);

        recyclerView = findViewById(R.id.rcv_sd);
        tv_nogoods = findViewById(R.id.tv_nogoods);

        tv_sd_totalprice = findViewById(R.id.tv_sd_totalprice);
        btn_order = findViewById(R.id.btn_order);
        btn_order.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_sd_review:
                Toast.makeText(this, "준비중인 서비스입니다.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_order:
                int total = 0;
                for (int i=0;i<adapter.getDatalist().size();++i) {
                    total += adapter.getDatalist().get(i).getPrice();
                }

                if(total > 0) {
                    BaseActivity.orderdetaillist = adapter.getDatalist();
                    startActivity(new Intent(this, PayActivity.class));
                } else {
                    Toast.makeText(this, "메뉴를 추가해주세요.", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    private void getGoodsInfo(int store_id) {
        Call<GoodsInfo> call = getDBService().getGoodsInfo(store_id);
        call.enqueue(new Callback<GoodsInfo>() {
            @Override
            public void onResponse(Call<GoodsInfo> call, Response<GoodsInfo> response) {
                if (response.message().equals(getString(R.string.NetworkCheck))) {
                    ArrayList<GoodsInfoColumn> list = response.body().getGoodsinfolist();
                    BaseActivity.goodslist = list;
                    ArrayList<OrderDetailInfoColumn> orderDetailList = new ArrayList<>();
                    for (int i = 0; i < list.size(); ++i) {
                        orderDetailList.add(new OrderDetailInfoColumn(list.get(i).getId()));
                    }
                    updateAdapter(orderDetailList);
                } else {
                    Log.i(TAG, "getGoodsInfo Error : " + response.message());
                }
            }

            @Override
            public void onFailure(Call<GoodsInfo> call, Throwable t) {
                Log.i(TAG, "getGoodsInfo Fail : " + t.getMessage());
            }
        });
    }

    private void updateAdapter(ArrayList<OrderDetailInfoColumn> list) {
        if (BaseActivity.goodslist.get(0).getId() != -1) {
            adapter.setDatalist(list);
            tv_nogoods.setVisibility(View.INVISIBLE);
        } else {
            adapter.getDatalist().clear();
            tv_nogoods.setVisibility(View.VISIBLE);
        }
        adapter.notifyDataSetChanged();
        progressOFF();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.tb_main, menu);
        MenuItem tb_cart = menu.findItem(R.id.cart_list);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.cart_list:
                Toast.makeText(this, getString(R.string.prepare_service), Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
