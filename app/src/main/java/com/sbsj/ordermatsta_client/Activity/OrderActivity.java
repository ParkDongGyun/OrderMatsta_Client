package com.sbsj.ordermatsta_client.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.sbsj.ordermatsta_client.Adapter.OrderAdapter;
import com.sbsj.ordermatsta_client.Util.OnItemClickListener;
import com.sbsj.ordermatsta_client.R;

public class OrderActivity extends BaseActivity {

    private final String TAG = "Order_Activity";

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private Button btn_order;

    private OrderAdapter adapter;

    private int totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        toolbar = findViewById(R.id.tb_order);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        adapter = new OrderAdapter(this, BaseActivity.orderlist, BaseActivity.goodslist, new OnItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                switch (v.getId()) {
                    case R.id.btn_ol_upAmount:
                        adapter.setAmount(position, true);
                        break;
                    case R.id.btn_ol_downAmount:
                        adapter.setAmount(position, false);
                        break;
                    case R.id.btn_ol_delete:
                        adapter.deleteitem(position);
                        BaseActivity.orderlist.remove(position);
                        break;
                }
            }
        });
        recyclerView = findViewById(R.id.rcv_order);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

//        btn_order = findViewById(R.id.btn_complete);
//        btn_order.setText(printPrice(getResources().getString(R.string.totalprice_msg), calculateTotalPrice()));
//        btn_order.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), PayActivity.class);
//                startActivity(intent);
//            }
//        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
