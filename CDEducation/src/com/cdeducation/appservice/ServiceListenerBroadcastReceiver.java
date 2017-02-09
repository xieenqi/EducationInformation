package com.cdeducation.appservice;

import cn.jpush.android.api.JPushInterface;

import com.cdeducation.App;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ServiceListenerBroadcastReceiver extends BroadcastReceiver {
	public static final String ACTION_NAME = "com.cdeducation.appservice.AppDownloadService";

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		boolean isServiceRunning = false;

		if (intent.getAction().equals(Intent.ACTION_TIME_TICK)) {
			// 检查Service状态
			ActivityManager manager = (ActivityManager) App.getContext().getSystemService(Context.ACTIVITY_SERVICE);
			for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
				if (ACTION_NAME.equals(service.service.getClassName()))

				{
					isServiceRunning = true;
				}

			}
			if (!isServiceRunning) {
				JPushInterface.resumePush(context);
				Intent i = new Intent(context, AppDownloadService.class);
				context.startService(i);
			}

		}
	}

}
