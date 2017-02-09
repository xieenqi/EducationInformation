package com.libray.util;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * @author t88yq @2014.02.20.
 */
public class AppVersion {

    private static PackageInfo packageInfo;

    public static int getVersionCode(Context context) {
        if (packageInfo == null) {
            try {
                packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                return 0;
            }
        }

        return packageInfo.versionCode;
    }

    public static String getVersionName(Context context) {
        if (packageInfo == null) {
            try {
                packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                return "";
            }
        }
        return packageInfo.versionName;
    }
}
