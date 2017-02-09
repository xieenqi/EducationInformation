package com.cdeducation.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import com.cdeducation.NewsActivity;
import com.cdeducation.R;
import com.cdeducation.config.Config;
import com.cdeducation.data.NewsPic;
import com.cdeducation.fragment.NewsDetialFragment;
import com.cdeducation.util.HttpUtils;
import com.libray.util.*;

import java.util.List;

/**
 * @author ZL @2014-07-22.
 */
public class BannerViewPagerAdapter extends PagerAdapter {
	Context context;
	List<NewsPic> list;
	LayoutInflater inflate;

	public BannerViewPagerAdapter(Context context) {
		this.context = context;
		inflate = LayoutInflater.from(context);
	}

	public void setData(List<NewsPic> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	public String getTitle(int pos) {
		if (pos < list.size()) {
			return list.get(pos).RecommendTitle;
		}
		return "成都教育装备";
	}

	@Override
	public int getCount() {
		return list != null ? list.size() : 0;
	}

	@Override
	public boolean isViewFromObject(View view, Object o) {
		return view == o;
	}

	@Override
	public Object instantiateItem(final ViewGroup container, final int position) {
		NewsPic item = list.get(position);
		final View v = inflate.inflate(
				R.layout.fragment_placeholder_banner_item, null);
		NetworkImageView imageView = ViewFinder.getView(v,
				R.id.fragment_banner_imageView);
		String url = item.RecommendImg;
		if (TextUtils.isEmpty(url)) {
			url = Config.BASE_URL;
		}
		imageView.setImageUrl(url,
				new ImageLoader(HttpUtils.newRequestQueue(context),
						BitmapLruCache.getInstance()));
		imageView.setImageResource(R.drawable.icon_banner);
		imageView.setTag(item);
		imageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(context, NewsActivity.class);
				intent.putExtra(NewsDetialFragment.ARG_DATA_PIC,
						list.get(position));
				intent.putExtra(NewsDetialFragment.ARG_FRAGS, 1);
				context.startActivity(intent);
			}
		});
		container.addView(v);
		return v;
	}

	@Override
	public void destroyItem(final ViewGroup container, final int position,
			final Object object) {
		container.removeView((View) object);
	}
}
