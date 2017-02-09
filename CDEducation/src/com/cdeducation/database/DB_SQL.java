package com.cdeducation.database;

public class DB_SQL {
	/**
	 * 地址查询
	 */
	public static String SQL_AREA = "select * from " + DBHelper.TABLE_AREA
			+ " where PARENT_ID =  ";

	public static String SQL_AREA_QUERY_BY_ID = "select * from " + DBHelper.TABLE_AREA
			+ " where ID in (%s)";

	public static String SQL_AREA_QUERY_BY_PARENT_ID = "select * from "
			+ DBHelper.TABLE_AREA + " where PARENT_ID = %d and NAME like %s";

	public static String SQL_AREA_QUERY_BY_NAME = "select * from " + DBHelper.TABLE_AREA
			+ " where NAME like %s order by id DESC";

	public static String SQL_AREA_INFO = "select * from " + DBHelper.TABLE_AREA
			+ " where ID = ";
}
