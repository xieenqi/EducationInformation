package com.cdeducation.fragment;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.cdeducation.App;
import com.cdeducation.BaseActivity;
import com.cdeducation.R;
import com.cdeducation.config.Commons;
import com.cdeducation.service.NewsItemFetcher;
import com.cdeducation.util.HttpUtils;
import com.libray.tools.JsonUtil;
import com.libray.util.Utils;
import com.libray.util.ViewFinder;

/**
 * 注册
 * 
 * @author zhaolin 2015-2-12
 * @Desc:
 */
public class RegisterFragment extends BaseFragment implements View.OnClickListener {
	EditText register_input_user;
	EditText register_input_pass;
	EditText register_input_pass_again;

	public interface OnSecondaryMenuClosed {

		public void onSecondaryMenuClosed(int index);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_register, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ViewFinder.getView(view, R.id.register_btn).setOnClickListener(this);
		register_input_user = ViewFinder.getView(view, R.id.register_input_user);
		register_input_pass = ViewFinder.getView(view, R.id.register_input_pass);
		register_input_pass_again = ViewFinder.getView(view, R.id.register_input_pass_again);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.register_btn:
			if (logic()) {
				try {
					register();
				} catch (JSONException e) {
					e.printStackTrace();
					showToast("注册失败");
				}
			}
			break;
		}
	}

	public boolean logic() {
		String email = register_input_user.getText().toString();
		String pass = register_input_pass.getText().toString();
		String repass = register_input_pass_again.getText().toString();
		if (TextUtils.isEmpty(email)) {
			showToast("邮箱不能空");
			return false;
		}
		if (!Utils.isVaildEmail(email)) {
			showToast("邮箱格式不对");
			return false;
		}
		if (TextUtils.isEmpty(pass) || TextUtils.isEmpty(repass)) {
			showToast("密码不能空");
			return false;
		}
		if (pass.length() < 6 || pass.length() > 20 || repass.length() < 6 || repass.length() > 20) {
			showToast("请输入6-20数字或者字符");
			return false;
		}
		if (pass.length() != repass.length()) {
			showToast("两次密码长度不对");
			return false;
		}
		if (!pass.trim().equals(repass.trim())) {
			showToast("两次密码不一致");
			return false;
		}
		return true;
	}

	public void register() throws JSONException {
		final String email = register_input_user.getText().toString();
		final String pass = register_input_pass.getText().toString();
		JSONObject jObject = new JSONObject();
		jObject.put("email", email);
		jObject.put("pwd", pass);
		showDialog();
		HttpUtils.newRequestJsonObjectPost(getActivity(), NewsItemFetcher.USR_REG, jObject, new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				dismissDialog();
				try {
					int result = JsonUtil.toBeanInt(response.toString());
					if (result == 0) {
						App.setLogin(true);
						App.setSPEmail(email);
						App.setSPPass(pass);
						getActivity().setResult(Activity.RESULT_OK);
						getActivity().finish();
						Commons.IS_SETTING_PAGE = true;
						showToast("注册成功");
					} else if (result == 1) {
						showToast("邮箱重复");
					} else {
						showToast("注册失败");
					}
				} catch (Exception e) {
					e.printStackTrace();
					showToast("注册失败");
				}
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				dismissDialog();
				showToast("注册失败");
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == BaseActivity.REQUET_INTENT_LOGIN) {
		}
	}

	@Override
	public void updateData() {
		// TODO Auto-generated method stub

	}
}
