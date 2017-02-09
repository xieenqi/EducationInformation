package com.cdeducation.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.cdeducation.App;
import com.cdeducation.BaseActivity;
import com.cdeducation.R;
import com.libray.util.ViewFinder;

/**
 * 編輯用戶名
 * 
 * @author zhaolin 2015-2-12
 * @Desc:
 */
public class EditNameFragment extends BaseFragment implements View.OnClickListener {
	EditText input_name;
	public interface OnSecondaryMenuClosed {

		public void onSecondaryMenuClosed(int index);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_edit_name, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ViewFinder.getView(view, R.id.submit_btn).setOnClickListener(this);
		input_name = ViewFinder.getView(view, R.id.input_name);
		String name=App.getInstance().getSPNames();
		if(!TextUtils.isEmpty(name)){
			input_name.setText(name);
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.submit_btn:
			String name = input_name.getText().toString();
			String spname=App.getInstance().getSPNames();
			if (!TextUtils.isEmpty(name)) {
				if(!name.equals(spname)){
					upload("names", name);
				}else{
					showToast("没有任何修改");
				}
				
			}else{
				showToast("请填写用户名");
			}
			break;
		}
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
