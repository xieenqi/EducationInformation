package com.cdeducation.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cdeducation.R;
import com.cdeducation.data.NewsItems;
import com.cdeducation.model.NewsItemsModel;

/**
 * @author zl @2014-07-21.
 */
public class NewsItemsAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private static Context context;
	private NewsItemsModel model;

	public NewsItemsAdapter(Context context, NewsItemsModel model) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.model = model;
		this.model.registerDataSetObserver(new DataSetObserver() {
			@Override
			public void onChanged() {
				notifyDataSetChanged();
			}

			@Override
			public void onInvalidated() {
				notifyDataSetInvalidated();
			}
		});
		
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public int getCount() {

		return model.size();
	}

	@Override
	public Object getItem(int i) {

		return model.getNItem(i);
	}

	@Override
	public long getItemId(int i) {
		
		return i;
	}

	public void cleanData(){
		model.clear();
		notifyDataSetChanged();
	}
	@Override
	public View getView(int i, View convertView, ViewGroup viewGroup) {
		HolderView needInfo;
		if (convertView == null||convertView.getTag()==null) {
			convertView = inflater.inflate(R.layout.fragment_newsitem_item, viewGroup, false);
			needInfo = new HolderView();
			needInfo.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
			needInfo.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
			convertView.setTag(needInfo);
		}else{
			needInfo = (HolderView) convertView.getTag();
		}
		NewsItems nItem = (NewsItems) getItem(i);
		needInfo.tv_title.setText(nItem.Title);
		needInfo.tv_date.setText(nItem.GetdateTime);
		
		return convertView; 
	}

	static class HolderView {
		TextView tv_title;
		TextView tv_date;
	}
}
