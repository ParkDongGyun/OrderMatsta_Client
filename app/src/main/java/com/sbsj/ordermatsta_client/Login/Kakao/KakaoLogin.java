package com.sbsj.ordermatsta_client.Login.Kakao;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import com.kakao.auth.ApiResponseCallback;
import com.kakao.auth.ErrorCode;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.usermgmt.response.UserResponse;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;
import com.sbsj.ordermatsta_client.Activity.BaseActivity;
import com.sbsj.ordermatsta_client.Activity.LoginActivity;
import com.sbsj.ordermatsta_client.R;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

public class KakaoLogin {
    private final String TAG = "KakaoLogin";
    private final String DIRECTORY = "Kakao";

    Context context;
    private SessionCallback callback;

    public KakaoLogin(Context context) {
        this.context = context;

        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);
        //자기 카카오톡 프로필 정보 및 디비정보 쉐어드에 저장해놨던거 불러오기
//        loadShared();
    }

    private class SessionCallback implements ISessionCallback {
        @Override
        public void onSessionOpened() {
            requestMe();

            Log.i(TAG, "onSessionOpened : ");
            //redirectSignupActivity();  // 세션 연결성공 시 redirectSignupActivity() 호출
        }
        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if (exception != null) {
                Logger.e(exception);
            }
            ((LoginActivity)context).setContentView(R.layout.activity_login); // 세션 연결이 실패했을때
            Log.i(TAG, "onSessionOpenFailed : ");
        }
        // 로그인화면을 다시 불러옴
    }

    protected void requestMe() { //유저의 정보를 받아오는 함수
        List<String> keys = new ArrayList<>();

        UserManagement.getInstance().me(new MeV2ResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                super.onFailure(errorResult);
                Log.i(TAG, "onFailure");
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Log.i(TAG, "onSessionClosed");
            }

            @Override
            public void onSuccess(MeV2Response result) {
                Log.i(TAG, "onSuccess");
                ((BaseActivity) context).saveTempLogin(DIRECTORY, result.getKakaoAccount().getDisplayId());
                ((LoginActivity)context).checkLogin(DIRECTORY, result.getKakaoAccount().getDisplayId());
            }
        });
    }


    public void removeSessionCallback() {
        Session.getCurrentSession().removeCallback(callback);
        Log.i(TAG, "removeSessionCallback : ");

    }

    //카카오 디벨로퍼에서 사용할 키값을 로그를 통해 알아낼 수 있다. (로그로 본 키 값을을 카카오 디벨로퍼에 등록해주면 된다.)
    private void getAppKeyHash(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                Log.i("Hash_key", "Hash key : "+something);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.i("Hash_key","Error : "+ e.toString());
        }
    }
}
