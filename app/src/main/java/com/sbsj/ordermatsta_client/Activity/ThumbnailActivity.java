package com.sbsj.ordermatsta_client.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.sbsj.ordermatsta_client.Activity.QRCode.QRCode;
import com.sbsj.ordermatsta_client.Network.MemberInfo;
import com.sbsj.ordermatsta_client.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ThumbnailActivity extends BaseActivity {

    private ImageView imgAndroid;
    private Animation anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thumbnail);

        initView();
        startLoading();


    }

    private void initView() {
        imgAndroid = (ImageView) findViewById(R.id.start_logo);
        anim = AnimationUtils.loadAnimation(this, R.anim.thumbnail_logo);
        imgAndroid.setAnimation(anim);

    }

    private void startLoading() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (loadShared()) {
                    if (BaseActivity.b_dir.isEmpty() || BaseActivity.b_sns.isEmpty()) {
                        Intent intent = new Intent(ThumbnailActivity.this, LoginActivity.class);
                        startActivity(intent);
                        return;
                    }

                    Call<MemberInfo> call = getDBService().getMemberInfo(BaseActivity.b_dir, BaseActivity.b_sns);
                    call.enqueue(new Callback<MemberInfo>() {
                        @Override
                        public void onResponse(Call<MemberInfo> call, Response<MemberInfo> response) {
                            BaseActivity.memberInfo = response.body().getMemberlist().get(0);
                            startActivity(new Intent(ThumbnailActivity.this, QRCode.class));
                            finish();
                        }

                        @Override
                        public void onFailure(Call<MemberInfo> call, Throwable t) {

                        }
                    });
                } else {
                    Intent intent = new Intent(ThumbnailActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        }, 2000);
    }
}
