package com.sbsj.ordermatsta_client.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.sbsj.ordermatsta_client.Adapter.PayAdapter;
import com.sbsj.ordermatsta_client.Adapter.PayAgreeAdapter;
import com.sbsj.ordermatsta_client.Network.GoodsInfoColumn;
import com.sbsj.ordermatsta_client.Network.MasterDeviceInfo;
import com.sbsj.ordermatsta_client.Network.MasterDeviceInfoColumn;
import com.sbsj.ordermatsta_client.Network.OrderDetailInfo;
import com.sbsj.ordermatsta_client.Network.OrderDetailInfoColumn;
import com.sbsj.ordermatsta_client.Network.OrderInfoColumn;
import com.sbsj.ordermatsta_client.Util.OnItemClickListener;
import com.sbsj.ordermatsta_client.R;
import com.sbsj.ordermatsta_client.Util.OrderInfoBoss;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kr.co.bootpay.Bootpay;
import kr.co.bootpay.enums.Method;
import kr.co.bootpay.enums.PG;
import kr.co.bootpay.enums.UX;
import kr.co.bootpay.listener.CancelListener;
import kr.co.bootpay.listener.CloseListener;
import kr.co.bootpay.listener.ConfirmListener;
import kr.co.bootpay.listener.DoneListener;
import kr.co.bootpay.listener.ErrorListener;
import kr.co.bootpay.listener.ReadyListener;
import kr.co.bootpay.model.BootExtra;
import kr.co.bootpay.model.BootUser;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PayActivity extends BaseActivity implements View.OnClickListener {

    final private String TAG = "Pay_Activity";

    private Toolbar toolbar;

    private RadioGroup rg_store;
    private boolean iswrapping = false;

    private LinearLayout ll_pay_table;
    private EditText et_pay_table;

    private EditText et_pay_coupon;
    private Button btn_pay_coupon;

    private RadioGroup rg_paymethod1;
    private RadioGroup rg_paymethod2;
    private Method method;

    private RecyclerView recyclerView;

    private TextView tv_price;
    private TextView tv_discount;
    private TextView tv_total;

    private LinearLayout ll_pay_agree_total;
    private CheckBox cb_pay_agree_total;
    private TextView tv_pay_agree_total;
    private PayAgreeAdapter payAgreeAdapter;

    private RecyclerView rcv_pay_agree;

    private Button btn_pay;

    int goods_price = 0;
    int discount_price = 0;
    int total_price = 0;

    private int order_id;

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private String token;

    private final String FCM_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send";
    private final String SERVER_KEY = "AAAAxMlWc-Q:APA91bGQVfI3T-sR2NWbEzK4CK0zVnsRI4QEkE2iqbo74vxt20N_LpboDFC5gN3CY36eWfPagWOrQK5HuaFJ3cJX7gY38mTChuIUSdPwWfjTA4YgkwBf7XACNDvsvlNA6eLGvtBIGo9-";

    ArrayList<OrderDetailInfoColumn> orderdetaillist = new ArrayList<>();
    ArrayList<GoodsInfoColumn> goodslist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        toolbar = findViewById(R.id.tb_pay);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.btn_header_back);// 툴바 왼쪽 버튼 이미지

        ll_pay_table = findViewById(R.id.ll_pay_tablenum);
        et_pay_table = findViewById(R.id.et_pay_tablenum);

        rg_store = findViewById(R.id.rg_pay_store);
        ((RadioButton) rg_store.getChildAt(0)).setChecked(true);
        rg_store.setOnCheckedChangeListener(radioListener1);

        et_pay_coupon = findViewById(R.id.et_pay_coupon);
        et_pay_coupon.setEnabled(false);
        btn_pay_coupon = findViewById(R.id.btn_pay_coupon);
        btn_pay_coupon.setOnClickListener(this);

        rg_paymethod1 = findViewById(R.id.rg_pay_paymethod1);
        rg_paymethod1.clearCheck();
        rg_paymethod1.setOnCheckedChangeListener(radioListener2);
        rg_paymethod2 = findViewById(R.id.rg_pay_paymethod2);
        rg_paymethod2.clearCheck();
        rg_paymethod2.setOnCheckedChangeListener(radioListener3);

        for (int i = 0; i < BaseActivity.orderdetaillist.size(); ++i) {
            if (BaseActivity.orderdetaillist.get(i).getAmount() > 0) {
                goodslist.add(BaseActivity.goodslist.get(i));
                orderdetaillist.add(BaseActivity.orderdetaillist.get(i));
            }
        }
        recyclerView = findViewById(R.id.rcv_pay);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new PayAdapter(this, orderdetaillist, goodslist));

        for (int i = 0; i < BaseActivity.orderdetaillist.size(); ++i) {
            goods_price += BaseActivity.orderdetaillist.get(i).getPrice();
        }
        tv_price = findViewById(R.id.tv_pay_orderprice);
        tv_price.setText(printPrice(getString(R.string.oneprice_msg), goods_price));

        tv_discount = findViewById(R.id.tv_pay_discount);

        tv_total = findViewById(R.id.tv_pay_totalprice);
        tv_total.setText(tv_price.getText());

        ll_pay_agree_total = findViewById(R.id.ll_agree_total);
        ll_pay_agree_total.setOnClickListener(this);
        cb_pay_agree_total = findViewById(R.id.cb_agree_total);
        cb_pay_agree_total.setOnClickListener(this);
        tv_pay_agree_total = findViewById(R.id.tv_agree_total);
        tv_pay_agree_total.setOnClickListener(this);

        rcv_pay_agree = findViewById(R.id.rcv_pay_agree);
        rcv_pay_agree.setLayoutManager(new LinearLayoutManager(this));
        rcv_pay_agree.setNestedScrollingEnabled(false);
        ArrayList<String> agreelist = new ArrayList<>();
        for (int i = 0; i < getResources().getStringArray(R.array.agreetitle_arr).length; ++i) {
            agreelist.add(getResources().getStringArray(R.array.agreetitle_arr)[i]);
        }
        payAgreeAdapter = new PayAgreeAdapter(this, agreelist, new OnItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                switch (v.getId()) {
                    case R.id.cb_agree:
                        agreeitem(position);
                        break;
                    case R.id.tv_agree_detail:
                        agreeitem(position);
                        break;
                    default:
                        Toast.makeText(PayActivity.this, getString(R.string.prepare_service), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        rcv_pay_agree.setAdapter(payAgreeAdapter);

        btn_pay = findViewById(R.id.btn_pay_pay);
        btn_pay.setOnClickListener(this);

        getFirebaseInstanceID();
    }


    @Override
    public void onBackPressed() {
        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("주문 취소")
                .setMessage("정말로 주문을 취소하시겠습니까?")
                .setPositiveButton("종료", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        finish();
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create().show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            android.app.AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("주문 취소")
                    .setMessage("정말로 주문을 취소하시겠습니까?")
                    .setPositiveButton("종료", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    })
                    .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).create().show();
        }
        return super.onOptionsItemSelected(item);
    }

    private RadioGroup.OnCheckedChangeListener radioListener1 = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            Log.i(TAG, "radiolistener1 : " + i);
            switch (i) {
                case 1:
                    iswrapping = false;
                    ll_pay_table.setAlpha(1f);
                    et_pay_table.setEnabled(false);
                    break;
                case 2:
                    iswrapping = true;
                    ll_pay_table.setAlpha(.1f);
                    et_pay_table.setEnabled(true);
                    break;
            }
        }
    };

    private RadioGroup.OnCheckedChangeListener radioListener2 = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            if(i != -1) {
                rg_paymethod2.setOnCheckedChangeListener(null);
                rg_paymethod2.clearCheck();
                rg_paymethod2.setOnCheckedChangeListener(radioListener3);

                Log.i(TAG, "radioListener2 : " + i);
                switch (i) {
                    case 3:
                        method = Method.CARD;
                        break;
                    case 4:
                        method = Method.PHONE;
                        break;
                }
            }
        }
    };

    private RadioGroup.OnCheckedChangeListener radioListener3 = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            if(i != -1) {
                rg_paymethod1.setOnCheckedChangeListener(null);
                rg_paymethod1.clearCheck();
                rg_paymethod1.setOnCheckedChangeListener(radioListener2);

                Log.i(TAG, "radioListener3 : " + i);
                switch (i) {
                    case 5:
                        method = Method.BANK;
                        break;
                    case 6:
                        method = Method.VBANK;
                        break;
                }
            }
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_pay_pay:
                callPGModule();
                break;
            case R.id.btn_pay_coupon:
                Toast.makeText(this, getString(R.string.prepare_service), Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_agree_total:
                agreeAll();
                break;
            case R.id.cb_agree_total:
                agreeAll();
                break;
            case R.id.tv_agree_total:
                agreeAll();
                break;
        }
    }

    private void agreeAll() {
        boolean tempbool = payAgreeAdapter.checkAgree.get(0);
        for (int i = 0; i < payAgreeAdapter.getDatalist().size(); ++i) {
            if (tempbool != payAgreeAdapter.checkAgree.get(i)) {
                for (int j = 0; j < payAgreeAdapter.checkAgree.size(); ++j) {
                    payAgreeAdapter.checkAgree.set(j, true);
                }
                break;
            }

            if (i == payAgreeAdapter.checkAgree.size() - 1)
                for (int j = 0; j < payAgreeAdapter.checkAgree.size(); ++j) {
                    payAgreeAdapter.checkAgree.set(j, !tempbool);
                }
        }
        payAgreeAdapter.notifyDataSetChanged();
    }

    private void agreeitem(int position) {
        payAgreeAdapter.checkAgree.set(position, !payAgreeAdapter.checkAgree.get(position));
        payAgreeAdapter.notifyDataSetChanged();

        for (int i = 0; i < payAgreeAdapter.checkAgree.size(); ++i) {
            if (!payAgreeAdapter.checkAgree.get(i)) {
                cb_pay_agree_total.setChecked(false);
                break;
            }

            if (i == payAgreeAdapter.checkAgree.size() - 1)
                cb_pay_agree_total.setChecked(true);
        }
    }

    private void callPGModule() {
        if (method == null) {
            Toast.makeText(this, "결제 수단을 선택해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!cb_pay_agree_total.isChecked()) {
            Toast.makeText(this, "약관동의를 수락해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 결제호출
        BootUser bootUser = new BootUser().setPhone("010-6780-5875");
        BootExtra bootExtra = new BootExtra().setQuotas(new int[]{0, 2, 3});

        Bootpay.init(getFragmentManager())
                .setApplicationId("5dcd57125ade160029ff099b") // 해당 프로젝트(안드로이드)의 application id 값
                .setPG(PG.KCP) // 결제할 PG 사
                .setMethod(method) // 결제수단
                .setContext(getApplicationContext())
                .setBootUser(bootUser)
                .setBootExtra(bootExtra)
                .setUX(UX.PG_DIALOG)
                .setName(BaseActivity.storeInfo.getName()) // 결제할 상품명
                .setOrderId("1234") // 결제 고유번호expire_month
                .setPrice(goods_price) // 결제할 금액
                .onConfirm(new ConfirmListener() { // 결제가 진행되기 바로 직전 호출되는 함수로, 주로 재고처리 등의 로직이 수행
                    @Override
                    public void onConfirm(@Nullable String message) {
                        if (0 < 10)
                            Bootpay.confirm(message); // 재고가 있을 경우.
                        else
                            Bootpay.removePaymentWindow(); // 재고가 없어 중간에 결제창을 닫고 싶을 경우

                        Log.d(TAG, "stock -1 : " + message);
                    }
                })
                .onDone(new DoneListener() { // 결제완료시 호출, 아이템 지급 등 데이터 동기화 로직을 수행합니다
                    @Override
                    public void onDone(@Nullable String message) {
                        Log.d(TAG, "onDone : " + message);
                        progressON("Loading...");

                        String receipt_id = "";
                        int total_price = 0;
                        StringBuffer stringBuffer = new StringBuffer();
                        try {
                            JSONObject jsonObject = new JSONObject(message);
                            receipt_id = jsonObject.getString("receipt_id");
                            total_price = jsonObject.getInt("price");
                        } catch (JSONException e) {
                            Log.i(TAG, "Error : JSON / " + e.getMessage());
                            e.printStackTrace();
                        }

                        if (receipt_id == null || total_price == 0) {
                            return;
                        }
                        insert_orderinfo(receipt_id, total_price);
                    }
                })
                .onReady(new ReadyListener() { // 가상계좌 입금 계좌번호가 발급되면 호출되는 함수입니다.
                    @Override
                    public void onReady(@Nullable String message) {
                        Log.d(TAG, "onReady : " + message);
                    }
                })
                .onCancel(new CancelListener() { // 결제 취소시 호출
                    @Override
                    public void onCancel(@Nullable String message) {
                        Toast.makeText(PayActivity.this, "결제가 취소되었습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onCancel : " + message);
                    }
                })
                .onError(new ErrorListener() { // 에러가 났을때 호출되는 부분
                    @Override
                    public void onError(@Nullable String message) {
                        Toast.makeText(PayActivity.this, "결제가 취소되었습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onError : " + message);
                    }
                })
                .onClose(
                        new CloseListener() { //결제창이 닫힐때 실행되는 부분
                            @Override
                            public void onClose(String message) {
                                Log.d(TAG, "close");
                            }
                        })
                .request();
    }

    private Map<String, String> makeMapOrderInfo(String receipt_id, String paymethod, int total_price, int discount, boolean iswrapping) {
        HashMap<String, String> map = new HashMap<>();
        map.put(getString(R.string.store_id), Integer.toString(BaseActivity.storeInfo.getId()));
        map.put(getString(R.string.member_id), Integer.toString(BaseActivity.memberInfo.getId()));
        map.put(getString(R.string.receipt_id), receipt_id);
        map.put(getString(R.string.paymethod), paymethod);
        map.put(getString(R.string.total_price), Integer.toString(total_price));
        map.put(getString(R.string.discount), Integer.toString(discount));
        map.put(getString(R.string.wrapping), Boolean.toString(iswrapping));

        return map;
    }

    private void insert_orderinfo(String receipt_id, int total_price) {
        Call<String> call = getDBService().insertOrderInfo(makeMapOrderInfo(receipt_id, method.name(), total_price, discount_price, iswrapping));
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.message().equals("OK")) {
                    String result = response.body();

                    if (result.contains("Error")) {
                        Log.i(TAG, "Error : 1 insert_orderinfo / " + result);
                    } else {
                        Log.i(TAG, "result : " + result);
                        order_id = Integer.parseInt(result);
                        insert_orderdetailinfo(result);
                    }
                } else {
                    Log.i(TAG, "Error : 2 insert_orderinfo / " + response.message());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i(TAG, "Fail : insert_orderinfo / " + t.getMessage());
            }
        });
    }

    private void insert_orderdetailinfo(final String order_id) {
        ArrayList<Integer> goods_idlist = new ArrayList<>();
        ArrayList<Integer> amountlist = new ArrayList<>();
        ArrayList<Integer> pricelist = new ArrayList<>();

        for (int i = 0; i < orderdetaillist.size(); ++i) {
            goods_idlist.add(orderdetaillist.get(i).getGoods_id());
            amountlist.add(orderdetaillist.get(i).getAmount());
            pricelist.add(orderdetaillist.get(i).getPrice());
        }

        Call<String> call = getDBService().insertOrderDetailInfo(Integer.parseInt(order_id), goods_idlist, amountlist, pricelist);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.message().equals("OK")) {
                    String result = response.body();

                    if (result.contains("Error")) {
                        Log.i(TAG, "Error : 1 insertOrderDetailInfo / " + result);
                    } else {
                        //생략 가능
//                        databaseReference.child(Integer.toString(BaseActivity.storeInfo.getId())).push().setValue(token);
                        get_orderdetailinfo(Integer.parseInt(order_id));
                    }
                } else {
                    Log.i(TAG, "Error : 2 insertOrderDetailInfo / " + response.message());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i(TAG, "Fail : insertOrderDetailInfo / " + t.getMessage());
            }
        });
    }

    private void get_orderdetailinfo(final int order_id) {
        Call<OrderDetailInfo> call = getDBService().getOrderDetailInfo(order_id);
        call.enqueue(new Callback<OrderDetailInfo>() {
            @Override
            public void onResponse(Call<OrderDetailInfo> call, Response<OrderDetailInfo> response) {
                if (response.message().equals(getString(R.string.NetworkCheck))) {
                    ArrayList<OrderDetailInfoColumn> orderDetailInfo = response.body().getOrderDetailInfoColumnslist();

                    if (orderDetailInfo.size() == 0) {
                        Toast.makeText(PayActivity.this, "Error 발생", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    progressOFF();
                    databaseReference.child(Integer.toString(BaseActivity.storeInfo.getId()))
                            .push()
                            .setValue(new OrderInfoBoss(order_id, token, orderDetailInfo));

                    getMasterDeviceInfo(BaseActivity.storeInfo.getMaster_id());

                    startActivity(new Intent(PayActivity.this, OrderCompleteActivity.class));
                    finish();
                } else {
                    Log.i(TAG, "Error : 2 getOrderDetailInfo / " + response.message());
                }
            }

            @Override
            public void onFailure(Call<OrderDetailInfo> call, Throwable t) {
                Log.i(TAG, "Fail : insertOrderDetailInfo / " + t.getMessage());
            }
        });
    }

    private void getFirebaseInstanceID() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }
                        // Get new Instance ID token
                        token = task.getResult().getToken();
//						if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)
//							Log.d(TAG, "serial N : "+((TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId());
                    }
                });
    }

    private void getMasterDeviceInfo(int master_id) {
        Log.i(TAG, "master_id : " + master_id);
        Call<MasterDeviceInfo> call = getDBService().getMasterDeviceInfo(master_id);
        call.enqueue(new Callback<MasterDeviceInfo>() {
            @Override
            public void onResponse(Call<MasterDeviceInfo> call, Response<MasterDeviceInfo> response) {
                if (response.message().equals(getString(R.string.NetworkCheck))) {
                    ArrayList<MasterDeviceInfoColumn> list = response.body().getMasterdevicelist();
                    Log.i(TAG, "deviceInfo Size : " + list.size());
                    for (int i = 0; i < list.size(); ++i) {
                        new Thread(new MyThread(list.get(i).getFb_token())).start();
                    }
                } else {
                    Log.i(TAG, "getMasterDeviceInfo Error : " + response.message());
                }
            }

            @Override
            public void onFailure(Call<MasterDeviceInfo> call, Throwable t) {
                Log.i(TAG, "getMasterDeviceInfo Fail : " + t.getMessage());
            }
        });
    }

    private void sendFCM(String master_token) {
        try {
            JSONObject jsonObject = new JSONObject();
            JSONObject notification = new JSONObject();
            notification.put("body", "주문 확인");
            notification.put("title", "주문을 확인 할려면 클릭해주세요.");
            jsonObject.put("notification", notification);
            jsonObject.put("to", master_token);

            URL url = new URL(FCM_MESSAGE_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.addRequestProperty("Authorization", "key=" + SERVER_KEY);
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-type", "application/json");
            OutputStream os = conn.getOutputStream();
            os.write(jsonObject.toString().getBytes("utf-8"));
            os.flush();
            conn.getResponseCode();
            Log.i(TAG, "Pass");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.i(TAG, "SendFCM MalformedURLException Error : " + e.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i(TAG, "SendFCM MalformedURLException Error : " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, "SendFCM MalformedURLException Error : " + e.getMessage());
        }
    }

    class MyThread implements Runnable {
        private String master_token;

        public MyThread(String master_token) {
            this.master_token = master_token;
        }

        @Override
        public void run() {
            sendFCM(master_token);
        }
    }
}
