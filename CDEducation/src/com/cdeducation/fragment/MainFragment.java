package com.cdeducation.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cdeducation.App;
import com.cdeducation.NewsActivity;
import com.cdeducation.R;
import com.cdeducation.adapter.BannerViewPagerAdapter;
import com.cdeducation.adapter.NewsItemsAdapter;
import com.cdeducation.data.NewsItems;
import com.cdeducation.data.NewsPic;
import com.cdeducation.model.NewsItemsModel;
import com.cdeducation.service.NewsItemFetcher;
import com.cdeducation.service.NewsMenuId;
import com.cdeducation.util.HttpUtils;
import com.libray.view.AutoScrollViewPager;
import com.libray.view.GalleryScroll;

import java.util.HashMap;
import java.util.List;

/**
 * zhaolin 门户首页
 */
public class MainFragment extends BaseListFragment {
	public static int screen_width = 0;
	public static int bannerHeight = 0;
	private AutoScrollViewPager viewPager;
	private ImageView defImage;
	View headerView;

	RelativeLayout bannerLayout;
	View tv_center_intro;
	View tv_center_lead;
	View tv_center_branch;
	View tv_system_build;
	View tv_party_affairs;
	View tv_union_activity;
	TextView tv_name;
	TextView tv_count;
	BannerViewPagerAdapter bannerViewPagerAdapter;
	NewsItemsAdapter newsItemsAdapter;
	NewsItemsModel model;

	public static final int HTTP_LOAD_AD = 11;
	public static final int HTTP_LOAD_LIST = 12;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_main, container, false);
		screen_width = App.getScreenWidth(getActivity());
		bannerHeight = screen_width / 10 * 5 - 10;
		initView();
		initHeader(inflater);
		initBannerView(inflater);
		getAdData();
		getListData();
		return rootView;
	}

	private void initView() {
		initListView();
		setModelDisabled();
		model = new NewsItemsModel();
		newsItemsAdapter = new NewsItemsAdapter(getActivity(), model);
		actualListView.setAdapter(newsItemsAdapter);
		actualListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				NewsItems newsItems = model.getNItem(position - 2);
				Intent intent = new Intent(getActivity(), NewsActivity.class);
				intent.putExtra(NewsDetialFragment.ARG_DATA, newsItems);
				intent.putExtra(NewsDetialFragment.ARG_FRAGS, 1);
				startActivity(intent);
				getActivity().overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
			}
		});
		setLoading(true, false);
	}

	public void initHeader(LayoutInflater inflater) {
		if (headerView == null) {
			headerView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_main_listview_header, null);
			bannerLayout = (RelativeLayout) headerView.findViewById(R.id.fl_banner_layout);
			tv_center_intro = (View) headerView.findViewById(R.id.tv_center_intro);
			tv_center_lead = (View) headerView.findViewById(R.id.tv_center_lead);
			tv_center_branch = (View) headerView.findViewById(R.id.tv_center_branch);
			tv_system_build = (View) headerView.findViewById(R.id.tv_system_build);
			tv_party_affairs = (View) headerView.findViewById(R.id.tv_party_affairs);
			tv_union_activity = (View) headerView.findViewById(R.id.tv_union_activity);
		}
		actualListView.addHeaderView(headerView);
		tv_center_intro.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					NewsItems newsItems = new NewsItems();
					newsItems.Id = Integer.parseInt(NewsMenuId.MENUID_CENTER_INTRO);
					newsItems.Title = "中心简介";
					Intent intent = new Intent(getActivity(), NewsActivity.class);
					intent.putExtra(NewsDetialFragment.ARG_DATA, newsItems);
					intent.putExtra(NewsDetialFragment.ARG_FRAGS, 0);
					startActivity(intent);
					getActivity().overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
				} catch (Exception e) {
					// TODO: handle exception
				}

			}
		});
		tv_center_lead.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					NewsItems newsItems = new NewsItems();
					newsItems.Id = Integer.parseInt(NewsMenuId.MENUID_CENTER_LEAD);
					newsItems.Title = "中心领导";
					Intent intent = new Intent(getActivity(), NewsActivity.class);
					intent.putExtra(NewsDetialFragment.ARG_DATA, newsItems);
					intent.putExtra(NewsDetialFragment.ARG_FRAGS, 0);
					startActivity(intent);
					getActivity().overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
		tv_center_branch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					NewsItems newsItems = new NewsItems();
					newsItems.Id = Integer.parseInt(NewsMenuId.MENUID_BRANCH_SET);
					newsItems.Title = "部门设置";
					Intent intent = new Intent(getActivity(), NewsActivity.class);
					intent.putExtra(NewsDetialFragment.ARG_DATA, newsItems);
					intent.putExtra(NewsDetialFragment.ARG_FRAGS, 0);
					startActivity(intent);
					getActivity().overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
		tv_system_build.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intentNewsList(NewsMenuId.MENUID_SYSTEM_BUILD, "制度建设");
			}
		});
		tv_party_affairs.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intentNewsList(NewsMenuId.MENUID_PARTY_AFFAIRS, "党务公开");
			}
		});
		tv_union_activity.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intentNewsList(NewsMenuId.MENUID_UNION_ACTIVITY, "工会活动");
			}
		});
	}

	/**
	 * 初始化广告数据
	 * 
	 * @param inflater
	 * @param container
	 */
	private void initBannerView(LayoutInflater inflater) {
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(screen_width, bannerHeight);
		View viewB = inflater.inflate(R.layout.fragment_main_banner, null);

		defImage = (ImageView) viewB.findViewById(R.id.fragment_banner_defimage);
		viewPager = (AutoScrollViewPager) viewB.findViewById(R.id.fragment_banner_ViewPager);
		viewPager.setInterval(5000);
		tv_name = (TextView) viewB.findViewById(R.id.tv_name);
		tv_count = (TextView) viewB.findViewById(R.id.tv_count);
		bannerViewPagerAdapter = new BannerViewPagerAdapter(getActivity());
		viewPager.setAdapter(bannerViewPagerAdapter);
		viewPager.setOnPageChangeListener(onPageChangeListener);
		bannerLayout.removeAllViews();
		bannerLayout.setLayoutParams(layoutParams);
		bannerLayout.setGravity(Gravity.CENTER_HORIZONTAL);
		bannerLayout.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);
		bannerLayout.addView(viewB);
	}

	public void setBannerData(List<NewsPic> list) {
		defImage.setVisibility(View.GONE);
		if (list == null || list.size() <= 0) {
			defImage.setVisibility(View.VISIBLE);
		} else {
			tv_count.setText("1/" + list.size());
			tv_name.setText(list.get(0).RecommendTitle);
			if (bannerViewPagerAdapter != null) {
				bannerViewPagerAdapter.setData(list);
			}
		}
	}

	public void setListData(List<NewsItems> nItems) {
		if (model != null)
			model.setNItem(nItems);
	}

	/**
	 * 广告滑动监听事件
	 */
	private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {

		@Override
		public void onPageScrolled(int i, float v, int i2) {

		}

		@Override
		public void onPageSelected(int i) {
			tv_count.setText((i+1)+"/" + bannerViewPagerAdapter.getCount());
			tv_name.setText(bannerViewPagerAdapter.getTitle(i));
		}

		@Override
		public void onPageScrollStateChanged(int i) {

		}
	};

	private View.OnTouchListener forbidenScroll() {
		return new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_MOVE) {
					return true;
				}
				return false;
			}
		};
	}

	@Override
	public void fragmentVisibleHint(boolean isVisibleToUser) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPause() {
		super.onPause();
		viewPager.stopAutoScroll();
	}

	@Override
	public void onResume() {
		super.onResume();
		viewPager.startAutoScroll();
	}

	/**
	 * 广告列表
	 */
	public void getAdData() {
		http_count++;
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("TopNum", "20");
		HttpUtils.requestGet(NewsItemFetcher.NEWS_AD_PIC, params, this, HTTP_LOAD_AD, NewsPic.getType());
	}

	/**
	 * 底部列表数据
	 */
	public void getListData() {
		http_count++;
		HashMap<String, String> params = new HashMap<String, String>();
		//params.put("MenuId", NewsMenuId.MENUID_NEWS_TRENDS);
		params.put("TopNum", "4");
		params.put("IsRecommend", "1");
		HttpUtils.requestGet(NewsItemFetcher.NEWS_INFO_LIST, params, this, HTTP_LOAD_LIST, NewsItems.getType());
	}

	boolean noData = false;

	@Override
	public void handleHttpResultSucc(int succCode, int what, Object data) {
		// TODO Auto-generated method stub
		switch (what) {
		case HTTP_LOAD_AD:
			if (http_count > 0) {
				http_count--;
			}
			if (data != null) {
				List<NewsPic> nItems = (List<NewsPic>) data;
				setBannerData(nItems);
			} else {
				noData = true;
			}
			break;
		case HTTP_LOAD_LIST:
			if (http_count > 0) {
				http_count--;
			}
			if (data != null) {
				List<NewsItems> nItems = (List<NewsItems>) data;
				setListData(nItems);
			} else {
				noData = true;
			}
			break;
		}
		if (http_count <= 0) {

			setLoading(false, !noData);
		}
		if (noData) {
			setNoData("没有数据");
		}
	}

	@Override
	public void handleHttpResultErr(int errCode, int what, Object data) {
		// TODO Auto-generated method stub
		http_count = 0;
		setLoading(false, false);
		setNoData("重新加载");
	}

	@Override
	protected void loadMoreData() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void loadNewData() {
		// TODO Auto-generated method stub
		getAdData();
		getListData();
	}
}
