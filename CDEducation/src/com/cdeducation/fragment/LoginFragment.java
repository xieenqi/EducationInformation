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
import com.cdeducation.FindPassActivity;
import com.cdeducation.R;
import com.cdeducation.config.Commons;
import com.cdeducation.service.NewsItemFetcher;
import com.cdeducation.util.HttpUtils;
import com.libray.tools.JsonUtil;
import com.libray.tools.MD5Tool;
import com.libray.util.Utils;
import com.libray.util.ViewFinder;

/**
 * 登录界面
 * 
 * @author zhaolin 2015-2-12
 * @Desc:
 */
public class LoginFragment extends BaseFragment implements View.OnClickListener {
	EditText login_input_pass;
	EditText login_input_user;

	public interface OnSecondaryMenuClosed {

		public void onSecondaryMenuClosed(int index);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_login, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ViewFinder.getView(view, R.id.login_forget_pass).setOnClickListener(this);
		ViewFinder.getView(view, R.id.login_register).setOnClickListener(this);
		ViewFinder.getView(view, R.id.login_btn).setOnClickListener(this);
		login_input_pass = ViewFinder.getView(view, R.id.login_input_pass);
		login_input_user = ViewFinder.getView(view, R.id.login_input_user);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.login_forget_pass:
			startActivityForResult(new Intent(getActivity(), FindPassActivity.class), REQUET_INTENT_FINDPASS);
			getActivity().overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
			break;
		case R.id.login_register:
//			startActivityForResult(new Intent(getActivity(), RegisterActivity.class), REQUET_INTENT_REGISTER);
//			getActivity().overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
			break;
		case R.id.login_btn:
			if (logic()) {
				try {
					login();
				} catch (JSONException e) {
					e.printStackTrace();
					showToast("登陆失败");
				}
			}
			break;
		}
	}

	public boolean logic() {
		String email = login_input_user.getText().toString();
		String pass = login_input_pass.getText().toString();
		if (TextUtils.isEmpty(email)) {
			showToast("邮箱不能空");
			return false;
		}
		if (!Utils.isVaildEmail(email)) {
			showToast("邮箱格式不对");
			return false;
		}
		if (TextUtils.isEmpty(pass)) {
			showToast("密码不能空");
			return false;
		}
		if (pass.length() < 6 || pass.length() > 20) {
			showToast("请输入6-20数字或者字符");
			return false;
		}
		return true;
	}

	public void login() throws JSONException {
		final String email = login_input_user.getText().toString();
		final String pass = login_input_pass.getText().toString();
		JSONObject jSONObject = new JSONObject();
		jSONObject.put("email", email);
		jSONObject.put("pwd", MD5Tool.getMD5(pass));
		showDialog();
		HttpUtils.newRequestJsonObjectPost(getActivity(), NewsItemFetcher.USR_LOGIN, jSONObject, new Response.Listener<JSONObject>() {

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
						getActivity().finish();
						Commons.IS_SETTING_PAGE = true;
						showToast("登陆成功");
					} else if (result == 1) {
						showToast("密码错误");
						App.setLogin(false);
					} else if (result == 2) {
						showToast("参数错误");
						App.setLogin(false);
					} else {
						showToast("登陆失败");
						App.setLogin(false);
					}

				} catch (Exception e) {
					e.printStackTrace();
					showToast("登陆失败");
					App.setLogin(false);
				}
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				dismissDialog();
				showToast("登陆失败");
				App.setLogin(false);
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != Activity.RESULT_OK) {
			return;
		}
		if (requestCode == REQUET_INTENT_REGISTER) {
			getActivity().finish();
			getActivity().setResult(Activity.RESULT_OK);
		}
	}

	@Override
	public void updateData() {
		// TODO Auto-generated method stub

	}
}
