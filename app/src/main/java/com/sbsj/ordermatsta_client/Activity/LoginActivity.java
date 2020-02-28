package com.sbsj.ordermatsta_client.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.kakao.auth.Session;
import com.kakao.usermgmt.LoginButton;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;
import com.sbsj.ordermatsta_client.Login.GoogleLogin;
import com.sbsj.ordermatsta_client.Login.Kakao.KakaoLogin;
import com.sbsj.ordermatsta_client.Login.NaverLogin;
import com.sbsj.ordermatsta_client.Activity.QRCode.QRCode;
import com.sbsj.ordermatsta_client.Network.MemberInfo;
import com.sbsj.ordermatsta_client.Network.MemberInfoColumn;
import com.sbsj.ordermatsta_client.R;
import com.sbsj.ordermatsta_client.Util.CustomBtnLogin;
import com.sbsj.ordermatsta_client.Util.OnCustomClickListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity implements OnCustomClickListener {

	private final String TAG = "Login_Activity";

	private final int RC_GOOGLE_LOGIN = 10;

	GoogleLogin googleLogin;
	KakaoLogin kakaoLogin;
	NaverLogin naverLogin;

	CustomBtnLogin cbtn_google;
	CustomBtnLogin cbtn_naver;
	CustomBtnLogin cbtn_kakao;

	SignInButton btn_google;
	OAuthLoginButton btn_naver;
	LoginButton btn_kakao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		SignInButton btnGoogle = findViewById(R.id.btn_google);
		googleLogin = new GoogleLogin(this, btnGoogle);
		cbtn_google = findViewById(R.id.btn_google_custom);
		cbtn_google.setOnCustomClickListener(this);

		OAuthLoginButton btnNaver = findViewById(R.id.btn_naver);
		naverLogin = new NaverLogin(this, btnNaver);
		cbtn_naver = findViewById(R.id.btn_naver_custom);
		cbtn_naver.setOnCustomClickListener(this);

		kakaoLogin = new KakaoLogin(this);
		btn_kakao = findViewById(R.id.btn_kakao);
		cbtn_kakao = findViewById(R.id.btn_kakao_custom);
		cbtn_kakao.setOnCustomClickListener(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		kakaoLogin.removeSessionCallback();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
			Log.i("KakaoLogin", "what should I do");
			return;
		}
		//구글 로그인 시도
		else if (requestCode == RC_GOOGLE_LOGIN) {
			Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
			try {
				GoogleSignInAccount account = task.getResult(ApiException.class);
				googleLogin.FirebaseAuthWithGoogle(account);
			} catch (ApiException e) {
				Log.i(TAG, "구글 로그인 시도 실패 APIException : " + e.toString());
			}
		} else {
			Toast.makeText(this, "다시 시도해주세요.", Toast.LENGTH_LONG);
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	public void checkLogin(String dir, final String sns_id) {
		Call<MemberInfo> call = getDBService().getMemberInfo(dir, sns_id);
		call.enqueue(new Callback<MemberInfo>() {
			@Override
			public void onResponse(Call<MemberInfo> call, Response<MemberInfo> response) {
				Log.i(TAG, "result : getMemberInfo / " + response.message());

				MemberInfoColumn memberInfo = response.body().getMemberlist().get(0);

				if (memberInfo.getId() == -1) {
					if (memberInfo.getDirectory() != "-1" || memberInfo.getSns_id() != "-1")
						insertMember(memberInfo);
					else
						Log.i(TAG, "문제 발생 : getMemberInfo / ");
				} else {
					BaseActivity.memberInfo = memberInfo;
					redirectQRcodeActivity();
				}
			}

			@Override
			public void onFailure(Call<MemberInfo> call, Throwable t) {
				Log.i(TAG, "error : getMemberInfo / " + t.getMessage());
			}
		});
	}

	public void insertMember(final MemberInfoColumn memberInfoColumn) {
		Call<String> call = getDBService().insertMemberInfo(memberInfoColumn.getDirectory(), memberInfoColumn.getSns_id());
		call.enqueue(new Callback<String>() {
			@Override
			public void onResponse(Call<String> call, Response<String> response) {
				Log.i(TAG, "result : " + response.message());
				String result = response.body();

				Log.i(TAG, response.body());
				if (result.contains("Error")) {
					Toast.makeText(LoginActivity.this, "서버 오류 발생 잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
					redirectLoginActivity();
				} else {
					try {
						BaseActivity.memberInfo = new MemberInfoColumn(Integer.parseInt(result), memberInfoColumn.getDirectory(), memberInfoColumn.getSns_id());
						redirectQRcodeActivity();
					} catch (NumberFormatException e) {
						Log.i(TAG, "Error : insertMember / " + e.getMessage());
					}

				}
			}

			@Override
			public void onFailure(Call<String> call, Throwable t) {
				Log.i(TAG, "error : insertMember / " + t.getMessage());
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_google_custom:
				googleLogin.loginGoogle(RC_GOOGLE_LOGIN);
				break;
			case R.id.btn_naver_custom:
				btn_naver.performClick();
				break;
			case R.id.btn_kakao_custom:
				btn_kakao.performClick();
				break;
		}
	}
	///////////////////////////////////////////////////////////////////////////////////////
	//액티비티 전환 함수
	///////////////////////////////////////////////////////////////////////////////////////
	public void redirectLoginActivity() {
		final Intent intent = new Intent(this, LoginActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivity(intent);
		finish();
	}

	public void redirectQRcodeActivity() {
		startActivity(new Intent(this, QRCode.class));
		finish();
	}
}