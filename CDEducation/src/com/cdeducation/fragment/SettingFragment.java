package com.cdeducation.fragment;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import cn.jpush.android.api.JPushInterface;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.cdeducation.AboutActivity;
import com.cdeducation.App;
import com.cdeducation.BaseActivity;
import com.cdeducation.FavoriteNewsActivity;
import com.cdeducation.R;
import com.cdeducation.config.Commons;
import com.cdeducation.data.UpdateApp;
import com.cdeducation.service.NewsItemFetcher;
import com.cdeducation.util.ApkUpdate;
import com.cdeducation.util.HttpUtils;
import com.cdeducation.util.SharedPreferencesTool;
import com.libray.tools.JsonUtil;

/**
 * 设置界面
 * 
 * @Desc:
 */
public class SettingFragment extends BaseNewFragment implements View.OnClickListener {

	public static final String pushStatus = "PUSH_STATUS";

	public interface OnSecondaryMenuClosed {

		public void onSecondaryMenuClosed(int index);
	}

	CheckBox cb_push;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_setting, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		view.findViewById(R.id.ll_about).setOnClickListener(this);
		view.findViewById(R.id.ll_collection).setOnClickListener(this);
		cb_push = (CheckBox) view.findViewById(R.id.cb_push);
		cb_push.setChecked(SharedPreferencesTool.getSharedPreferences(getActivity(), pushStatus, false));
		cb_push.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				SharedPreferencesTool.setEditor(getActivity(), pushStatus, isChecked);
				if (isChecked) {
					JPushInterface.resumePush(getActivity());
				} else {
					JPushInterface.stopPush(getActivity());
				}
			}
		});
	}

	public void setUI() {
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.ll_collection:
			startActivityForResult(new Intent(getActivity(), FavoriteNewsActivity.class), REQUET_INTENT_REGISTER);
			getActivity().overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
			break;
		case R.id.ll_about:
			startActivity(new Intent(getActivity(), AboutActivity.class));
			getActivity().overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
			break;
		}
	}

	public void updateApk() throws JSONException {
		JSONObject jsonRequest = new JSONObject();
		jsonRequest.put("os", "android");
		showDialog();
		HttpUtils.newRequestJsonObjectPost(getActivity(), NewsItemFetcher.VERSION, jsonRequest, new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				// TODO Auto-generated method stub
				dismissDialog();
				try {
					UpdateApp updateApp = JsonUtil.toBeanNoNameO(response.toString(), UpdateApp.class);
					if (updateApp != null) {
						if (TextUtils.isEmpty(updateApp.content)) {
							updateApp.content = "你有新版需要更新,抓紧更新哦";
						}
						App.setUpdateApp(updateApp);
						ApkUpdate apkUpdate = new ApkUpdate(getActivity());
						apkUpdate.UpdateApk(updateApp);
					}
				} catch (Exception e) {
					e.printStackTrace();
					showToast("服务器异常");
				}
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				dismissDialog();
				showToast("服务器异常");
			}
		});
	}

	@Override
	public void updateData() {
		// TODO Auto-generated method stub
		setUI();
	}

	@Override
	public void onResume() {
		super.onResume();
		updateData();
		if (Commons.IS_SETTING_PAGE) {
			Commons.IS_SETTING_PAGE = false;
			if (App.getInstance().isLogin()) {
				getUserInfo();
			}
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == BaseActivity.REQUET_INTENT_LOGIN) {
		}
	}

	@Override
	public void handleHttpResultSucc(int succCode, int what, Object data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleHttpResultErr(int errCode, int what, Object data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void fragmentVisibleHint(boolean isVisibleToUser) {
		// TODO Auto-generated method stub

	}
}
