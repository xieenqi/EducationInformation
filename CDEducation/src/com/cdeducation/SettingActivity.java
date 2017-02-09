package com.cdeducation;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cdeducation.R;
import com.libray.util.ViewFinder;

/**设置页面
 */
public class SettingActivity extends BaseActivity {

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeting);
        final LinearLayout menu_left = ViewFinder.getView(this, R.id.menu_left);
		final TextView menu_title = ViewFinder.getView(this, R.id.menu_title);
		menu_title.setText("设置");
		menu_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
    }
    @Override
    public void finish() {
    	super.finish();
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
    }
}
