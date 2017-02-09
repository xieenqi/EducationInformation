package com.cdeducation;

import com.cdeducation.R;
import com.libray.util.ViewFinder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;


/**
 * @author zl 2014/7/24.
 */
@SuppressLint({ "SetJavaScriptEnabled", "HandlerLeak" })
public class FindPassActivity extends BaseActivity {
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
		setContentView(R.layout.activity_find_pass);
		TextView textView = ViewFinder.getView(this, R.id.menu_title);
		textView.setText("找回密码");
		ViewFinder.getView(this, R.id.menu_left).setOnClickListener(
				new View.OnClickListener() {
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
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
	}
}
