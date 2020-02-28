package com.sbsj.ordermatsta_client.Activity.QRCode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.sbsj.ordermatsta_client.Activity.BaseActivity;
import com.sbsj.ordermatsta_client.Activity.StoreActivity;
import com.sbsj.ordermatsta_client.Network.RetrofitOrderDB;
import com.sbsj.ordermatsta_client.Network.StoreInfo;
import com.sbsj.ordermatsta_client.R;
import com.sbsj.ordermatsta_client.Util.CustomBtnIcon;
import com.sbsj.ordermatsta_client.Util.OnCustomClickListener;
import com.sbsj.ordermatsta_client.Util.OnItemClickListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity implements DecoratedBarcodeView.TorchListener {

    String[] PERMISSIONS = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    final int PERMISSIONS_REQUEST = 100;

    final String TAG = "QRcodeActivity";
    private Activity activity = this;

    Toolbar toolbar;
    DecoratedBarcodeView decoratedBarcodeView;

    CustomBtnIcon btn_sound;
    CustomBtnIcon btn_qr;
    CustomBtnIcon btn_flash;
    CaptureManager captureManager;

    Boolean isFlashOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.tb_qrcode);
        setSupportActionBar(toolbar);

        //툴바 셋팅
        ActionBar actionBar = getSupportActionBar();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                quit(getString(R.string.permission_msg));
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                quit(getString(R.string.permission_msg));
            } else {
                ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSIONS_REQUEST);
            }
        }

        decoratedBarcodeView = findViewById(R.id.bv_qrcode);
        decoratedBarcodeView.setTorchListener(this);

        try {
            Field scannerAlphaField = decoratedBarcodeView.getViewFinder().getClass().getDeclaredField("SCANNER_ALPHA");
            scannerAlphaField.setAccessible(true);
            scannerAlphaField.set(decoratedBarcodeView.getViewFinder(), new int[1]);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        captureManager = new CaptureManager(this, decoratedBarcodeView);
        captureManager.initializeFromIntent(getIntent(), savedInstanceState);
        captureManager.decode();

        btn_sound = findViewById(R.id.btn_main_sound);
        btn_sound.setOnCustomClickListener(new OnCustomClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity, "준비중인 서비스입니다.", Toast.LENGTH_SHORT).show();
//                if(QRCode.issoundOn) {
//                    btn_sound.setimage(R.drawable.btn_soun_off);
//                    btn_sound.setText(R.string.sound_off);
//                } else {
//                    btn_sound.setimage(R.drawable.btn_soun_on);
//                    btn_sound.setText(R.string.sound_on);
//                }
//                QRCode.issoundOn = !QRCode.issoundOn;
//                QRCode.integrator.setBeepEnabled(QRCode.issoundOn);
//                QRCode.integrator.initiateScan();
            }
        });
        btn_qr = findViewById(R.id.btn_main_qr);
        btn_qr.setOnCustomClickListener(new OnCustomClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, getString(R.string.prepare_service), Toast.LENGTH_SHORT).show();
            }
        });

        btn_flash = findViewById(R.id.btn_main_flash);
        btn_flash.setOnCustomClickListener(new OnCustomClickListener() {
            @Override
            public void onClick(View view) {
                if (isFlashOn) {
                    decoratedBarcodeView.setTorchOff();
                    btn_flash.setimage(R.drawable.btn_flash_on);
                    btn_flash.setText(R.string.flash_on);
                } else {
                    decoratedBarcodeView.setTorchOn();
                    btn_flash.setimage(R.drawable.btn_flash_off);
                    btn_flash.setText(R.string.flash_off);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        captureManager.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        captureManager.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        captureManager.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        captureManager.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 권한 허가
                } else {
                    quit(getString(R.string.permission_msg));
                    // 권한 거부
                    // 사용자가 해당권한을 거부했을때 해주어야 할 동작을 수행합니다.
                }
                return;
        }
    }

    @Override
    public void onTorchOn() {
        isFlashOn = true;
    }

    @Override
    public void onTorchOff() {
        isFlashOn = false;
    }

    private boolean hasFlash() {
        return getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    private void quit(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                finish();
            }
        };
        handler.sendEmptyMessageDelayed(0, 5000);
    }

    @Override
    public void onBackPressed() {
        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("종료")
                .setMessage("정말로 종료하시겠습니까?")
                .setPositiveButton("종료", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.finishAffinity(activity);
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create().show();
    }
}
