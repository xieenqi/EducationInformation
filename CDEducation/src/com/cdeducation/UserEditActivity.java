package com.cdeducation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.cdeducation.R;
import com.cdeducation.fragment.UserEditFragment;
import com.libray.util.ImageUtils;
import com.libray.util.ViewFinder;

/**
 * @author zl 2014/7/24.
 */
@SuppressLint({ "SetJavaScriptEnabled", "HandlerLeak" })
public class UserEditActivity extends BaseActivity {
	WebView webView;
	UserEditFragment userEditFragment;
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
		setContentView(R.layout.activity_user_edit);
		userEditFragment = (UserEditFragment) getSupportFragmentManager()
				.findFragmentById(R.id.userEditFragment);
		TextView textView = ViewFinder.getView(this, R.id.menu_title);
		textView.setText("账号编辑");
		ViewFinder.getView(this, R.id.menu_left).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != Activity.RESULT_OK) {
			return;
		}
		switch (requestCode) {
		case ImageUtils.PHOTO_CODE:
			if (data != null && data.getData() != null) {
				Uri url = data.getData();
				ImageUtils.cropImage(UserEditActivity.this,url);
			}
			break;
		case ImageUtils.CUT_CODE:
			if (userEditFragment != null)
				userEditFragment.uploadImage(ImageUtils.cropImageUri);
			break;
		case ImageUtils.CAMERA_CODE:
			if (ImageUtils.imageUriFromCamera != null) {
				ImageUtils.cropImage(UserEditActivity.this,ImageUtils.imageUriFromCamera);
			}
			break;
		}
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
