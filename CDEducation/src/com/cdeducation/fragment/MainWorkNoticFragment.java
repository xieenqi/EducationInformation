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
 * 工作通报
 */
public class MainWorkNoticFragment extends BaseScrFragment {
	LinearLayout ll_patrol_content;
	LinearLayout ll_situation_content;
	public static final int HTTP_PATROL = 10;
	public static final int HTTP_SITUATION = 11;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_work, container, false);
		initView();
		initListener();
		return rootView;

	}

	public void getAllListData() {
		getListData(NewsMenuId.MENUID_SPECIAL_NETWORKS, HTTP_SITUATION);
		getListData(NewsMenuId.MENUID_WEB_PATROL, HTTP_PATROL);
	}

	public void initView() {
		initListView();
		ll_situation_content = ViewFinder.getView(rootView, R.id.ll_situation_content);
		ll_patrol_content = ViewFinder.getView(rootView, R.id.ll_patrol_content);
		ViewFinder.getView(rootView, R.id.ll_situation).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intentNewsList(NewsMenuId.MENUID_SPECIAL_NETWORKS, "教育专网运行情通报");
			}
		});
		ViewFinder.getView(rootView, R.id.ll_patrol).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intentNewsList(NewsMenuId.MENUID_WEB_PATROL, "中小学校网站巡查");
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
	public void fragmentVisibleHint(boolean isVisibleToUser) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void handleHttpResultSucc(int succCode, int what, Object data) {
		// TODO Auto-generated method stub
		onRefreshComplete();
		switch (what) {
		case HTTP_SITUATION:
			if (data != null) {
				List<NewsItems> list = (List<NewsItems>) data;
				initMenuData(list, ll_situation_content,NewsMenuId.MENUID_SPECIAL_NETWORKS);
			}
			break;

		case HTTP_PATROL:
			if (data != null) {
				List<NewsItems> list = (List<NewsItems>) data;
				initMenuData(list, ll_patrol_content,NewsMenuId.MENUID_WEB_PATROL);
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
	public void updateData() {
		// TODO Auto-generated method stub

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
