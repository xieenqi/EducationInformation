package com.cdeducation.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cdeducation.data.NewsItems;

/**
 * @author t77yq @2014-07-22.
 */
public class DBManager {

	public static final String DB_NAME = "DB_DATABASE";

	private static final int DB_VERSION = 2;

	private DBHelper helper;
	public static DBManager dbManager;

	public DBManager(Context context) {
		if (helper == null) {
			helper = new DBHelper(context, DB_NAME, null, DB_VERSION);
		}
	}

	public static DBManager getInstance(Context context) {
		if (dbManager == null) {
			dbManager = new DBManager(context);
		}
		return dbManager;
	}

	public void addItem(NewsItems item) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.beginTransaction();
		db.execSQL("INSERT INTO " + DBHelper.TABLE_PPT + " VALUES(null, ?, ?, ?,?)", new Object[] { item.Id, item.Title, item.GetdateTime, item.MenuId });
		db.setTransactionSuccessful();
		db.endTransaction();
	}

	public Cursor queryAll() {
		SQLiteDatabase db = helper.getReadableDatabase();
		return db.rawQuery("select * from " + DBHelper.TABLE_PPT + " order by _id desc", null);
	}

	public Cursor queryByNo(int no) {
		SQLiteDatabase db = helper.getReadableDatabase();
		return db.rawQuery("select * from " + DBHelper.TABLE_PPT + " where Id=" + no, null);
	}

	public void deleteByNo(int no) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.beginTransaction();
		db.execSQL("delete from " + DBHelper.TABLE_PPT + " where Id = ?", new Object[] { no });
		db.setTransactionSuccessful();
		db.endTransaction();
	}

	/**
	 * 地址查詢
	 * 
	 * @return
	 */
	public Cursor queryProvince() {
		SQLiteDatabase db = helper.getReadableDatabase();
		return db.rawQuery(DB_SQL.SQL_AREA + String.valueOf(0), null);
	}

	public Cursor queryCity(int provinceCode) {
		SQLiteDatabase db = helper.getReadableDatabase();
		return db.rawQuery(DB_SQL.SQL_AREA + String.valueOf(provinceCode), null);
	}

	public Cursor queryDistrict(int cityCode) {
		SQLiteDatabase db = helper.getReadableDatabase();
		return db.rawQuery(DB_SQL.SQL_AREA + String.valueOf(cityCode), null);
	}

	public Cursor queryAreaInfo(int areaCode) {
		SQLiteDatabase db = helper.getReadableDatabase();
		return db.rawQuery(DB_SQL.SQL_AREA_INFO + String.valueOf(areaCode), null);
	}

	public void close() {
		helper.close();
	}
}
