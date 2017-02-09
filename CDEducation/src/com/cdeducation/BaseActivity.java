package com.cdeducation;

import com.cdeducation.R;
import com.cdeducation.util.HttpUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

/**
 * @author t77yq @2014-07-25.
 */
public class BaseActivity extends FragmentActivity {
	public static final int REQUET_INTENT_LOGIN = 10001;
	private ProgressDialog pd;
    public Context ct;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ct=BaseActivity.this;
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	public void showDialog() {
		if (pd == null) {
			pd = new ProgressDialog(ct);
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		}
		pd.setMessage(this.getString(R.string.pull_to_refresh_refreshing_label));
		if (!pd.isShowing()) {
			pd.show();
		}
	}

	public void dismissDialog() {
		if (pd != null && pd.isShowing()) {
			pd.dismiss();
		}
	}

	public void showToast(String text) {
		Toast.makeText(ct, text, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 关闭键盘事件
	 * 
	 * @author
	 * @update
	 */
	public void closeInput(View view) {
		if (view != null) {
			InputMethodManager inputMethodManager = (InputMethodManager) view
					.getContext()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			if (inputMethodManager != null) {
				inputMethodManager.hideSoftInputFromWindow(
						view.getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}
	}

	/**
	 * 关闭键盘事件
	 * 
	 * @author
	 * @update
	 */
	public void closeInput() {
		InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		if (inputMethodManager != null && getCurrentFocus() != null) {
			inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		  HttpUtils.newRequestQueue(this).cancelAll(new Object());
	}
	
}
