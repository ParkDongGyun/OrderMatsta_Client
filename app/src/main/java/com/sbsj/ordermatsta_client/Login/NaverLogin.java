package com.sbsj.ordermatsta_client.Login;

import android.content.Context;
import android.util.Log;

import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;
import com.sbsj.ordermatsta_client.Activity.BaseActivity;
import com.sbsj.ordermatsta_client.Activity.LoginActivity;
import com.sbsj.ordermatsta_client.R;

import org.json.JSONException;
import org.json.JSONObject;

public class NaverLogin {
    private final String TAG = "NaverLogin";
    private final String DIRECTORY = "Naver";
    // ->네이버
    private String OAUTH_CLIENT_NAME = "네이버 아이디로 로그인";

    //private OAuthLoginButton mOAuthLoginButton;
    private OAuthLogin mOAuthLoginInstance;
    private String id;
    private Context mContext;

    //생성자
    public NaverLogin(Context context, OAuthLoginButton btnNaver) {
        // ->네이버 초기화
        mOAuthLoginInstance = OAuthLogin.getInstance();
        mOAuthLoginInstance.showDevelopersLog(true);
        mOAuthLoginInstance.init(context, context.getString(R.string.naverLoginId), context.getString(R.string.naverLoginSecret), OAUTH_CLIENT_NAME);
        // ->네이버 로그인 버튼 셋팅
        /**/
        //mOAuthLoginButton = findViewById(R.id.buttonOAuthLoginImg);
        btnNaver.setOAuthLoginHandler(mOAuthLoginHandler);
        this.mContext = context;
    }


    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
    }*/


    private OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
        @Override
        public void run(boolean success) {
            if (success) {
                Thread thread = new Thread() {
                    public void run() {
                        String accessToken = mOAuthLoginInstance.getAccessToken(mContext);
                        String data = mOAuthLoginInstance.requestApi(mContext, accessToken, "https://openapi.naver.com/v1/nid/me");
                        try {
                            JSONObject result = new JSONObject(data);
                            id = result.getJSONObject("response").getString("id");
                            Log.i(TAG,id);
                        } catch (JSONException e) {
                            Log.i(TAG, "error : "+e.toString());
                        }
                    }
                };
                thread.start();
                try {
                    thread.join();
                    Log.i(TAG, "Success");
                    ((BaseActivity) mContext).saveTempLogin(DIRECTORY, id);
                    ((LoginActivity)mContext).checkLogin(DIRECTORY, id);
                } catch (InterruptedException e) {
                    Log.i(TAG, "error : "+e.toString());
                }
            }
        }

    };
}
