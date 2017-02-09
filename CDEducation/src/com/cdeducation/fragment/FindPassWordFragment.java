package com.cdeducation.fragment;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.cdeducation.BaseActivity;
import com.cdeducation.R;
import com.cdeducation.service.NewsItemFetcher;
import com.cdeducation.util.HttpUtils;
import com.libray.tools.JsonUtil;
import com.libray.util.*;

/**
 * 找回密码
 * 
 * @author zhaolin 2015-2-12
 * @Desc:
 */
public class FindPassWordFragment extends BaseFragment implements View.OnClickListener {
	EditText find_input_email;

	public interface OnSecondaryMenuClosed {

		public void onSecondaryMenuClosed(int index);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_find_pass, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ViewFinder.getView(view, R.id.submit_btn).setOnClickListener(this);
		find_input_email = ViewFinder.getView(view, R.id.find_input_email);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.submit_btn:
			if (logic()) {
				try {
					findPass();
				} catch (JSONException e) {
					e.printStackTrace();
					showToast("找回密码失败");
				}
			}
			break;
		}
	}

	public boolean logic() {
		String email = find_input_email.getText().toString();
		if (TextUtils.isEmpty(email)) {
			showToast("邮箱不能空");
			return false;
		}
		if (!Utils.isVaildEmail(email)) {
			showToast("邮箱格式不对");
			return false;
		}
		return true;
	}

	public void findPass() throws JSONException {
		String email = find_input_email.getText().toString();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("email", email);
		showDialog();
		HttpUtils.newRequestJsonObjectPost(getActivity(), NewsItemFetcher.USR_FINDPASS, jsonObject, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				dismissDialog();
				try {
					int result = JsonUtil.toBeanInt(response.toString());
					if (result == 0) {
						showToast("密码邮件已经发送到指定邮箱");
					} else if (result == 1) {
						showToast("该邮箱不存在");
					} else {
						showToast("找回密码失败");
					}
				} catch (Exception e) {
					e.printStackTrace();
					showToast("找回密码失败");
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
					showToast("找回密码失败");
					dismissDialog();
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// if (resultCode != Activity.RESULT_OK)
		// {
		// return;
		// }
		if (requestCode == BaseActivity.REQUET_INTENT_LOGIN) {
		}
	}

	@Override
	public void updateData() {
		// TODO Auto-generated method stub
		
	}
}
