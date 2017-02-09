package com.cdeducation.config;

import java.io.File;

import android.annotation.SuppressLint;
import android.os.Environment;

import com.cdeducation.App;

public class Commons {
	public final static String cacheMainPath = getSDPath() + File.separator + App.getInstance().getPackageName();

	@SuppressLint("SdCardPath")
	public static String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
		} else {
			return "/sdcard";
		}
		return sdDir.toString();
	}

	public final static String PHOTOCACHEPIC = cacheMainPath + "/Image";

	public final static String XUTILS_CACHE = cacheMainPath + "/XutilsCache";

	public final static String CACHEAPK = cacheMainPath + "/UpdateApk";
	public final static String DOWNLOAD = cacheMainPath + "/download";
	// 是否刷新收藏界面
	public static boolean IS_COLLECT_PAGE = false;
	// 是否刷新设置界面
	public static boolean IS_SETTING_PAGE = false;
	// 是否设置消息头中的Authorization
	public static boolean IS_SET_AUTHORIZATION = false;
}
