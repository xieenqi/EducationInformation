package com.cdeducation.fragment;


import com.cdeducation.App;
import com.cdeducation.EditNameActivity;
import com.cdeducation.EditPhoneActivity;
import com.cdeducation.FixPassActivity;
import com.cdeducation.R;
import com.cdeducation.SelectAddressActivity;
import com.cdeducation.config.Commons;
import com.cdeducation.config.Config;
import com.libray.util.BitmapUtil;
import com.libray.util.ImageUtils;
import com.libray.util.ViewFinder;
import com.libray.view.CircleImageView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 设置界面
 * 
 * @author zhaolin 2015-2-12
 * @Desc:
 */
public class UserEditFragment extends BaseFragment implements View.OnClickListener {

	CircleImageView headerImage;
	TextView loginTv;
	TextView edit_addres;
	TextView edit_sex;
	TextView edit_email;
	TextView edit_name;
	TextView edit_phone;

	public interface OnSecondaryMenuClosed {

		public void onSecondaryMenuClosed(int index);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_user_edit, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		headerImage = ViewFinder.getView(view, R.id.set_headerImage);
		headerImage.setOnClickListener(this);
		ViewFinder.getView(view, R.id.edit_head).setOnClickListener(this);
		ViewFinder.getView(view, R.id.edit_name_ll).setOnClickListener(this);
		ViewFinder.getView(view, R.id.edit_phone_ll).setOnClickListener(this);
		ViewFinder.getView(view, R.id.edit_sex_ll).setOnClickListener(this);
		ViewFinder.getView(view, R.id.edit_address_ll).setOnClickListener(this);
		ViewFinder.getView(view, R.id.edit_pass_ll).setOnClickListener(this);
		ViewFinder.getView(view, R.id.logout_btn).setOnClickListener(this);
		edit_name = ViewFinder.getView(view, R.id.edit_name);
		edit_addres = ViewFinder.getView(view, R.id.edit_address);
		edit_sex = ViewFinder.getView(view, R.id.edit_sex);
		edit_email = ViewFinder.getView(view, R.id.edit_email);
		edit_phone = ViewFinder.getView(view, R.id.edit_phone);
		setUI();
	}

	public void setUI() {
		edit_email.setText(App.getInstance().getSPEmail());
		edit_addres.setText(App.getInstance().getSPArea());
		String sex = App.getInstance().getSPSex();
		if (!TextUtils.isEmpty(sex)) {
			if (sex.equals("1")) {
				edit_sex.setText(getActivity().getString(R.string.sex_male));
			} else if (sex.equals("2")) {
				edit_sex.setText(getActivity().getString(R.string.sex_female));
			} else {
				edit_sex.setText(getActivity().getString(R.string.sex_baomi));
			}
		} else {
			edit_sex.setText(getActivity().getString(R.string.sex_baomi));
		}
		edit_phone.setText(App.getInstance().getSPMobile());
		edit_name.setText(App.getInstance().getSPNames());
		loadImageByVolley(Config.BASE_URL + App.getInstance().getSPIcon(), headerImage);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.edit_head:
		case R.id.set_headerImage:
			AddPhotoFragment addPhotoFragment = new AddPhotoFragment();
			addPhotoFragment.show(getChildFragmentManager(), "");
			break;
		case R.id.edit_name_ll:
			startActivityForResult(new Intent(getActivity(), EditNameActivity.class), REQUET_INTENT_EDIT_NAME);
			getActivity().overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
			break;
		case R.id.edit_phone_ll:
			startActivityForResult(new Intent(getActivity(), EditPhoneActivity.class), REQUET_INTENT_EDIT_NAME);
			getActivity().overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
			break;

		case R.id.edit_sex_ll:
			GenderFragment genderFragment = new GenderFragment();
			genderFragment.show(getChildFragmentManager(), "");
			genderFragment.setOnUpadteListener(new GenderFragment.OnUpadteListener() {

				@Override
				public void OnUpadte(int type) {
					// TODO Auto-generated method stub
					upload("sex", type + "");
				}
			});
			break;
		case R.id.edit_pass_ll:
			startActivityForResult(new Intent(getActivity(), FixPassActivity.class), REQUET_INTENT_FIXPASS);
			getActivity().overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
			break;
		case R.id.edit_address_ll:
			startActivityForResult(new Intent(getActivity(), SelectAddressActivity.class), REQUET_INTENT_SELECT_ADDRESS);
			getActivity().overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
			break;
		case R.id.logout_btn:
			App.setLogin(false);
			getActivity().finish();
			Commons.IS_SETTING_PAGE = true;
			break;

		}
	}

	public void setAddress(String address) {
		if (edit_addres != null)
			edit_addres.setText(address);
	}

	public void uploadImage(Uri cropImageUri) {

		if (cropImageUri != null) {
			Bitmap mBitmap = BitmapUtil.uriToBitmap(getActivity(), ImageUtils.cropImageUri);
			if (mBitmap != null) {
				String data = BitmapUtil.getBitmapStrBase64(mBitmap);
				if (!TextUtils.isEmpty(data)) {
					upload("icon", data);
				}

			}
		}
	}

	public void swichType(String type, String data) {
		super.swichType(type, data);
		swichTypeSelf(type, data);
	}

	@Override
	public void updateData() {
		// TODO Auto-generated method stub
		setUI();
	}

	public void swichTypeSelf(String type, String data) {
		if (TextUtils.isEmpty(type) || TextUtils.isEmpty(data))
			return;
		if (type.equals("names")) {
			App.saveNames(data);
			updateData();
		} else if (type.equals("icon")) {
			getUserInfo();
		} else if (type.equals("mobile")) {
			App.saveMobile(data);
			updateData();
		} else if (type.equals("sex")) {
			App.saveSex(data);
			updateData();
		} else if (type.equals("area")) {
			App.saveArea(data);
			updateData();
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != Activity.RESULT_OK) {
			return;
		}
		switch (requestCode) {
		case REQUET_INTENT_SELECT_ADDRESS:
			String address = data.getStringExtra("data");
			upload("area", address);
			break;
		case REQUET_INTENT_EDIT_NAME:
			if (data != null) {
				String type = data.getStringExtra("type");
				String name = data.getStringExtra("data");
				swichTypeSelf(type, name);
			}
			break;
		}
	}

}
