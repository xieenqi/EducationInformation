package com.cdeducation.model;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import com.cdeducation.data.AreaInfo;
import com.cdeducation.database.DBManager;

/**
 * Created by Administrator on 14-2-20.
 */
public class AreaManager {
	private static DBManager dbManager = null;

	private static AreaManager areaManager = null;

	private AreaManager(Context context) {
		dbManager = DBManager.getInstance(context);
	}

	public static AreaManager getInstance(Context context) {
		if (areaManager == null) {
			areaManager = new AreaManager(context);
		}
		return areaManager;
	}

	public List<AreaInfo> getProvinceList() {
		List<AreaInfo> provinceList = new ArrayList<AreaInfo>();
		Cursor cursor = dbManager.queryProvince();
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			do {
				AreaInfo areaInfo = new AreaInfo();
				areaInfo.id = cursor.getInt(cursor.getColumnIndex("ID"));
				areaInfo.name = cursor.getString(cursor.getColumnIndex("NAME"));
				areaInfo.capitalId = cursor.getInt(cursor
						.getColumnIndex("CAPITAL"));
				areaInfo.parentId = cursor.getInt(cursor
						.getColumnIndex("PARENT_ID"));
				areaInfo.extra = cursor.getString(cursor
						.getColumnIndex("SEQUENCE"));
				provinceList.add(areaInfo);
			} while (cursor.moveToNext());
			cursor.close();
		}
		dbManager.close();
		return provinceList;
	}

	public List<AreaInfo> getCityList(int provinceCode) {
		List<AreaInfo> cityList = new ArrayList<AreaInfo>();
		Cursor cursor = dbManager.queryCity(provinceCode);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			do {
				AreaInfo areaInfo = new AreaInfo();
				areaInfo.id = cursor.getInt(cursor.getColumnIndex("ID"));
				areaInfo.name = cursor.getString(cursor.getColumnIndex("NAME"));
				areaInfo.capitalId = cursor.getInt(cursor
						.getColumnIndex("CAPITAL"));
				areaInfo.parentId = cursor.getInt(cursor
						.getColumnIndex("PARENT_ID"));
				areaInfo.extra = cursor.getString(cursor
						.getColumnIndex("SEQUENCE"));
				cityList.add(areaInfo);
			} while (cursor.moveToNext());
			cursor.close();
		}
		dbManager.close();
		return cityList;
	}

	public List<AreaInfo> getDistrictList(int cityCode) {
		List<AreaInfo> districtList = new ArrayList<AreaInfo>();
		Cursor cursor = dbManager.queryDistrict(cityCode);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			do {
				AreaInfo areaInfo = new AreaInfo();
				areaInfo.id = cursor.getInt(cursor.getColumnIndex("ID"));
				areaInfo.name = cursor.getString(cursor.getColumnIndex("NAME"));
				areaInfo.capitalId = cursor.getInt(cursor
						.getColumnIndex("CAPITAL"));
				areaInfo.parentId = cursor.getInt(cursor
						.getColumnIndex("PARENT_ID"));
				areaInfo.extra = cursor.getString(cursor
						.getColumnIndex("SEQUENCE"));
				districtList.add(areaInfo);
			} while (cursor.moveToNext());
			cursor.close();
		}
		dbManager.close();
		return districtList;
	}

	public AreaInfo getParentAreaInfo(int cityCode) {
		Cursor cursor = dbManager.queryDistrict(cityCode);
		AreaInfo areaInfo = new AreaInfo();
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			do {
				areaInfo.id = cursor.getInt(cursor.getColumnIndex("ID"));
				areaInfo.name = cursor.getString(cursor.getColumnIndex("NAME"));
				areaInfo.capitalId = cursor.getInt(cursor
						.getColumnIndex("CAPITAL"));
				areaInfo.parentId = cursor.getInt(cursor
						.getColumnIndex("PARENT_ID"));
				areaInfo.extra = cursor.getString(cursor
						.getColumnIndex("SEQUENCE"));
			} while (cursor.moveToNext());
			cursor.close();
		}
		dbManager.close();
		return areaInfo;
	}

	public AreaInfo getAreaInfo(int areaCode) {
		Cursor cursor = dbManager.queryAreaInfo(areaCode);
		AreaInfo areaInfo = new AreaInfo();
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			do {
				areaInfo.id = cursor.getInt(cursor.getColumnIndex("ID"));
				areaInfo.name = cursor.getString(cursor.getColumnIndex("NAME"));
				areaInfo.capitalId = cursor.getInt(cursor
						.getColumnIndex("CAPITAL"));
				areaInfo.parentId = cursor.getInt(cursor
						.getColumnIndex("PARENT_ID"));
				areaInfo.extra = cursor.getString(cursor
						.getColumnIndex("SEQUENCE"));
			} while (cursor.moveToNext());
			cursor.close();
		}
		dbManager.close();
		return areaInfo;
	}
}
