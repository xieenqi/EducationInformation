package com.cdeducation.fragment;

import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.cdeducation.NewsActivity;
import com.cdeducation.R;
import com.cdeducation.adapter.NewsItemsAdapter;
import com.cdeducation.data.NewsItemDomain;
import com.cdeducation.data.NewsItems;
import com.cdeducation.model.NewsItemsModel;
import com.cdeducation.service.NewsItemFetcher;
import com.cdeducation.service.NewsMenuId;
import com.cdeducation.util.HttpUtils;

/**
 * 通知公告
 * 
 * @author zhaolin 2015-2-12
 * @Desc:
 */
public class MainNoticFragment extends BaseListFragment {

	NewsItemsAdapter adapter;
	NewsItemsModel model;
	public static final int HTTP_LOAD_LIST = 12;
	public static final String KEY_MENUID = "menuid";
	public static final String KEY_ISACTIVITY = "isActivity";
	public static final String KEY_TITLE = "title";
	String menuId = NewsMenuId.MENUID_NOTICE_BULLETIN;
	boolean isActivity = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_main_notice, container,
				false);
		menuId = getActivity().getIntent().getStringExtra(KEY_MENUID);
		isActivity = getActivity().getIntent().getBooleanExtra(KEY_ISACTIVITY,
				false);
		if (TextUtils.isEmpty(menuId)) {
			menuId = NewsMenuId.MENUID_NOTICE_BULLETIN;
		}
		initView();
		initListener();
		return rootView;

	}

	public void initView() {
		initListView();
		model = new NewsItemsModel();
		adapter = new NewsItemsAdapter(getActivity(), model);
		actualListView.setAdapter(adapter);
	}

	public void initListener() {
		actualListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				NewsItems newsItems = model.getNItem(position - 1);
				Intent intent = new Intent(getActivity(), NewsActivity.class);
				intent.putExtra(NewsDetialFragment.ARG_DATA, newsItems);
				intent.putExtra(NewsDetialFragment.ARG_FRAGS, 1);
				intent.putExtra(NewsDetialFragment.ARG_MENUID, menuId);
				startActivity(intent);
				getActivity().overridePendingTransition(R.anim.slide_left_in,
						R.anim.slide_left_out);
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		if (isActivity) {
			isActivity = false;
			fragmentVisibleHint(true);
		}
	}

	public void getListData() {
		if (model == null || model.size() == 0)
			setLoading(true, false);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("MenuId", menuId);
		params.put("page", page_curr + "");
		params.put("pageRows", page_rows + "");
		HttpUtils.requestGet(NewsItemFetcher.NEWS_PAGE_LIST, params, this,
				HTTP_LOAD_LIST, NewsItemDomain.class);
	}

	@Override
	public void handleHttpResultSucc(int succCode, int what, Object data) {
		// TODO Auto-generated method stub
		onRefreshComplete();
		switch (what) {
		case HTTP_LOAD_LIST:
			NewsItemDomain domain = (NewsItemDomain) data;
			if (data != null && domain != null) {
				if (model.size() == 0) {
					setLoading(false, true);
				}
				List<NewsItems> nItems = domain.rows;
				if (page_curr == 1) {
					model.createNItems(nItems);
					setModelBoth();
				} else {
					model.setNItem(nItems);
				}
				page_curr++;
			} else {
				setLoading(false, false);
				if (model == null || model.size() == 0)
					setNoData("没有数据");
			}
			if (domain != null && model.size() == domain.total) {
				closePullUpRefresh();
			}
			break;
		}
	}

	@Override
	public void handleHttpResultErr(int errCode, int what, Object data) {
		// TODO Auto-generated method stub
		setLoading(false, false);
		if (model == null || model.size() == 0)
			setNoData("重新加载");
		onRefreshComplete();
	}

	@Override
	public void updateData() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void loadMoreData() {
		// TODO Auto-generated method stub
		getListData();
	}

	@Override
	protected void loadNewData() {
		// TODO Auto-generated method stub
		page_curr = 1;
		getListData();
	}

	@Override
	public void fragmentVisibleHint(boolean isVisibleToUser) {
		// TODO Auto-generated method stub
		if (isVisibleToUser)
			loadNewData();
	}

}
