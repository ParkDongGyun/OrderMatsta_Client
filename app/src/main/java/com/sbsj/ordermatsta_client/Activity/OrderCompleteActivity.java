package com.sbsj.ordermatsta_client.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.sbsj.ordermatsta_client.R;

public class OrderCompleteActivity extends AppCompatActivity {

    private Button btn_order_complete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_complete);

        btn_order_complete = findViewById(R.id.btn_order_complete);
        btn_order_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
