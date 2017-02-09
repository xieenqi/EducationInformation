package com.cdeducation;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.cdeducation.R;
import com.cdeducation.data.AreaInfo;
import com.cdeducation.fragment.SelectAddressFragment;
import com.cdeducation.model.AreaManager;
import com.libray.util.ViewFinder;

/**
 * @author zl 2014/7/24.
 */
@SuppressLint({ "SetJavaScriptEnabled", "HandlerLeak" })
public class SelectAddressActivity extends BaseActivity {
	WebView webView;
	boolean isFirst = true;
	// code login
	public static final int WEB_LOGIN_START = 0;
	public static final int WEB_LOGIN_END = WEB_LOGIN_START + 1;
	public static final int LOGIN_SUCCEED = WEB_LOGIN_END + 1;
	public static final int LOGIN_PASSWORD_ERROR = LOGIN_SUCCEED + 1;
	public static final int LOGIN_EXCEPTION = LOGIN_PASSWORD_ERROR + 1;

	public boolean isUpdate = false;
	SelectAddressFragment pFragment = null;
	SelectAddressFragment cFragment = null;
	SelectAddressFragment sFragment = null;
	int page_now = 0;
	final int page_p = 0;
	final int page_c = 1;
	final int page_s = 2;
	AreaManager areaManager = null;
    String address="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_address);
		TextView textView = ViewFinder.getView(this, R.id.menu_title);
		textView.setText("地址选择");
		ViewFinder.getView(this, R.id.menu_left).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						back();
					}
				});
		initProvince();
	}
    /**
     * 省
     */
	public void initProvince() {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		pFragment = new SelectAddressFragment();
		pFragment
				.setOnItemListener(new SelectAddressFragment.OnItemListener() {
					@Override
					public void onItemClick(AreaInfo areaInfo) {
						// TODO Auto-generated method stub
						if (areaInfo != null){
							initCity(areaInfo.id);
							address=areaInfo.name+" ";
						}
							
					}
				});
		pFragment.initProvinceList();
		ft.replace(R.id.container, pFragment);
		ft.commit();
		page_now = page_p;
	}
    /**
     * 城市
     * @param provinceCode
     */
	public void initCity(int provinceCode) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		cFragment = new SelectAddressFragment();
		cFragment
				.setOnItemListener(new SelectAddressFragment.OnItemListener() {
					@Override
					public void onItemClick(AreaInfo areaInfo) {
						// TODO Auto-generated method stub
						if (areaInfo != null){
							initDistrict(areaInfo.id);
							address=address+areaInfo.name+" ";
						}
							
					}
				});
		cFragment.initCityList(provinceCode);
		ft.replace(R.id.container, cFragment);
		ft.commit();
		page_now = page_c;
	}
    /**
     * 县
     * @param cityCode
     */
	public void initDistrict(int cityCode) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		sFragment = new SelectAddressFragment();
		sFragment.initDistrictList(cityCode);
		sFragment
				.setOnItemListener(new SelectAddressFragment.OnItemListener() {
					@Override
					public void onItemClick(AreaInfo areaInfo) {
						// TODO Auto-generated method stub
						if (areaInfo != null) {
							address =address+areaInfo.name+" ";
							Intent intent=getIntent();
							intent.putExtra("data", address);
							setResult(RESULT_OK, intent);
							finish();
						}
					}
				});
		ft.replace(R.id.container, sFragment);
		ft.commit();
		page_now = page_s;
	}

	public void back() {
		switch (page_now) {
		case page_p:
			finish();
			break;
		case page_c:
			if (pFragment != null) {
				getSupportFragmentManager().beginTransaction()
						.replace(R.id.container, pFragment).commit();
			}
			page_now=page_p;
			break;
		case page_s:
			if (cFragment != null) {
				getSupportFragmentManager().beginTransaction()
						.replace(R.id.container, cFragment).commit();
			}
			page_now=page_c;
			break;
		}
	}

	@Override
	public void onBackPressed() {
		back();
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
	}
}
