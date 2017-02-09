package com.libray.tools;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

/**
 * <p>
 * Title: JsonUtil
 * </p>
 * <p>
 * Description:Json数据辅助类,将服务器返回的josn字符串进行解析
 * 服务器未返回具体数据的主要通过验证success字段和msg字段;返回数据的主要验证success字段和data字段
 * </p>
 * *
 * 
 * @author zhaolin
 * @date 203-6-13 16:33
 */
public final class JsonUtil {
	/**
	 * 将服务器返回结果中的字段还原成相应的实体类
	 * 
	 * @param json
	 *            服务器返回给手机端的字符串(明文)
	 * @param cls
	 *            实体类的Class
	 * @return 返回data数据对应的实体类对象
	 */
	public static final <T extends Object> T toBean(
			String json, Class<T> cls) throws Exception {
		checkServerJson(json);
		String data = json.toString();
		Gson gson = GsonFactory.getGson(GsonFactory.STANDARD);
		return gson.fromJson(data, cls);
	}

	/**
	 * 将服务器返回结果中的data字段还原成相应的实体类
	 * 
	 * @param json
	 *            服务器返回给手机端的字符串(明文)
	 * @param cls
	 *            实体类的Class
	 * @return 返回data数据对应的实体类对象
	 */
	public static final <T extends Object> T toBeanName(String json,
			Class<T> cls, String clssName) throws Exception {
		checkServerJson(json);
		JSONObject obj = new JSONObject(json);
		if (obj.isNull(clssName)) {
			return null;
		}
		String data = obj.getString(clssName);
		Gson gson = GsonFactory.getGson(GsonFactory.STANDARD);
		return gson.fromJson(data, cls);
	}

	/**
	 * 将服务器返回结果中的字段还原成相应的实体类
	 * 
	 * @param json
	 *            服务器返回给手机端的字符串(明文)
	 * @param cls
	 *            实体类的Class
	 * @return 返回data数据对应的实体类对象
	 */
	public static final <T extends Object> T toBeanSimple(String json,
			Class<T> cls) throws Exception {
		checkServerJson(json);
		Gson gson = GsonFactory.getGson(GsonFactory.STANDARD);
		return gson.fromJson(json, cls);
	}
	/**
	 * 将服务器返回结果中的字段还原成相应的实体类
	 * 
	 * @param json
	 *            服务器返回给手机端的字符串(明文)
	 * @param cls
	 *            实体类的Class
	 * @return 返回data数据对应的实体类对象
	 */
	public static final boolean toBeanBoolean(String json) throws Exception {
		checkServerJson(json);
		JSONObject obj = new JSONObject(json);
		if(!obj.isNull("result")){
			return  obj.getBoolean("result");
		}
		return false;
	}
	/**
	 * 将服务器返回结果中的字段还原成相应的实体类
	 * 
	 * @param json
	 *            服务器返回给手机端的字符串(明文)
	 * @param cls
	 *            实体类的Class
	 * @return 返回data数据对应的实体类对象
	 */
	public static final int toBeanInt(String json) throws Exception {
		checkServerJson(json);
		JSONObject obj = new JSONObject(json);
		if(!obj.isNull("result")){
			return  obj.getInt("result");
		}
		return -1;
	}
	/**
	 * 将服务器返回结果中的字段还原成相应的实体类
	 * 
	 * @param json
	 *            服务器返回给手机端的字符串(明文)
	 * @param cls
	 *            实体类的Class
	 * @return 返回data数据对应的实体类对象
	 */
	public static final <T extends Object> T toBeanNoName(String json,
			Class<T> cls) throws Exception {
		checkServerJson(json);
		JSONArray jsonArray=new JSONArray(json);
		JSONObject obj = jsonArray.getJSONObject(0);
		String data = obj.toString();
        if(TextUtils.isEmpty(data)){
            return null;
        }
        if(data.trim().length()<=2){
            return null;
        }
		Gson gson = GsonFactory.getGson(GsonFactory.STANDARD);
		return gson.fromJson(data, cls);
	}
	/**
	 * 将服务器返回结果中的字段还原成相应的实体类
	 * 
	 * @param json
	 *            服务器返回给手机端的字符串(明文)
	 * @param cls
	 *            实体类的Class
	 * @return 返回data数据对应的实体类对象
	 */
	public static final <T extends Object> T toBeanNoNameO(String json,
			Class<T> cls) throws Exception {
		checkServerJson(json);
		JSONObject obj = new JSONObject(json);
		String data = obj.toString();
        if(TextUtils.isEmpty(data)){
            return null;
        }
        if(data.trim().length()<=2){
            return null;
        }
		Gson gson = GsonFactory.getGson(GsonFactory.STANDARD);
		return gson.fromJson(data, cls);
	}
	/**
	 * 检Json数据(包含data字段)是否合法(若为null或者长度为0，将抛出异常，将返回错误信息)
	 * 
	 * @param json
	 * @throws Exception
	 */
	private static void checkServerJson(String json) throws Exception {
		if (TextUtils.isEmpty(json.trim())) {
			throw new Exception("JsonUtil checkServerJson"
					+ " the return json is empty");
		}
	}

	/**
	 * 将服务器返回结果中的data字段还原成相应的实体类对象列表
	 * 
	 * @param json
	 *            服务器返回给手机端的字符串(明文)
	 * @param type
	 *            data字段对应的列表对象类型
	 * @return 返回data数据对应的实体类对象列表
	 */
	public static final <T extends Object> List<T> toBeanNoNameList(String json,
			Type type) throws Exception {
		checkServerJson(json);
		Gson gson = GsonFactory.getGson(GsonFactory.STANDARD);
		List<T> cs = gson.fromJson(json, type);
		return cs;
	}
	/**
	 * 将服务器返回结果中的data字段还原成相应的实体类对象列表
	 * 
	 * @param json
	 *            服务器返回给手机端的字符串(明文)
	 * @param type
	 *            data字段对应的列表对象类型
	 * @return 返回data数据对应的实体类对象列表
	 */
	public static final <T extends Object> List<T> toBeanList(String json,
			Type type) throws Exception {
		checkServerJson(json);
		JSONObject obj = new JSONObject(json);
		Gson gson = GsonFactory.getGson(GsonFactory.STANDARD);
		List<T> cs = gson.fromJson(obj.getString("data"), type);
		return cs;
	}

	/**
	 * 将一个对象转换成json数据
	 * 
	 * @param obj
	 *            需要被转换成json格式数据的对象
	 * @return 返回转换后json格式的字符串
	 */
	public static final String getObjectJson(Object obj) {
		if (obj == null) {
			throw new RuntimeException("the object is unUseful");
		}
		Gson gson = GsonFactory.getGson(GsonFactory.STANDARD);
		return gson.toJson(obj);
	}

	/**
	 * 从服务器返回的json数据中获取boolean类型的值(data字段)
	 * 
	 * @param json
	 * @return
	 */
	public static final boolean getSimplexBooleanFromJson(String json)
			throws Exception {

		checkServerJson(json);
		JSONObject obj = new JSONObject(json);
		return obj.getBoolean("data");
	}

	public static final boolean getBooleanFromJson(String json)
			throws Exception {

		checkServerJson(json);
		JSONObject obj = new JSONObject(json);
		if (obj.getInt("data") != 0) {
			return true;
		} else
			return false;

	}

	/**
	 * 从服务器返回的json数据中获取Integer类型的值(data字段)
	 * 
	 * @param json
	 * @return
	 */
	public static final int getSimplexIntegerFromJson(String json)
			throws Exception {
		checkServerJson(json);
		JSONObject obj = new JSONObject(json);
		return obj.getInt("data");
	}

	/**
	 * 从服务器返回的json数据中获取Long类型的值(data字段)
	 * 
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public static final long getSimplexLongFromJson(String json)
			throws Exception {
		checkServerJson(json);
		JSONObject obj = new JSONObject(json);
		return obj.getLong("data");
	}

	/**
	 * 从服务器返回的json数据中获取String类型的值(state字段)
	 * 
	 * @param json
	 * @return
	 */
	public static final String getSimplexStringFromJson(String json)
			throws Exception {
        checkServerJson(json);
		JSONObject obj = new JSONObject(json);
		return obj.getString("token");
	}

	public static JSONObject getJSONfromURL(String url,
			List<NameValuePair> nameValuePairs) {
		InputStream is = null;
		String result = "";
		JSONObject jArray = null;

		// http post
		try {

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(url);
			if (nameValuePairs != null)
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();

		} catch (Exception e) {
			Log.e("log_tag", "Error in http connection " + e.toString());
		}

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			if (is != null) {
				is.close();
			}
			result = sb.toString();
		} catch (Exception e) {
			Log.e("log_tag", "Error converting result " + e.toString());
		}

		try {

			jArray = new JSONObject(result);
		} catch (JSONException e) {
			Log.e("log_tag", "Error parsing data " + e.toString());
		}
		return jArray;
	}

	public static JSONObject getJSONfromURL(String url) {
		InputStream is = null;
		String result = "";
		JSONObject jArray = null;

		// http post
		try {

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(url);
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();

		} catch (Exception e) {
			Log.e("log_tag", "Error in http connection " + e.toString());
		}

		// convert response to string
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			if(is!=null){
				is.close();
			}
			result = sb.toString();
		} catch (Exception e) {
			Log.e("log_tag", "Error converting result " + e.toString());
		}

		try {
			jArray = new JSONObject(result);
		} catch (JSONException e) {
			Log.e("log_tag", "Error parsing data " + e.toString());
		}

		return jArray;
	}
}
