/**
 * 2013-1-6 下午4:29:33
 * Administrator
 * TODO
 */
package com.libray.tools;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;




/**
 * 2013-1-6 下午4:29:33
 * 
 * @author suji
 *         <p>
 *         Title: GsonFactory
 *         </p>
 *         <p>
 *         Description: Gson工厂类
 *         </p>
 */
public class GsonFactory {

	public static final int STANDARD = 1;
    public static final String JSON_DATE_FORMAT = "yyyy-MM-dd";
	/**
	 * 获取一个Gson对象
	 * 
	 * @param style
	 *            需要获取的Gson对象的类型(参考GsonFactory中的常量)
	 * @return 返回对象类型的Gson对象
	 */
	public static Gson getGson(int style) {
		Gson gson = null;
		switch (style) {
		case STANDARD:
			gson = new GsonBuilder().serializeNulls()
					.setDateFormat(JSON_DATE_FORMAT)
					.excludeFieldsWithoutExposeAnnotation().create();
			break;
		}
		return gson;
	}
}
