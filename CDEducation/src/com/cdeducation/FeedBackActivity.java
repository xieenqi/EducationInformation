package com.cdeducation;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.cdeducation.R;
import com.cdeducation.fragment.FeedBackFragment;
import com.cdeducation.service.NewsItemFetcher;
import com.cdeducation.util.HttpUtils;
import com.libray.tools.JsonUtil;
import com.libray.util.ViewFinder;

/**
 * @author t77yq @2014-07-18.
 */
public class FeedBackActivity extends BaseActivity {
	FeedBackFragment fragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feed_back);
		fragment = (FeedBackFragment) getSupportFragmentManager().findFragmentById(R.id.feedBackFragment);
		ViewFinder.getView(this, R.id.menu_left).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		TextView textView = ViewFinder.getView(this, R.id.menu_title);
		textView.setText("意见反馈");
		TextView menu_right_text = ViewFinder.getView(this, R.id.menu_right_text);
		menu_right_text.setVisibility(View.VISIBLE);
		menu_right_text.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					feedback();
				} catch (JSONException e) {
					e.printStackTrace();
					showToast("反馈失败");
				}
			}
		});

	}

	public void feedback() throws JSONException {
		String text = fragment.getContent();
		if (TextUtils.isEmpty(text)) {
			showToast("你还没有输入反馈意见");
			return;
		}
		showDialog();
		JSONObject jSONObject = new JSONObject();
		jSONObject.put("center", text);
		HttpUtils.newRequestJsonObjectPost(ct, NewsItemFetcher.GUESTBOOK, jSONObject, new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				dismissDialog();
				try {
					int result = JsonUtil.toBeanInt(response.toString());
					if (result == 0) {
						finish();
						showToast("反馈成功");
					} else {
						showToast("反馈失败");
						App.setLogin(false);
					}

				} catch (Exception e) {
					e.printStackTrace();
					showToast("反馈失败");
					App.setLogin(false);
				}
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				dismissDialog();
				showToast("反馈失败");
			}
		});
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
	}
}
