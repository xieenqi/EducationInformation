package com.cdeducation.service;

import com.cdeducation.config.Config;

/**
 * @author t77yq @2014-07-18.
 */
public class NewsItemFetcher {
	/************************** 教育装备接口start **********************************/
	public static final String HOST = Config.BASE_URL+"/AppInterface";
	/**
	 * int TopNum
	 */
	public static final String NEWS_AD_PIC = HOST + "/NewsPic.ashx";// 广告栏目的列表
	/**
	 * MenuId（栏目ID号）
	 */
	public static final String NEWS_CONTENT = HOST + "/ColumnContent.ashx";// 栏目的内容
	/**
	 * MenuId（栏目ID号） TopNum（显示栏目数量） IsRecommend（是否推荐）
	 */
	public static final String NEWS_INFO_LIST = HOST + "/ColumnInformation.ashx";// 栏目信息列表
	/**
	 * MenuId（栏目ID） startTime（开始时间） endTime（结束时间） keywords（关健字） SearchType（搜索类型）
	 * pageRows（分页行数） page（当前页） order（排序） sort（排序方式）
	 */
	public static final String NEWS_PAGE_LIST = HOST + "/ColumnInformationList.ashx";// 栏目分页列表
	/**
	 * Id（栏目列表信息ID号）
	 */
	public static final String NEWS_LIST_INFO = HOST + "/InformationGet.ashx";// 栏目列表信息
	/**
	 * Id（栏目名称ID号）
	 */
	public static final String NEWS_FUNCTION_MENU = HOST + "/FunctionMenuGet.ashx";// 获取当前栏目名称及当前位置
	/************************** 教育装备接口end **********************************/

	// 广告PPT接口,和文章接口一样,但是没有参数
	public static final String ADVARTICLE = HOST + "/public/advarticle";// 广告PPT接口,和文章接口一样,但是没有参数

	public static final String NEWS_ITEM = HOST + "/public/items";// 栏目获取接口

	public static final String NEWS_ITEM_LIST = HOST + "/public/article";// 获取文章列表接口

	public static final String NEWS_DETAILS = HOST + "/public/content/article/";// 文章URL

	public static final String NEWS_SEARCH = HOST + "/public/search"; // 搜索

	public static final String NEWS_SEETNG = HOST + "/public/config";// 系统订阅接口

	public static final String USR_LOGIN = HOST + "/public/login";// 登录
	/**
	 * parm:email 用户注册邮箱 parm:md5(password) 状态代码：错误类别，0成功，1邮箱重复，2验证码错误
	 */
	public static final String USR_REG = HOST + "/public/reg";// 注册
	/** 状态代码：错误类别，0成功，1原始密码错误 */
	public static final String USR_REPASS = HOST + "/public/repass";// 重置密码
	public static final String USR_FINDPASS = HOST + "/public/findpass";// 找回密码
	public static final String USR_INFO = HOST + "/public/user/infor";// 获取用户信息接口

	/** HTTP信息头里加上email:md5(password) */
	/** parm:type 修改个人信息的类别：names昵称,icon头像,mobile手机号,sex性别,area地区 */
	/** parm:data /** 修改个人信息数据，如果type=icon,data使用bas64将图片打成字符串 */
	/**
	 * 返回数据对象 recode /** 状态代码：错误类别，0成功，1错误
	 */
	public static final String USER_UPDATE = HOST + "/public/update";// 信息修改接口
	/** parm:os ios/ipad/android */
	/**
	 * 返回数据对象 version /** 版本号
	 */
	/** appDownloadUrl /**下载app的url **/
	public static final String VERSION = HOST + "/public/version";// 获取版本号

	public static final String GUESTBOOK = HOST + "/public/guestbook";// 意見反饋

}
