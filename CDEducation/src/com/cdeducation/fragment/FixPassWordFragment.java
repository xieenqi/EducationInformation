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
import com.cdeducation.App;
import com.cdeducation.BaseActivity;
import com.cdeducation.R;
import com.cdeducation.service.NewsItemFetcher;
import com.cdeducation.util.HttpUtils;
import com.libray.tools.JsonUtil;
import com.libray.util.ViewFinder;

/**
 * 修改密码
 * 
 * @author zhaolin 2015-2-12
 * @Desc:
 */
public class FixPassWordFragment extends BaseFragment implements View.OnClickListener {

	public interface OnSecondaryMenuClosed {

		public void onSecondaryMenuClosed(int index);
	}

	EditText fix_input_pass_again;
	EditText fix_input_pass_new;
	EditText fix_input_pass;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_fix_pass, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ViewFinder.getView(view, R.id.submit_btn).setOnClickListener(this);
		fix_input_pass_again = ViewFinder.getView(view, R.id.fix_input_pass_again);
		fix_input_pass_new = ViewFinder.getView(view, R.id.fix_input_pass_new);
		fix_input_pass = ViewFinder.getView(view, R.id.fix_input_pass);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.submit_btn:
			if (logic()) {
				try {
					fixPass();
				} catch (JSONException e) {
					e.printStackTrace();
					showToast("修改失败");
				}
			}
			break;
		}
	}

	public boolean logic() {
		String pass = fix_input_pass.getText().toString();
		String newPass = fix_input_pass_new.getText().toString();
		String reNewPass = fix_input_pass_again.getText().toString();
		if (TextUtils.isEmpty(pass)) {
			showToast("旧密码不能为空");
			return false;
		}
		if (pass.length() < 6 || pass.length() > 20) {
			showToast("请输入6-20数字或者字符");
			return false;
		}
		if (TextUtils.isEmpty(newPass)) {
			showToast("新密码不能空");
			return false;
		}
		if (newPass.length() < 6 || newPass.length() > 20) {
			showToast("请输入6-20数字或者字符");
			return false;
		}
		if (TextUtils.isEmpty(reNewPass)) {
			showToast("新密码不能空");
			return false;
		}
		if (reNewPass.length() < 6 || reNewPass.length() > 20) {
			showToast("请输入6-20数字或者字符");
			return false;
		}
		if (reNewPass.length() != newPass.length() || !newPass.equals(reNewPass)) {
			showToast("两次密码不一致");
		}
		return true;
	}

	public void fixPass() throws JSONException {
		String email = App.getInstance().getSPEmail();
		String pass = fix_input_pass.getText().toString();
		final String reNewPass = fix_input_pass_again.getText().toString();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("email", email);
		jsonObject.put("rpwd", pass);
		jsonObject.put("npwd", reNewPass);
		showDialog();
		HttpUtils.newRequestJsonObjectPost(getActivity(), NewsItemFetcher.USR_REPASS, jsonObject, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				dismissDialog();
				try {
					int result = JsonUtil.toBeanInt(response.toString());
					if (result == 0) {
						showToast("修改成功");
						App.setSPPass(reNewPass);
						getActivity().finish();
					}else
					if (result == 1) {
						showToast("原始密码错误");
					} else {
						showToast("修改失败");
					}
				} catch (Exception e) {
					e.printStackTrace();
					showToast("修改失败");
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				dismissDialog();
				showToast("修改失败");
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
