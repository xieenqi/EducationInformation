package com.cdeducation.data;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

@SuppressLint("ParcelCreator") 
public class UpdateApp implements Parcelable {

	@Expose
	public String downloadurl;
	@Expose
	public int versionCode;
	@Expose
	public String version;
	@Expose
	public String content;
	// 强制更新 0=false 1=true
	@Expose
	public int forceUpdate;

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.downloadurl);
		dest.writeInt(this.versionCode);
		dest.writeString(this.version);
		dest.writeString(this.content);
		dest.writeInt(this.forceUpdate);
	}

}