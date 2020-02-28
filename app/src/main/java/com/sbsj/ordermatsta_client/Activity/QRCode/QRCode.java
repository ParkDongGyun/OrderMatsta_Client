package com.sbsj.ordermatsta_client.Activity.QRCode;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.ViewfinderView;
import com.sbsj.ordermatsta_client.Activity.BaseActivity;
import com.sbsj.ordermatsta_client.Activity.StoreActivity;
import com.sbsj.ordermatsta_client.Network.StoreInfo;
import com.sbsj.ordermatsta_client.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QRCode extends BaseActivity {
    String TAG = "QRResultActivity";
    private final String URL = "http://dongkyun5654.cafe24.com/get_tableinfo.php";
    private final String TAG_TBNAME = "TABLE_NAME";

    static public IntentIntegrator integrator;
    public static boolean issoundOn = true;
    @Override
    protected void onResume() {
        super.onResume();

        integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(MainActivity.class);
        integrator.setBeepEnabled(QRCode.issoundOn);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        Log.d("onActivityResult", "onActivityResult: .");
        if (resultCode == Activity.RESULT_OK) {
            IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
            String result = scanResult.getContents();

            if(result != null) {
                Log.i(TAG, "result1 : " + result);
                String id = result.split(getString(R.string.defend_word))[0];

                Log.i(TAG, "result2 : " + id);
                Call<StoreInfo> call = getDBService().getStoreInfo(id);
                call.enqueue(new Callback<StoreInfo>() {
                    @Override
                    public void onResponse(Call<StoreInfo> call, Response<StoreInfo> response) {
                        if(response.message().equals(getString(R.string.NetworkCheck))) {
                            Log.i(TAG, "Success : " + response.body());

                            if(response.body().getStorelist().get(0).getId() ==  -1) {
                                Toast.makeText(QRCode.this, "네트워크 오류 발생", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            BaseActivity.storeInfo = response.body().getStorelist().get(0);

                            Intent storeintent = new Intent(getApplicationContext(), StoreActivity.class);
                            startActivity(storeintent);
                        } else {
                            Log.i(TAG, "Fail : " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<StoreInfo> call, Throwable t) {
                        Log.i(TAG, "Error : " + t.getMessage());
                    }
                });
//                Intent nextIntent = new Intent(getApplicationContext(), MenuActivity.class);
//                nextIntent.putExtra(STOREINFO,result);
//                startActivity(nextIntent);
            } else {
                Toast.makeText(this, "QR코드를 다시 한번 확인해주세요!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
