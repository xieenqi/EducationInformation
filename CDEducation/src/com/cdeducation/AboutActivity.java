package com.cdeducation;

import com.cdeducation.R;
import com.libray.util.ViewFinder;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**关于我们页面
 * @author t77yq @2014-07-18.
 */
public class AboutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ViewFinder.getView(this, R.id.menu_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView textView = ViewFinder.getView(this, R.id.menu_title);
		textView.setText("关于我们");
    }

    @Override
    public void finish() {
    	super.finish();
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
    }
}
