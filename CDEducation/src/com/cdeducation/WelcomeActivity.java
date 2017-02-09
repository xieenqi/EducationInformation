package com.cdeducation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.cdeducation.R;
import com.cdeducation.config.Config;
import com.libray.util.ViewFinder;
import com.viewpagerindicator.CirclePageIndicator;

/**第一次进入欢迎页面
 */
public class WelcomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        final ViewPager viewPager = ViewFinder.getView(this, R.id.welcome_pager);
        viewPager.setAdapter(new PagerAdapter() {

            private static final int COUNT = 3;

            private final int[] IMAGE_RES = new int[] {
                    R.drawable.welcome_01,
                    R.drawable.welcome_02,
                    R.drawable.welcome_03
            };

            @Override
            public int getCount() {
                return COUNT;
            }

            @Override
            public boolean isViewFromObject(View view, Object o) {
                return view == o;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

            @Override
            public Object instantiateItem(ViewGroup container, final int position) {
                View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.welcome_item, container, false);
                container.addView(view);
                ImageView imageView = ViewFinder.getView(view, R.id.welcomeImage);
                imageView.setImageResource(IMAGE_RES[position]);
                Button buttonOk = ViewFinder.getView(view, R.id.welcomeOkButton);
                if (position == getCount() - 1) {
                    buttonOk.setVisibility(View.VISIBLE);
                    buttonOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            App.defaultSharedPreferencesEditor(getApplication())
                                    .putBoolean(Config.KEY_FIRST_LAUNCH, true).apply();
                            startActivity(new Intent(getApplicationContext(), HomeMainActivity.class));
                            finish();
                        }
                    });
                }
                return view;
            }
        });
        CirclePageIndicator indicator = (CirclePageIndicator)findViewById(R.id.welcomePagerIndicator);
        indicator.setViewPager(viewPager);
    }
}
