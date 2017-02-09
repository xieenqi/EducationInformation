package com.cdeducation;

import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.cdeducation.R;
import com.cdeducation.appservice.FileDownloadService;
import com.cdeducation.appservice.MessgeReceiver;
import com.cdeducation.config.Commons;
import com.cdeducation.data.NewsItems;
import com.cdeducation.data.NewsPic;
import com.cdeducation.database.DBManager;
import com.cdeducation.fragment.NewsDetialFragment;
import com.libray.util.ViewFinder;

/**
 * @author t77yq @2014-07-18.
 */
public class NewsActivity extends BaseActivity implements MessgeReceiver.OnDownloadBackListener {

	private boolean saved;
	public CheckBox saveCheck;
	public TextView menu_title;
	Intent downloadIntent;
	MessgeReceiver messgeReceiver;
	NewsDetialFragment fileFragment;

	public void registerBoradcastReceiver() {
		messgeReceiver = new MessgeReceiver();
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction(MessgeReceiver.ACTION_NAME);
		// 注册广播
		registerReceiver(messgeReceiver, myIntentFilter);
	}

	public void unRegisterBoradcastReceiver() {
		// 注销广播
		if (messgeReceiver != null)
			unregisterReceiver(messgeReceiver);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news);
		registerBoradcastReceiver();
		fileFragment = (NewsDetialFragment) getSupportFragmentManager().findFragmentById(R.id.NewsDetialFragment);
		menu_title = ViewFinder.getView(this, R.id.menu_title);
		saveCheck = ViewFinder.getView(this, R.id.menu_save);
		ViewFinder.getView(this, R.id.menu_left).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		final NewsPic pic = (NewsPic) getIntent().getSerializableExtra(NewsDetialFragment.ARG_DATA_PIC);
		if (pic != null) {
			saveCheck.setVisibility(View.GONE);
			if (!TextUtils.isEmpty(pic.RecommendTitle)) {
				menu_title.setText(pic.RecommendTitle);
			} else {
				menu_title.setText("成都教育装备");
			}
		} else {
			final NewsItems data = (NewsItems) getIntent().getSerializableExtra(NewsDetialFragment.ARG_DATA);
			if (data != null) {
				menu_title.setText(data.Title);
			} else {
				String title = getIntent().getStringExtra(NewsDetialFragment.ARG_TITLE);
				if (!TextUtils.isEmpty(title)) {
					menu_title.setText(title);
				} else {
					menu_title.setText("成都教育装备");
				}
			}

			if (data != null) {
				int frags = getIntent().getIntExtra(NewsDetialFragment.ARG_FRAGS, 0);
				if (frags == 1)
					saveCheck.setVisibility(View.VISIBLE);
				saveCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
						Commons.IS_COLLECT_PAGE = true;
						if (checked) {
							if (!saved) {
								saved = true;
								data.MenuId = getIntent().getStringExtra(NewsDetialFragment.ARG_MENUID);
								DBManager dbManager = DBManager.getInstance(NewsActivity.this);
								dbManager.addItem(data);
								dbManager.close();
							}
						} else {
							if (saved) {
								saved = false;
								DBManager dbManager = DBManager.getInstance(NewsActivity.this);
								dbManager.deleteByNo(data.Id);
								dbManager.close();
							}
						}
					}
				});
				DBManager dbManager = DBManager.getInstance(this);
				Cursor cursor = dbManager.queryByNo(data.Id);
				if (cursor != null && cursor.getCount() != 0) {
					saved = true;
					saveCheck.setChecked(true);
				}
				dbManager.close();
			} else {
				String temp = getIntent().getStringExtra("url");
				if (!TextUtils.isEmpty(temp)) {
					saveCheck.setVisibility(View.GONE);
				}
			}
		}
		downloadIntent = new Intent(this, FileDownloadService.class);
		startService(downloadIntent);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (downloadIntent != null)
			stopService(downloadIntent);
		unRegisterBoradcastReceiver();
	}

	@Override
	public void finish() {
		Intent data = new Intent();
		data.putExtra("saved", saved);
		setResult(RESULT_OK, data);
		super.finish();
		overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
	}

	@Override
	public void onDownloadBack(int errCode, Object file) {
		// TODO Auto-generated method stub
		String text = "点击打开(如打不开,应安装浏览器或相应的软件)";
		if (fileFragment != null)
			fileFragment.setClickText(text, true);
	}

	@Override
	public void onDownloadProgress(int precent) {
		// TODO Auto-generated method stub
		String text = "已经下载" + precent + "%";
		if (fileFragment != null)
			fileFragment.setClickText(text, false);
	}
}
