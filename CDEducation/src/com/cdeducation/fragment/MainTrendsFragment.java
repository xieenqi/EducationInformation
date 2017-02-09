package com.cdeducation.fragment;

import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.cdeducation.R;
import com.cdeducation.data.NewsItems;
import com.cdeducation.service.NewsMenuId;
import com.libray.util.ViewFinder;

/**
 * 新闻动态
 * 
 * @Desc:
 */
public class MainTrendsFragment extends BaseScrFragment {
	LinearLayout ll_news_trends_content;
	LinearLayout ll_provincial_info_content;
	public static final int HTTP_NEWS_TRENDS = 10;
	public static final int HTTP_PROVINCIAL_INFO = 11;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_new, container, false);
		initView();
		initListener();
		return rootView;

	}

	public void initView() {
		initListView();
		ll_news_trends_content = ViewFinder.getView(rootView, R.id.ll_news_trends_content);
		ll_provincial_info_content = ViewFinder.getView(rootView, R.id.ll_provincial_info_content);
		ViewFinder.getView(rootView, R.id.ll_news_trends).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intentNewsList(NewsMenuId.MENUID_NEWS_TRENDS, "工作动态");
			}
		});
		ViewFinder.getView(rootView, R.id.ll_provincial_info).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intentNewsList(NewsMenuId.MENUID_PROVINCE_INFO, "部省级信息");
			}
		});
		getAllListData();
	}

	public void getAllListData() {
		getListData(NewsMenuId.MENUID_NEWS_TRENDS, HTTP_NEWS_TRENDS);
		getListData(NewsMenuId.MENUID_PROVINCE_INFO, HTTP_PROVINCIAL_INFO);
	}

	@Override
	public void fragmentVisibleHint(boolean isVisibleToUser) {
		// TODO Auto-generated method stub

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
	public void updateData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleHttpResultSucc(int succCode, int what, Object data) {
		// TODO Auto-generated method stub
		onRefreshComplete();
		switch (what) {
		case HTTP_NEWS_TRENDS:
			if (data != null) {
				List<NewsItems> list = (List<NewsItems>) data;
				initMenuData(list, ll_news_trends_content,NewsMenuId.MENUID_NEWS_TRENDS);
			}
			break;

		case HTTP_PROVINCIAL_INFO:
			if (data != null) {
				List<NewsItems> list = (List<NewsItems>) data;
				initMenuData(list, ll_provincial_info_content,NewsMenuId.MENUID_PROVINCE_INFO);
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
