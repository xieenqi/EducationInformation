package com.cdeducation.view;


import com.cdeducation.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

public class DialogHelper {

	private DialogHelper() {
	};


	/**
	 * @param context
	 *            默认可以取消
	 * 
	 * @return APK更新的对话框
	 */
	public static Dialog getDialogUploadApkDialog(Context context,
			String title, String info,
			final OnClickListener cancleOnClickListener,
			final OnClickListener okOnClickListener, final boolean isForce) {

		final AlertDialog dialog = new AlertDialog.Builder(context).create();
		dialog.setCancelable(!isForce);
		dialog.show();

		Window view = dialog.getWindow();
		view.setContentView(R.layout.dialog_upload_apk);
		TextView tv_msg_info = (TextView) view.findViewById(R.id.tv_msg_info);
		TextView tv_title = (TextView) view.findViewById(R.id.tv_title);

		if (isForce) {
			view.findViewById(R.id.tv_later).setVisibility(View.GONE);
			view.findViewById(R.id.iv_line).setVisibility(View.GONE);
		}

		if (info != null) {
			tv_msg_info.setText(info);
		}

		if (title != null) {
			tv_title.setText(title);
		} else {
			// 没有title的类型
			view.findViewById(R.id.rl_title).setVisibility(View.GONE);
		}

		View tv_ok = view.findViewById(R.id.tv_ok);
		tv_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (okOnClickListener != null)
					okOnClickListener.onClick(v);
				if (isForce)
					return;
				dialog.dismiss();
			}
		});
		View tv_later = view.findViewById(R.id.tv_later);
		tv_later.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (cancleOnClickListener != null)
					cancleOnClickListener.onClick(v);
				if (isForce)
					return;
				dialog.dismiss();
			}
		});

		return dialog;
	}
}
