package com.cdeducation.util;

public class HttpResultTool {
	public static final int HTTP_OK = 1;
	public static final int HTTP_ERROR = 0;

	public static final int HTTP_SYS_ERROR = 7;// 平台异常

	public static final int HTTP_CENTRE_TOKENERROR = 2;
	public static final int HTTP_CENTRE_BLACKLIST = 3;

	public static final int HTTP_JSON_ERROR = 48;
	public static final int HTTP_URL_NULL = 49;
	public static final int HTTP_NO_NETWORK = 50;

	public static final int HTTP_LOAD_INIT = 100;
	public static final int HTTP_LOAD_DOWN = 101;
	public static final int HTTP_LOAD_UP = 102;
}
