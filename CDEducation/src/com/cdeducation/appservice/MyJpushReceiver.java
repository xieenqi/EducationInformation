package com.cdeducation.appservice;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;

import org.json.JSONException;
import org.json.JSONObject;

import com.cdeducation.App;
import com.cdeducation.HomeMainActivity;
import com.cdeducation.R;
import com.cdeducation.data.NewsItems;
import com.cdeducation.fragment.SettingFragment;
import com.cdeducation.util.SharedPreferencesTool;
import com.google.gson.Gson;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器 如果不定义这个 Receiver，则： 1) 默认用户会打开主界面 2) 接收不到自定义消息
 */
public class MyJpushReceiver extends BroadcastReceiver {
	private static final String TAG = "JPush自定义";
	NotificationManager manger = null;

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		manger = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
		// 此处判断是否打开推送开关 默认打开
		if (!SharedPreferencesTool.getSharedPreferences(context, SettingFragment.pushStatus, false)) {
			return;
		}
		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
			String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
			Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
			// send the Registration Id to your server...

		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
			Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
			sedNotication(context, bundle);

		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
			Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
			int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
			Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
			Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
			String msg = bundle.getString(JPushInterface.EXTRA_EXTRA);
			Gson gson = new Gson();
			NewsItems pushData = gson.fromJson(msg, NewsItems.class);
			// 打开自定义的Activity
			App.notiflyNews = pushData;// 给这个创建一个对象就可以了NewsActivity
			if (pushData==null||pushData.Id <= 0)
				App.notiflyNews = null;
			Intent i = new Intent(context, HomeMainActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			context.startActivity(i);
			if (manger != null)
				manger.cancelAll();

		} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
			Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
			// 在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity，
			// 打开一个网页等..

		} else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
			boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
			Log.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);

		} else {
			Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
		}
	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			} else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
				if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
					Log.i(TAG, "This message has no Extra data");
					continue;
				}

				try {
					JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
					Iterator<String> it = json.keys();

					while (it.hasNext()) {
						String myKey = it.next().toString();
						sb.append("\nkey:" + key + ", value: [" + myKey + " - " + json.optString(myKey) + "]");
					}
				} catch (JSONException e) {
					Log.e(TAG, "Get message extra JSON error!");
				}

			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}

	public void sedNotication(Context context, Bundle bundle) {
		String msg = bundle.getString(JPushInterface.EXTRA_EXTRA);
		Gson gson = new Gson();
		NewsItems pushData = gson.fromJson(msg, NewsItems.class);
		String text = bundle.getString(JPushInterface.EXTRA_MESSAGE);

		RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.remote_view);
		rv.setImageViewResource(R.id.image, R.drawable.ic_launcher);
		if (pushData != null && !TextUtils.isEmpty(pushData.Title)) {
			rv.setCharSequence(R.id.text, "setText", pushData.Title);
		} else {
			rv.setCharSequence(R.id.text, "setText", text);
		}
		App.notiflyNews = pushData;// 给这个创建一个对象就可以了NewsActivity
		NotificationCompat.Builder nb = new NotificationCompat.Builder(context);

		Intent dialIntent = new Intent(context, HomeMainActivity.class);
		if (pushData != null&&pushData.Id>0) {
			dialIntent.addFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
		}
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, dialIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		nb.setContentIntent(contentIntent);

		nb.setTicker(context.getString(R.string.app_name) + "新消息");
		nb.setSmallIcon(android.R.drawable.ic_dialog_email);
		nb.setContent(rv);
		nb.setDefaults(Notification.DEFAULT_ALL);
		nb.setAutoCancel(true);
		manger = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		manger.notify(20, nb.build());
		// {"Id":352,"Title":"单位234简介"}
	}
}
