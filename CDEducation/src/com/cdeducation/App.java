package com.cdeducation;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.Display;
import android.view.WindowManager;
import cn.jpush.android.api.JPushInterface;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cdeducation.appservice.AppDownloadService;
import com.cdeducation.appservice.ServiceListenerBroadcastReceiver;
import com.cdeducation.data.NewsItems;
import com.cdeducation.data.UpdateApp;
import com.cdeducation.data.UserInfo;
import com.cdeducation.service.NewsItemFetcher;
import com.libray.tools.MD5Tool;
import com.libray.util.FileUtils;

/**
 * @author t77yq @2014-07-22.
 */
public class App extends Application {
	public static NewsItems notiflyNews;
	private int screenWidth;
	private static App application;
	private UserInfo mUserInfo;
	public static Context mContext;

	public synchronized static App getInstance() {
		return application;
	}

	public static Context getContext() {
		return mContext;
	}

	public static int getScreenWidth(Context context) {
		if (context instanceof Activity) {
			context = ((Activity) context).getApplication();
		}
		return ((App) context).screenWidth;
	}

	private int screenHeight;

	public static int getScreenHeight(Context context) {
		if (context instanceof Activity) {
			context = ((Activity) context).getApplication();
		}
		return ((App) context).screenHeight;
	}

	private RequestQueue requestQueue;

	public static RequestQueue newRequestQueue(Context context) {
		return ((App) context).requestQueue;
	}

	public static SharedPreferences defaultSharedPreferences(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context);
	}

	public static SharedPreferences.Editor defaultSharedPreferencesEditor(Context context) {
		return defaultSharedPreferences(context).edit();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate() {
		super.onCreate();
		application = this;
		mContext = getApplicationContext();
		mUserInfo = new UserInfo();
		Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
		screenWidth = display.getWidth();
		screenHeight = display.getHeight();
		requestQueue = Volley.newRequestQueue(this);
		// 初始化下载服务
		Intent downloadIntent = new Intent(this, AppDownloadService.class);
		startService(downloadIntent);
		JPushInterface.setDebugMode(true);
		JPushInterface.init(this);

		IntentFilter filter = new IntentFilter(Intent.ACTION_TIME_TICK);
		ServiceListenerBroadcastReceiver receiver = new ServiceListenerBroadcastReceiver();
		registerReceiver(receiver, filter);
	}

	public UserInfo getmUserInfo() {
		return mUserInfo;
	}

	@SuppressWarnings("unused")
	private void initMenusIfNeed() {
		final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		if (sp.getString("menus", "").isEmpty()) {
			sp.edit().putString("menus", FileUtils.readFileFromAssets(this, "menus.json")).apply();
		}
		requestQueue.add(new StringRequest(Request.Method.GET, NewsItemFetcher.NEWS_ITEM, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				sp.edit().putString("menus", response).apply();
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				//
			}
		}));
	}

	public static void setUpdateApp(UpdateApp updateApp) {
		if (updateApp != null) {
			App.defaultSharedPreferences(App.getInstance()).edit().putString("version", updateApp.version).putString("downloadurl", updateApp.downloadurl).apply();
		}
	}

	public String getSPVersion() {
		return App.defaultSharedPreferences(App.getInstance()).getString("version", "");
	}

	public String getSPDownloadurl() {
		return App.defaultSharedPreferences(App.getInstance()).getString("downloadurl", "");
	}

	public static void setLogin(boolean isLogin) {
		App.defaultSharedPreferences(App.getInstance()).edit().putBoolean("isLogin", isLogin).apply();
	}

	public static void saveUserInfo(UserInfo userInfo) {
		boolean isLogin = false;
		if (userInfo == null) {
			isLogin = false;
			App.defaultSharedPreferences(App.getInstance()).edit().putBoolean("isLogin", isLogin).apply();
			return;
		} else {
			isLogin = true;
		}
		App.defaultSharedPreferences(App.getInstance()).edit().putString("uid", userInfo.uid).putString("email", userInfo.email).putString("names", userInfo.name).putString("icon", userInfo.icon)
				.putString("sex", userInfo.sex).putString("mobile", userInfo.mobile).putString("area", userInfo.area).apply();
	}

	public static void saveNames(String names) {
		App.defaultSharedPreferences(App.getInstance()).edit().putString("names", names).apply();
	}

	public static void saveMobile(String mobile) {
		App.defaultSharedPreferences(App.getInstance()).edit().putString("mobile", mobile).apply();
	}

	public static void saveIcon(String icon) {
		App.defaultSharedPreferences(App.getInstance()).edit().putString("icon", icon).apply();
	}

	public static void saveArea(String area) {
		App.defaultSharedPreferences(App.getInstance()).edit().putString("area", area).apply();
	}

	public static void saveUid(String uid) {
		App.defaultSharedPreferences(App.getInstance()).edit().putString("uid", uid).apply();
	}

	public static void saveSex(String sex) {
		App.defaultSharedPreferences(App.getInstance()).edit().putString("sex", sex).apply();
	}

	public static void setSPEmail(String email) {
		if (!TextUtils.isEmpty(email))
			App.defaultSharedPreferences(App.getInstance()).edit().putString("email", email).apply();
	}

	public static void setSPPass(String pass) {
		if (!TextUtils.isEmpty(pass))
			App.defaultSharedPreferences(App.getInstance()).edit().putString("pass", MD5Tool.getMD5(pass)).apply();
	}

	public String getSPUid() {
		final SharedPreferences sp = App.defaultSharedPreferences(App.getInstance());
		return sp.getString("id", "");
	}

	public String getSPEmail() {
		return App.defaultSharedPreferences(App.getInstance()).getString("email", "");
	}

	public String getSPPass() {
		return App.defaultSharedPreferences(App.getInstance()).getString("pass", "");
	}

	public String getSPIcon() {
		return App.defaultSharedPreferences(App.getInstance()).getString("icon", "");
	}

	public String getSPNames() {
		return App.defaultSharedPreferences(App.getInstance()).getString("names", "");
	}

	public String getSPSex() {
		return App.defaultSharedPreferences(App.getInstance()).getString("sex", "");
	}

	public String getSPMobile() {
		return App.defaultSharedPreferences(App.getInstance()).getString("mobile", "");
	}

	public String getSPArea() {
		return App.defaultSharedPreferences(App.getInstance()).getString("area", "");
	}

	public boolean isLogin() {
		return App.defaultSharedPreferences(App.getInstance()).getBoolean("isLogin", false);
	}

	public String getToken() {
		return App.defaultSharedPreferences(App.getInstance()).getString("token", "");
	}

	public void saveCookie(String cookie) {
		App.defaultSharedPreferences(App.getInstance()).edit().putString("cookie", cookie).apply();
	}

	public String getSPCookie() {
		return App.defaultSharedPreferences(App.getInstance()).getString("cookie", "");
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		System.out.print("结束");
	}

}
