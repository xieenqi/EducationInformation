package com.cdeducation.fragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.cdeducation.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.libray.util.ViewFinder;

public abstract class BaseListFragment extends BaseNewFragment implements PullToRefreshBase.OnRefreshListener2<ListView> {
	public static int page_curr = 1;
	public static int page_rows = 20;
	PullToRefreshListView mPullRefreshListView;
	ListView actualListView;
	View emptyView;
	View loading_dialog;
	TextView no_data;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.activity_fragment_list, container, false);
		return rootView;
	}

	public void initListView() {
		mPullRefreshListView = ViewFinder.getView(rootView, R.id.listView_pull);
		actualListView = mPullRefreshListView.getRefreshableView();
		mPullRefreshListView.setOnRefreshListener(this);
		actualListView.setFadingEdgeLength(0);
		actualListView.setCacheColorHint(0);
		actualListView.setDividerHeight(0);
		actualListView.setDivider(null);
		mPullRefreshListView.setVisibility(View.VISIBLE);
		emptyView = ViewFinder.getView(rootView, R.id.fragment_contentList_nodata);
		loading_dialog = ViewFinder.getView(rootView, R.id.loading_dialog);
		no_data = ViewFinder.getView(rootView, R.id.no_data);
		no_data.setVisibility(View.GONE);
		// actualListView.setEmptyView(emptyView);
	}

	/**
	 * 数据加载进度框
	 * 
	 * @param isLoading
	 */
	public void setLoading(boolean isLoading, boolean isData) {
		if (no_data != null)
			no_data.setVisibility(View.GONE);
		if (isData) {
			if (mPullRefreshListView != null) {
				mPullRefreshListView.setVisibility(View.VISIBLE);
			}
			emptyView.setVisibility(View.GONE);
		} else {
			if (mPullRefreshListView != null) {
				mPullRefreshListView.setVisibility(View.GONE);
			}
			emptyView.setVisibility(View.VISIBLE);
		}
		if (isLoading) {
			if (loading_dialog != null) {
				loading_dialog.setVisibility(View.VISIBLE);
			}
		} else {
			if (loading_dialog != null) {
				loading_dialog.setVisibility(View.GONE);
			}
		}
	}

	/**
	 * 设置没有数据提示
	 * 
	 * @param isData
	 */
	public void setNoData(String text) {
		if (loading_dialog != null) {
			loading_dialog.setVisibility(View.GONE);
		}
		if (mPullRefreshListView != null) {
			mPullRefreshListView.setVisibility(View.GONE);
		}
		if (no_data != null) {
			no_data.setText(text);
			no_data.setVisibility(View.VISIBLE);
			no_data.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					setLoading(true, false);
					loadNewData();
				}
			});
		}
	}

	// 打开下拉上拉
	protected void setModelBoth() {
		// 刷新结束的时候
		if (mPullRefreshListView != null) {
			mPullRefreshListView.setMode(Mode.BOTH);
		}
	}

	// 取消下拉上拉
	protected void setModelDisabled() {
		// 刷新结束的时候
		if (mPullRefreshListView != null) {
			mPullRefreshListView.setMode(Mode.DISABLED);
		}
	}

	// 下拉取消
	protected void closePullDownRefresh() {
		// 刷新结束的时候
		if (mPullRefreshListView != null)
			mPullRefreshListView.setMode(Mode.PULL_FROM_END);
	}

	// 上拉取消
	protected void closePullUpRefresh() {
		// 刷新结束的时候
		if (mPullRefreshListView != null)
			mPullRefreshListView.setMode(Mode.PULL_FROM_START);

	}

	public void onRefreshComplete() {
		if (mPullRefreshListView != null)
			mPullRefreshListView.onRefreshComplete();
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
	public void handleHttpResultSucc(int succCode, int what, Object data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleHttpResultErr(int errCode, int what, Object data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> listViewPullToRefreshBase) {
		loadNewData();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> listViewPullToRefreshBase) {
		loadMoreData();
	}
}
