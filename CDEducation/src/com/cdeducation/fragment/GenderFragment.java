package com.cdeducation.fragment;

import com.cdeducation.R;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

public class GenderFragment extends DialogFragment {
	private OnUpadteListener listener = null;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog dialog = new Dialog(getActivity(), R.style.dialogBottom);
		dialog.setContentView(R.layout.fragment_gender);
		Button gender_nan = (Button) dialog.findViewById(R.id.gender_nan);
		gender_nan.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onUpadteText(1);//男
				dismiss();
			}
		});
		Button gender_nv = (Button) dialog.findViewById(R.id.gender_nv);
		gender_nv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onUpadteText(2);//女
				dismiss();
			}
		});
		Button gender_secrecy = (Button) dialog
				.findViewById(R.id.gender_secrecy);
		gender_secrecy.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onUpadteText(0);//保密
				dismiss();
			}
		});
		Button gender_cancel = (Button) dialog.findViewById(R.id.gender_cancel);
		gender_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		RelativeLayout layout = (RelativeLayout) dialog
				.findViewById(R.id.gender_layout);
		layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		return dialog;
	}

	public interface OnUpadteListener {

		public void OnUpadte(int type);

	}

	public void setOnUpadteListener(OnUpadteListener onUpadteListener) {
		listener = onUpadteListener;
	}

	private void onUpadteText(int sexType) {
		if(listener!=null){
			listener.OnUpadte(sexType);
		}
		
	}

}
