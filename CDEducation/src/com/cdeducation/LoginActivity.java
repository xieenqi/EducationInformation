package com.cdeducation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cdeducation.R;
import com.cdeducation.data.UserInfo;
import com.cdeducation.service.NewsItemFetcher;
import com.cdeducation.util.HttpUtils;
import com.libray.tools.JsonUtil;
import com.libray.util.ViewFinder;

/**
 * @author zl 2014/7/24.
 */
@SuppressLint({ "SetJavaScriptEnabled", "HandlerLeak" })
public class LoginActivity extends BaseActivity {
	boolean isFirst = true;
	// code login
	public static final int WEB_LOGIN_START = 0;
	public static final int WEB_LOGIN_END = WEB_LOGIN_START + 1;
	public static final int LOGIN_SUCCEED = WEB_LOGIN_END + 1;
	public static final int LOGIN_PASSWORD_ERROR = LOGIN_SUCCEED + 1;
	public static final int LOGIN_EXCEPTION = LOGIN_PASSWORD_ERROR + 1;

	public boolean isUpdate = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		TextView textView = ViewFinder.getView(this, R.id.menu_title);
		textView.setText("会员");
		ViewFinder.getView(this, R.id.menu_left).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	public void getUserInfo(String cookieStr) {
		String temp = "JSESSIONID=";
		String tm = "";
		if (!TextUtils.isEmpty(cookieStr)) {
			tm = cookieStr.substring(temp.length());
		}
		final String requestUrl = String.format(NewsItemFetcher.USR_INFO, tm);
		HttpUtils.newRequestQueue(getApplicationContext()).add(new StringRequest(Request.Method.GET, requestUrl, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					UserInfo userInfo = JsonUtil.toBeanNoName(response, UserInfo.class);
					App.saveUserInfo(userInfo);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
			}
		}));
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		setResult(Activity.RESULT_OK);
	}

	@Override
	public void finish() {
		super.finish();
		 overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
	}
}
