package com.cdeducation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.cdeducation.R;
import com.cdeducation.util.HttpUtils;
import com.libray.util.ViewFinder;

/**
 * @author zl 2014/7/24.
 */
@SuppressLint({ "SetJavaScriptEnabled", "HandlerLeak" })
public class RegisterActivity extends BaseActivity {
	WebView webView;
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
		setContentView(R.layout.activity_register);
		TextView textView = ViewFinder.getView(this, R.id.menu_title);
		textView.setText("注册");
		ViewFinder.getView(this, R.id.menu_left).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		setResult(Activity.RESULT_OK);
	}

	@Override
	protected void onStop() {
		super.onStop();
		HttpUtils.newRequestQueue(this).cancelAll(new Object());
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
	}
}
