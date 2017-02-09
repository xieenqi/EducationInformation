package com.cdeducation.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class NetworkTool {

	public static final int STATA_NO_NET = -1;
	public static final int STATA_2G3G4G = 0;
	public static final int STATA_WIFI = 1;

	/**
	 * 网络是否可用，包括3G和WIFI中的任意一种
	 */
	public static boolean NetworkAvailable(Context context) {
		try {
			ConnectivityManager connectivity = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = connectivity.getActiveNetworkInfo();
			if (info == null || !info.isConnected()) {
				return false;
			}
			if (info.isRoaming()) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public static int NetworkType(Context context) {
		try {
			ConnectivityManager connectivity = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = connectivity.getActiveNetworkInfo();
			if (info == null)
				return -1;
			return info.getType();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * GPRS/3G 是否可用
	 */
	public static boolean GprsNetworkAvailable(Context context) {
		try {
			ConnectivityManager connectivity = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity == null) {
				return false;
			} else {
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (info == null) {
					return false;
				} else {
					if (info.isAvailable()) {
						return true;
					}
				}
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
			return false;
		} catch (RuntimeException e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}

	/**
	 * wifi 是否可用
	 */
	public static boolean WifiNetworkAvailable(Context context) {
		try {
			WifiManager wifiManager = (WifiManager) context
					.getSystemService(Context.WIFI_SERVICE);
			WifiInfo wifiInfo = wifiManager.getConnectionInfo();
			int ipAddress = wifiInfo == null ? 0 : wifiInfo.getIpAddress();//

			if (wifiManager.isWifiEnabled() && ipAddress != 0) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 得到wifi mac
	 */
	public static String getMac(Context context) {
		try {
			WifiManager wifiManager = (WifiManager) context
					.getSystemService(Context.WIFI_SERVICE);
			WifiInfo wifiInfo = wifiManager.getConnectionInfo();
			return wifiInfo.getMacAddress();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}