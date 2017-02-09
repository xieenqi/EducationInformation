package com.cdeducation.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

/**
 * @author zl @2014/07/25.
 */
public class UserInfo  implements Parcelable {
	
	@Expose
    public String uid;
    @Expose
    public String id;
    @Expose
    public String email;
    @Expose
    public  String name;
    @Expose
    public String icon;
    @Expose
    public String mobile;
    @Expose
    public String sex;
    @Expose
    public String area;
    @Expose
    public String loginid;
    public UserInfo() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    	  dest.writeString(this.uid);
        dest.writeString(this.id);
        dest.writeString(this.email);
        dest.writeString(this.name);
        dest.writeString(this.icon);
        dest.writeString(this.mobile);
        dest.writeString(this.sex);
        dest.writeString(this.area);
        dest.writeString(this.loginid);
    }

    private UserInfo(Parcel in) {
    	this.uid = in.readString();
        this.id = in.readString();
        this.email = in.readString();
        this.name = in.readString();
        this.icon = in.readString();
        this.mobile = in.readString();
        this.sex = in.readString();
        this.area = in.readString();
        this.loginid = in.readString();
    }

    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        public UserInfo createFromParcel(Parcel source) {
            return new UserInfo(source);
        }

        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };
}
