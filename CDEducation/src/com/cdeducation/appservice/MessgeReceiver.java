package com.cdeducation.appservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class MessgeReceiver extends BroadcastReceiver {
	public static final String ACTION_NAME = "com.cdeducation.appservice.MessgeReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (intent != null) {
			int type = intent.getIntExtra(FileDownloadService.KEY_HTTP_STATE, 0);
			Bundle bundle = intent.getBundleExtra(FileDownloadService.KEY_HTTP_DATA);
			switch (type) {
			case FileDownloadService.DOWNLOAD_SUCC:
				((OnDownloadBackListener) context).onDownloadBack(FileDownloadService.DOWNLOAD_SUCC, intent.getData());
				break;

			case FileDownloadService.DOWNLOAD_PROGRESS:
				int precent = bundle.getInt(FileDownloadService.KEY_PRECENT, 0);
				((OnDownloadBackListener) context).onDownloadProgress(precent);
				break;
			case FileDownloadService.DOWNLOAD_CANCEL:
			case FileDownloadService.DOWNLOAD_ERR:

				break;
			}
		}

	}

	public interface OnDownloadBackListener {
		public void onDownloadBack(int errCode, Object file);

		public void onDownloadProgress(int precent);
	}

}
