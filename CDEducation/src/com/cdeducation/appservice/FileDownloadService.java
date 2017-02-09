package com.cdeducation.appservice;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.cdeducation.HomeMainActivity;
import com.cdeducation.config.Commons;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;

public class FileDownloadService extends Service {
	
	public static final String KEY_PRECENT = "precent";
	public static final String KEY_HTTP_STATE = "key_http_state";
	public static final String KEY_HTTP_DATA = "key_http_data";
	public static final int DOWNLOAD_ERR = 1;
	public static final int DOWNLOAD_CANCEL = 2;
	public static final int DOWNLOAD_SUCC = 3;
	public static final int DOWNLOAD_PROGRESS = 4;
	private static NotificationManager nm;
	private static Notification notification;
	private static boolean cancelUpdate = false;
	private static MyHandler myHandler;
	private static ExecutorService executorService = Executors.newFixedThreadPool(5); // 固定五个线程来执行任务
	public static Map<Integer, Integer> download = new HashMap<Integer, Integer>();
	public static Context context;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		myHandler = new MyHandler(Looper.myLooper(), FileDownloadService.this);
		context = this;
	}

	@Override
	@Deprecated
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		download.clear();
		nm.cancelAll();
	}

	@Override
	public boolean onUnbind(Intent intent) {
		return super.onUnbind(intent);
	}

	public static void downNewFile(final String url, final String filePath, final int notificationId, final String name) {
		if (download.containsKey(notificationId))
			return;
		notification = new Notification();
		notification.icon = android.R.drawable.stat_sys_download;
		notification.tickerText = name + "开始下载";
		notification.when = System.currentTimeMillis();
		notification.defaults = Notification.DEFAULT_LIGHTS;
		// 显示在“正在进行中”
		notification.flags = Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;
		PendingIntent contentIntent = PendingIntent.getActivity(context, notificationId, new Intent(context, HomeMainActivity.class), 0);
		notification.setLatestEventInfo(context, name, "0%", contentIntent);
		download.put(notificationId, 0);
		// 将下载任务添加到任务栏中
		nm.notify(notificationId, notification);
		// 启动线程开始执行下载任务
		downFile(url, filePath, notificationId, name);
	}

	// 下载更新文件
	private static void downFile(final String url, final String filePath, final int notificationId, final String name) {
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				File tempFile = null;
				try {
					tempFile = new File(filePath);
					if (tempFile.exists())
						tempFile.delete();
					tempFile.createNewFile();
					HttpClient client = new DefaultHttpClient();
					// params[0]代表连接的url
					HttpGet get = new HttpGet(url);
					HttpResponse response = client.execute(get);
					HttpEntity entity = response.getEntity();
					long length = entity.getContentLength();
					InputStream is = entity.getContent();
					if (is != null) {
						File rootFile = new File(Commons.DOWNLOAD);
						if (!rootFile.exists() && !rootFile.isDirectory())
							rootFile.mkdir();

						// 已读出流作为参数创建一个带有缓冲的输出流
						BufferedInputStream bis = new BufferedInputStream(is);

						// 创建一个新的写入流，讲读取到的图像数据写入到文件中
						FileOutputStream fos = new FileOutputStream(tempFile);
						// 已写入流作为参数创建一个带有缓冲的写入流
						BufferedOutputStream bos = new BufferedOutputStream(fos);

						int read;
						long count = 0;
						int precent = 0;
						byte[] buffer = new byte[1024];
						while ((read = bis.read(buffer)) != -1 && !cancelUpdate) {
							bos.write(buffer, 0, read);
							count += read;
							precent = (int) (((double) count / length) * 100);

							// 每下载完成1%就通知任务栏进行修改下载进度
							if (precent - download.get(notificationId) >= 1) {
								download.put(notificationId, precent);
								Message message = myHandler.obtainMessage(DOWNLOAD_PROGRESS, precent);
								Bundle bundle = new Bundle();
								bundle.putString("name", name);
								bundle.putInt(KEY_PRECENT, precent);
								message.setData(bundle);
								message.arg1 = notificationId;
								myHandler.sendMessage(message);
							}
						}
						bos.flush();
						bos.close();
						fos.flush();
						fos.close();
						is.close();
						bis.close();
					}

					if (!cancelUpdate) {
						Message message = myHandler.obtainMessage(DOWNLOAD_SUCC, tempFile);
						message.arg1 = notificationId;
						Bundle bundle = new Bundle();
						bundle.putString("name", name);
						message.setData(bundle);
						myHandler.sendMessage(message);
					} else {
						tempFile.delete();
						Message message = myHandler.obtainMessage(DOWNLOAD_CANCEL, tempFile);
						message.arg1 = notificationId;
						Bundle bundle = new Bundle();
						bundle.putString("name", name);
						message.setData(bundle);
						myHandler.sendMessage(message);
					}
				} catch (ClientProtocolException e) {
					if (tempFile.exists())
						tempFile.delete();
					Message message = myHandler.obtainMessage(DOWNLOAD_ERR, name + "下载失败：网络异常！");
					message.arg1 = notificationId;
					myHandler.sendMessage(message);
				} catch (IOException e) {
					if (tempFile.exists())
						tempFile.delete();
					Message message = myHandler.obtainMessage(DOWNLOAD_ERR, name + "下载失败：文件传输异常");
					message.arg1 = notificationId;
					myHandler.sendMessage(message);
				} catch (Exception e) {
					if (tempFile.exists())
						tempFile.delete();
					Message message = myHandler.obtainMessage(DOWNLOAD_ERR, name + "下载失败," + e.getMessage());
					message.arg1 = notificationId;
					myHandler.sendMessage(message);
				}
			}
		});
	}

	/* 事件处理类 */
	class MyHandler extends Handler {
		private Context context;

		public MyHandler(Looper looper, Context c) {
			super(looper);
			this.context = c;
		}

		@Override
		public void handleMessage(Message msg) {
			PendingIntent contentIntent = null;
			Intent intent=new Intent(MessgeReceiver.ACTION_NAME);
			super.handleMessage(msg);
			if (msg != null) {
				switch (msg.what) {
				case DOWNLOAD_SUCC:
					contentIntent = PendingIntent.getActivity(FileDownloadService.this, msg.arg1, new Intent(FileDownloadService.this, HomeMainActivity.class), 0);
					notification.setLatestEventInfo(FileDownloadService.this, msg.getData().getString("name") + "下载完成", "100%", contentIntent);
					nm.notify(msg.arg1, notification);
					download.remove(msg.arg1);
					nm.cancel(msg.arg1);
					intent.putExtra(KEY_HTTP_STATE, DOWNLOAD_SUCC);
					intent.putExtra(KEY_HTTP_DATA, msg.getData());
					sendBroadcast(intent);
					break;
				case DOWNLOAD_PROGRESS:
					contentIntent = PendingIntent.getActivity(FileDownloadService.this, msg.arg1, new Intent(FileDownloadService.this, HomeMainActivity.class), 0);
					notification.setLatestEventInfo(FileDownloadService.this, msg.getData().getString("name") + "正在下载", download.get(msg.arg1) + "%", contentIntent);
					nm.notify(msg.arg1, notification);
					intent.putExtra(KEY_HTTP_STATE, DOWNLOAD_PROGRESS);
					intent.putExtra(KEY_HTTP_DATA, msg.getData());
					sendBroadcast(intent);
					break;
				case DOWNLOAD_ERR:
					download.remove(msg.arg1);
					nm.cancel(msg.arg1);
					intent.putExtra(KEY_HTTP_STATE, DOWNLOAD_ERR);
					intent.putExtra(KEY_HTTP_DATA, msg.getData());
					sendBroadcast(intent);
					break;
				case DOWNLOAD_CANCEL:
					download.remove(msg.arg1);
					nm.cancel(msg.arg1);
					intent.putExtra(KEY_HTTP_STATE, DOWNLOAD_CANCEL);
					intent.putExtra(KEY_HTTP_DATA, msg.getData());
					sendBroadcast(intent);
					break;
				}
			}
		}
	}

}
