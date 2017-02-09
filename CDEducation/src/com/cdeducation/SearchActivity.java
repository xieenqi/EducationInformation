package com.cdeducation;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cdeducation.R;
import com.libray.util.ViewFinder;

/**搜索页面
 * @Author zl @2014/7/24.
 */
public class SearchActivity extends BaseActivity {

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        
//        final LinearLayout menu_left = ViewFinder.getView(this, R.id.menu_left);
////		final TextView menu_title = ViewFinder.getView(this, R.id.menu_title);
////		menu_title.setText("搜索");
//		menu_left.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				finish();
//			}
//		});
    }
    @Override
    public void finish() {
    	super.finish();
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
    }
}
