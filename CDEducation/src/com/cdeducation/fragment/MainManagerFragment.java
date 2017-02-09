package com.cdeducation.fragment;

import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.cdeducation.R;
import com.cdeducation.data.NewsItems;
import com.cdeducation.service.NewsMenuId;
import com.libray.util.ViewFinder;

/**
 * 装备管理
 * 
 */
public class MainManagerFragment extends BaseScrFragment {
	LinearLayout ll_information_content;
	LinearLayout ll_equipment_content;
	LinearLayout ll_website_content;
	LinearLayout ll_training_content;
	public static final int HTTP_INFORMATION = 10;
	public static final int HTTP_EQUIPMENT = 11;
	public static final int HTTP_WEBSITE = 12;
	public static final int HTTP_TRAINING = 13;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_manager, container, false);
		initView();
		initListener();
		return rootView;

	}

	public void getAllListData() {
		getListData(NewsMenuId.MENUID_INFO_SKILL, HTTP_INFORMATION);
		getListData(NewsMenuId.MENUID_EDUCATION_DEVICE, HTTP_EQUIPMENT);
		getListData(NewsMenuId.MENUID_EDUCATION_WEB, HTTP_WEBSITE);
		getListData(NewsMenuId.MENUID_RESEARCH_TRAIN, HTTP_TRAINING);
	}

	public void initView() {
		initListView();
		ll_information_content = ViewFinder.getView(rootView, R.id.ll_information_content);
		ll_equipment_content = ViewFinder.getView(rootView, R.id.ll_equipment_content);
		ll_website_content = ViewFinder.getView(rootView, R.id.ll_website_content);
		ll_training_content = ViewFinder.getView(rootView, R.id.ll_training_content);
		ViewFinder.getView(rootView, R.id.ll_equipment).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intentNewsList(NewsMenuId.MENUID_EDUCATION_DEVICE, "教仪装备");
			}
		});
		ViewFinder.getView(rootView, R.id.ll_information).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intentNewsList(NewsMenuId.MENUID_INFO_SKILL, "信息技术");
			}
		});
		ViewFinder.getView(rootView, R.id.ll_website).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intentNewsList(NewsMenuId.MENUID_EDUCATION_WEB, "教育网站");
			}
		});
		ViewFinder.getView(rootView, R.id.ll_training).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intentNewsList(NewsMenuId.MENUID_RESEARCH_TRAIN, "研究培训");
			}
		});
		getAllListData();

	}

	public void initListener() {
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void fragmentVisibleHint(boolean isVisibleToUser) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleHttpResultSucc(int succCode, int what, Object data) {
		// TODO Auto-generated method stub
		onRefreshComplete();
		switch (what) {
		case HTTP_EQUIPMENT:
			http_count--;
			if (data != null) {
				List<NewsItems> list = (List<NewsItems>) data;
				initMenuData(list, ll_equipment_content,NewsMenuId.MENUID_EDUCATION_DEVICE);
			}
			break;

		case HTTP_INFORMATION:
			http_count--;
			if (data != null) {
				List<NewsItems> list = (List<NewsItems>) data;
				initMenuData(list, ll_information_content,NewsMenuId.MENUID_INFO_SKILL);
			}
			break;
		case HTTP_TRAINING:
			http_count--;
			if (data != null) {
				List<NewsItems> list = (List<NewsItems>) data;
				initMenuData(list, ll_training_content,NewsMenuId.MENUID_RESEARCH_TRAIN);
			}
			break;
		case HTTP_WEBSITE:
			http_count--;
			if (data != null) {
				List<NewsItems> list = (List<NewsItems>) data;
				initMenuData(list, ll_website_content,NewsMenuId.MENUID_EDUCATION_WEB);
			}
			break;
		}
	}

	@Override
	public void handleHttpResultErr(int errCode, int what, Object data) {
		// TODO Auto-generated method stub
		onRefreshComplete();
	}

	@Override
	protected void loadMoreData() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void loadNewData() {
		// TODO Auto-generated method stub
		getAllListData();
	}
}
