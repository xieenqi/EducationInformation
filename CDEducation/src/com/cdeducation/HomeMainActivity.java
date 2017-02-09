package com.cdeducation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

import cn.jpush.android.api.JPushInterface;

import com.cdeducation.R;
import com.cdeducation.adapter.MainPagerAdapter;
import com.cdeducation.appservice.AppDownloadService;
import com.cdeducation.fragment.MainFragment;
import com.cdeducation.fragment.MainManagerFragment;
import com.cdeducation.fragment.MainNoticFragment;
import com.cdeducation.fragment.MainTrendsFragment;
import com.cdeducation.fragment.MainWorkNoticFragment;
import com.cdeducation.fragment.NewsDetialFragment;
import com.cdeducation.fragment.SettingFragment;
import com.cdeducation.util.SharedPreferencesTool;
import com.libray.util.ViewFinder;

/**
 * 主页面
 * 
 * @author zl @2014-07-20.
 */

public class HomeMainActivity extends BaseActivity {

	private MainPagerAdapter sectionsPagerAdapter;
	long exitTime = 0;

	OnRightDataChangeListener onRightDataChangeListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		initViewPager();
	}

	public void initView() {
		final ImageView menu_left = ViewFinder.getView(this, R.id.menu_left);
		final ImageView menu_right = ViewFinder.getView(this, R.id.menu_right);
		menu_left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(new Intent(ct, SearchActivity.class));
			}
		});
		menu_right.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(new Intent(ct, SettingActivity.class));
			}
		});
	}

	private ViewPager initViewPager() {
		sectionsPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
		sectionsPagerAdapter.setData(new MainFragment());
		sectionsPagerAdapter.setData(new MainNoticFragment());
		sectionsPagerAdapter.setData(new MainTrendsFragment());
		sectionsPagerAdapter.setData(new MainManagerFragment());
		sectionsPagerAdapter.setData(new MainWorkNoticFragment());

		RadioGroup rg_tab = ViewFinder.getView(this, R.id.rg_tab);
		final RadioButton rb_one = ViewFinder.getView(this, R.id.rb_one);
		final RadioButton rb_two = ViewFinder.getView(this, R.id.rb_two);
		final RadioButton rb_three = ViewFinder.getView(this, R.id.rb_three);
		final RadioButton rb_four = ViewFinder.getView(this, R.id.rb_four);
		final RadioButton rb_five = ViewFinder.getView(this, R.id.rb_five);
		final ViewPager viewPager = ViewFinder.getView(this, R.id.pager);
		viewPager.setAdapter(sectionsPagerAdapter);
		viewPager.setOffscreenPageLimit(sectionsPagerAdapter.getCount());
		viewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						switch (position) {
						case 0:
							rb_one.setChecked(true);
							break;
						case 1:
							rb_two.setChecked(true);
							break;
						case 2:
							rb_three.setChecked(true);
							break;
						case 3:
							rb_four.setChecked(true);
							break;
						case 4:
							rb_five.setChecked(true);
							break;
						}
					}
				});

		rg_tab.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.rb_one:
					viewPager.setCurrentItem(0, true);
					break;
				case R.id.rb_two:
					viewPager.setCurrentItem(1, true);
					break;
				case R.id.rb_three:
					viewPager.setCurrentItem(2, true);
					break;
				case R.id.rb_four:
					viewPager.setCurrentItem(3, true);
					break;
				case R.id.rb_five:
					viewPager.setCurrentItem(4, true);
					break;

				}

			}
		});
		return viewPager;
	}

	public interface OnRightDataChangeListener {
		public void onLoginDataChange();
	}

	@Override
	protected void onStart() {
		super.onStart();
		intentAct();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		intentAct();
	}

	public void intentAct() {
		if (App.notiflyNews != null) {
			Intent intent1 = new Intent(this, NewsActivity.class);
			intent1.putExtra(NewsDetialFragment.ARG_DATA, App.notiflyNews);
			intent1.putExtra(NewsDetialFragment.ARG_FRAGS, 1);
			startActivity(intent1);
			overridePendingTransition(R.anim.slide_left_in,
					R.anim.slide_left_out);
			App.notiflyNews = null;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (SharedPreferencesTool.getSharedPreferences(ct,
				SettingFragment.pushStatus, false)) {
			if (JPushInterface.isPushStopped(ct)) {
				JPushInterface.resumePush(ct);
			}
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (SharedPreferencesTool.getSharedPreferences(ct,
				SettingFragment.pushStatus, false)) {
			if (JPushInterface.isPushStopped(ct)) {
				JPushInterface.resumePush(ct);
			}
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// if (resultCode != Activity.RESULT_OK)
		// {
		// return;
		// }
		if (requestCode == HomeMainActivity.REQUET_INTENT_LOGIN) {
			if (onRightDataChangeListener != null) {
				onRightDataChangeListener.onLoginDataChange();
			}
		}
	}

	public void registerChangeListener(
			OnRightDataChangeListener onRightDataChangeListener) {
		this.onRightDataChangeListener = onRightDataChangeListener;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& AppDownloadService.download.size() > 0) {
			Toast.makeText(this, "更新进行中，请勿退出！", 0).show();
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(this, "再按一次退出程序!", Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				finish();
				// System.exit(0);
				super.onKeyDown(keyCode, event);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
