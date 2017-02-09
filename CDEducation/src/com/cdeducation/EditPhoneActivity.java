package com.cdeducation;


import com.cdeducation.R;
import com.libray.util.ViewFinder;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


/**
 * @author zl 2014/7/24.
 */
public class EditPhoneActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_phone);
		TextView textView = ViewFinder.getView(this, R.id.menu_title);
		textView.setText("编辑手机号");
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
