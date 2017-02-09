package com.cdeducation.fragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.widget.ScrollView;

import com.cdeducation.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.libray.util.ViewFinder;

public abstract class BaseScrFragment extends BaseNewFragment implements PullToRefreshBase.OnRefreshListener2<ScrollView> {
	public static int page_curr = 1;
	public static int page_rows = 20;
	PullToRefreshScrollView mPullRefreshScrollView;

	public void initListView() {
		mPullRefreshScrollView = ViewFinder.getView(rootView, R.id.pulltorefreshscrollview);
		mPullRefreshScrollView.setOnRefreshListener(this);
	}

	public void onRefreshComplete() {
		if (mPullRefreshScrollView != null)
			mPullRefreshScrollView.onRefreshComplete();
	}

	@Override
	public void updateData() {
		// TODO Auto-generated method stub

	}

	// 加载更多
	protected abstract void loadMoreData();

	// 上拉刷新
	protected abstract void loadNewData();

	protected String getTime() {
		return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(new Date());
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ScrollView> listViewPullToRefreshBase) {
		loadNewData();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ScrollView> listViewPullToRefreshBase) {
		loadMoreData();
	}
}
