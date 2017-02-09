package com.cdeducation;

import android.content.Intent;
import android.os.Bundle;

import com.cdeducation.config.Config;
import com.cdeducation.fragment.SettingFragment;
import com.cdeducation.util.SharedPreferencesTool;

/**
 * @author t77yq @2014-07-24.
 */
public class LauncherActivity extends BaseActivity {
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		boolean initialized = App.defaultSharedPreferences(this).getBoolean(
				Config.KEY_FIRST_LAUNCH, false);
		startActivity(new Intent(this, initialized ? HomeMainActivity.class
				: WelcomeActivity.class));
		if (!initialized) {//第一次安装软件 push打开
			SharedPreferencesTool.setEditor(LauncherActivity.this,
					SettingFragment.pushStatus, !initialized);
		}
		finish();
	}
}
