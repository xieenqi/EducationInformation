/**
 * @author ChenDu GOGO  Administrator
 *	时间   2012 下午3:04:14
 *  包名：com.gogofood.bean
            工程名：GoGoCity
 */
package com.cdeducation.util;

import java.io.File;

import com.cdeducation.R;
import com.cdeducation.appservice.AppDownloadService;
import com.cdeducation.config.Commons;
import com.cdeducation.data.UpdateApp;
import com.cdeducation.view.DialogHelper;
import com.libray.tools.FileTool;
import com.libray.tools.MD5Tool;
import com.libray.util.AppVersion;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

/**
 * 类名 ApkUpdate.java 时间 2012-7-23 下午3:04:14 描述
 */
public class ApkUpdate {
	/**
	 * 0代表没有更新 1代表有更新 2代表更新出现异常
	 */
	String TAG = "ApkUpdate";
	private Context mContext;

	/**
	 * @param mContext
	 * @param mHandler
	 */
	public ApkUpdate(Context mContext) {
		super();
		this.mContext = mContext;
	}

	public void UpdateApk(final UpdateApp mDomain) {
		if (mDomain == null) {
			return;
		}
		String title = "升级提示";
		StringBuilder sb_info = new StringBuilder();

		String info = mDomain.content;
		sb_info.append(info);
		boolean isForce = mDomain.forceUpdate > 0;
		int v = AppVersion.getVersionCode(mContext);
		if (mDomain.versionCode <= v) {
			Toast.makeText(mContext, "已经是最新版本", Toast.LENGTH_SHORT).show();
			return;
		}
		DialogHelper.getDialogUploadApkDialog(mContext, title, sb_info.toString(), null, new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!FileTool.isSDExist()) {
					Toast.makeText(mContext, "SD卡不存在，请检查SD卡", Toast.LENGTH_SHORT).show();
					return;
				}
				if (FileTool.getSDAvailaleSpace() < 10) {
					Toast.makeText(mContext, "SD卡剩余存储空间不足", Toast.LENGTH_SHORT).show();
					return;
				}
				/**
				 * 如果APK已经存在，那么直接安装
				 */
				String filePath = getFilePath(mDomain.downloadurl, mDomain.version);
				if (checkApkExsit(filePath)) {
					Intent intent = new Intent();
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.setAction(android.content.Intent.ACTION_VIEW);
					intent.setDataAndType(Uri.fromFile(new File(filePath)), "application/vnd.android.package-archive");
					mContext.startActivity(intent);

					return;
				}
				String appName = mContext != null ? mContext.getString(R.string.app_name) : "app";
				// 开始更新
				AppDownloadService.downNewFile(mDomain.downloadurl, filePath, 10000, appName);
			}
		}, isForce);
	}

	/**
	 * @author zhaolin
	 * @Desc: apk 路径
	 * @param apkUrl
	 * @param verName
	 * @return String
	 */
	public String getFilePath(String apkUrl, String verName) {
		if (!FileTool.isExist(Commons.CACHEAPK))
			FileTool.mkdirFile(Commons.CACHEAPK);

		String tempName = MD5Tool.getMD5(verName + FileTool.GetFileName(apkUrl));
		if (TextUtils.isEmpty(tempName)) {
			tempName = "" + System.currentTimeMillis();
		}

		final String fileName = tempName + ".apk"; // 获取文件名

		return Commons.CACHEAPK + File.separator + fileName;
	}

	

	/**
	 * @author zhaolin
	 * @Desc: 检查apk是否存在
	 * @param filePath
	 * @return boolean
	 */
	public boolean checkApkExsit(String filePath) {
		return FileTool.isExist(filePath) && checkApkRight(filePath);
	}

	/**
	 * 检查apk是否是完整的
	 */
	public boolean checkApkRight(String filePath) {
		boolean result = false;
		try {
			PackageManager pm = mContext.getPackageManager();
			PackageInfo info = pm.getPackageArchiveInfo(filePath, PackageManager.GET_ACTIVITIES);
			if (info != null) {
				result = true;
			}
		} catch (Exception e) {
			result = false;
		}
		return result;
	}
}