package com.cdeducation;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.cdeducation.R;
import com.cdeducation.data.NewsItems;
import com.cdeducation.database.DBManager;
import com.cdeducation.fragment.NewsDetialFragment;
import com.cdeducation.service.NewsMenuId;
import com.libray.util.*;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import java.util.ArrayList;
import java.util.List;

/**
 * 收藏界面
 * 
 * @author t77yq @2014-07-25.
 */
public class FavoriteNewsActivity extends BaseActivity {

	private static final int DETAILS = 10002;

	private int selected;

	private boolean dataInvalidated;

	private List<NewsItems> data = new ArrayList<NewsItems>();

	private BaseAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favorite_news);
		PullToRefreshListView pullToRefreshListView = ViewFinder.getView(this, R.id.favorite_news);
		TextView textView = ViewFinder.getView(this, R.id.menu_title);
		textView.setText("收藏");
		ViewFinder.getView(this, R.id.menu_left).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		DBManager dbManager = DBManager.getInstance(this);
		Cursor cursor = dbManager.queryAll();
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			do {
				int Id = cursor.getInt(cursor.getColumnIndex("Id"));
				String Title = cursor.getString(cursor.getColumnIndex("Title"));
				String GetdateTime = cursor.getString(cursor.getColumnIndex("GetdateTime"));
				String MenuId = cursor.getString(cursor.getColumnIndex("MenuId"));
				NewsItems newsItems = new NewsItems();
				newsItems.Id = Id;
				newsItems.Title = Title;
				newsItems.GetdateTime = GetdateTime;
				newsItems.MenuId = MenuId;
				data.add(newsItems);
			} while (cursor.moveToNext());
			cursor.close();
		}
		dbManager.close();
		pullToRefreshListView.setAdapter(adapter = new BaseAdapter() {
			@Override
			public int getCount() {
				return data.size();
			}

			@Override
			public NewsItems getItem(int position) {
				return data.get(position);
			}

			@Override
			public long getItemId(int position) {
				return 0;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				if (convertView == null) {
					convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.fragment_newsitem_item, parent, false);
					convertView.setTag(new ViewHolder(convertView));
				}
				ViewHolder viewHolder = (ViewHolder) convertView.getTag();
				NewsItems pptItem = getItem(position);
				viewHolder.tv_title.setText(pptItem.Title);
				viewHolder.tv_date.setText(pptItem.GetdateTime);
				return convertView;
			}

			class ViewHolder {

				TextView tv_title;

				TextView tv_date;

				public ViewHolder(View view) {
					tv_title = ViewFinder.getView(view, R.id.tv_title);
					tv_date = ViewFinder.getView(view, R.id.tv_date);
				}
			}
		});
		TextView emptyView = new TextView(getApplicationContext());
		emptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		emptyView.setText("没有收藏的文章");
		emptyView.setGravity(Gravity.CENTER);
		emptyView.setTextColor(getResources().getColor(android.R.color.black));
		pullToRefreshListView.setEmptyView(emptyView);
		pullToRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				selected = position - 1;
				NewsItems newsItems = data.get(selected);
				Intent intent = new Intent(ct, NewsActivity.class);
				intent.putExtra(NewsDetialFragment.ARG_DATA, data.get(selected));
				intent.putExtra(NewsDetialFragment.ARG_FRAGS, 1);
				intent.putExtra(NewsDetialFragment.ARG_MENUID, newsItems != null ? newsItems.MenuId : "");
				startActivityForResult(intent, DETAILS);
				overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
			}
		});
		pullToRefreshListView.setMode(Mode.DISABLED);
	}

	@Override
	protected void onPostResume() {
		super.onPostResume();
		if (dataInvalidated) {
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case DETAILS:
				if (!data.getBooleanExtra("saved", true)) {
					this.data.remove(selected);
					dataInvalidated = true;
				}
				break;
			}
		}
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
	}
}
