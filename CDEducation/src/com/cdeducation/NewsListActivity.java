package com.cdeducation;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cdeducation.R;
import com.cdeducation.fragment.MainNoticFragment;
import com.libray.util.ViewFinder;

/**列表详情页面
 * @author t77yq @2014-07-18.
 */
public class NewsListActivity extends BaseActivity {
	String title = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_list);
		ViewFinder.getView(this, R.id.menu_left).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		TextView menu_title = ViewFinder.getView(this, R.id.menu_title);
		title = getIntent().getStringExtra(MainNoticFragment.KEY_TITLE);
		menu_title.setText(title);
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
	}
}
