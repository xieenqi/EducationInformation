package com.cdeducation.database;

import com.cdeducation.R;

import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author t77yq @2014-07-22.
 */
public class DBHelper extends SQLiteOpenHelper {
	private Context context;
	public static final String TABLE_PPT = "ppt_item";

	public static final String TABLE_AREA = "Area";
	private String[] createAreaScript;

	public DBHelper(Context context, String name,
			SQLiteDatabase.CursorFactory factory, int version) {
		super(context, name, factory, version);
		this.context = context;
	}

	public static final String CREATE_AREA_TABLE = "CREATE table IF NOT EXISTS "
			+ TABLE_AREA
			+ " (ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
			+ "NAME TEXT, PARENT_ID INTEGER, CAPITAL INTEGER, SEQUENCE TEXT)";
	public static final String DROP_AREA = "DROP table IF EXISTS " + TABLE_AREA;

	public static final String CREATE_PPT_ITEM_TABLE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_PPT
			+ "(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "Id INTEGER,"
			+ "Title VARCHAR,"
			+ "GetdateTime VARCHAR,"
			+ "MenuId VARCHAR)";
	public static final String DROP_PPT_ITEM = "DROP table IF EXISTS "
			+ TABLE_PPT;

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_PPT_ITEM_TABLE);
		db.execSQL(CREATE_AREA_TABLE);
		Resources res = context.getResources();
		createAreaScript = res.getStringArray(R.array.area_text);
		executeSQLScript(db, createAreaScript);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (newVersion > oldVersion) {
			deleteAllOldTable(db);
			onCreate(db);
		}
	}

	private void deleteAllOldTable(SQLiteDatabase db) {

		db.execSQL(DROP_PPT_ITEM);
		db.execSQL(DROP_AREA);
	}

	private void executeSQLScript(SQLiteDatabase database, String[] createScript) {

		try {

			for (String sqlStatement : createScript) {
				sqlStatement = sqlStatement.trim();
				if (sqlStatement.length() > 0) {
					if (database.isOpen())
						database.execSQL(sqlStatement);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
