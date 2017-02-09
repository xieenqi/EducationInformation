package com.cdeducation.util;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cdeducation.App;
import com.cdeducation.service.IDataCallBack;
import com.cdeducation.service.RequestHeader;
import com.cdeducation.service.RequestJsonArrayHeader;
import com.cdeducation.service.RequestJsonHeader;
import com.google.gson.JsonSyntaxException;

import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie2;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author t77yq @2014-07-23.
 */
public final class HttpUtils {

	private HttpUtils() {

	}

	public static RequestQueue newRequestQueue(Context context) {
		if (context instanceof Activity) {
			context = ((Activity) context).getApplication();
		}
		return App.newRequestQueue(context);
	}

	public static RequestQueue newRequestQueue(Context context, String cookie) {
		DefaultHttpClient mHttpClient = new DefaultHttpClient();
		CookieStore cs = mHttpClient.getCookieStore();
		cs.addCookie(new BasicClientCookie2("Cookie", cookie));

		return Volley
				.newRequestQueue(context, new HttpClientStack(mHttpClient));
	}

	public static void newRequestPost(Context context, String url,
			Map<String, String> map, Listener<String> listener,
			ErrorListener errorListener) {
		Request<String> request = new RequestHeader(Request.Method.POST, url,
				map, listener, errorListener);
		newRequestQueue(context).add(request);
	}

	public static void newRequestGet(Context context, String url,
			Map<String, String> map, Listener<String> listener,
			ErrorListener errorListener) {
		Request<String> request = new RequestHeader(Request.Method.GET, url,
				map, listener, errorListener);
		newRequestQueue(context).add(request);
	}

	public static void newRequestJsonObjectPost(Context context, String url,
			JSONObject jsonRequest, Listener<JSONObject> listener,
			ErrorListener errorListener) {
		Request<JSONObject> request = new RequestJsonHeader(
				Request.Method.POST, url, jsonRequest, listener, errorListener);
		RequestQueue requestQueue = newRequestQueue(context);
		requestQueue.add(request);
	}

	public static void newRequestJsonObjectGet(Context context, String url,
			JSONObject jsonRequest, Listener<JSONObject> listener,
			ErrorListener errorListener) {
		Request<JSONObject> request = new RequestJsonHeader(Request.Method.GET,
				url, jsonRequest, listener, errorListener);
		RequestQueue requestQueue = newRequestQueue(context);
		requestQueue.add(request);
	}

	public static void newRequestJsonArrayPost(Context context, String url,
			JSONArray jsonRequest, Listener<JSONArray> listener,
			ErrorListener errorListener) {
		Request<JSONArray> request = new RequestJsonArrayHeader(
				Request.Method.POST, url, jsonRequest, listener, errorListener);
		newRequestQueue(context).add(request);
	}

	public static void newRequestJsonArrayGet(Context context, String url,
			JSONArray jsonRequest, Listener<JSONArray> listener,
			ErrorListener errorListener) {
		Request<JSONArray> request = new RequestJsonArrayHeader(
				Request.Method.GET, url, jsonRequest, listener, errorListener);
		newRequestQueue(context).add(request);
	}

	/**
	 * GET
	 * 
	 * @param url
	 * @param params
	 * @param callBack
	 * @param what
	 * @param type
	 */
	public static <T> void requestGet(String url, Map<String, String> params,
			final IDataCallBack callBack, final int what, final Type type) {
		if (NetworkTool.NetworkType(App.getContext()) < 0) {
			callBack.handleHttpResultErr(HttpResultTool.HTTP_NO_NETWORK, what,
					"网络异常");
			return;
		}
		if (TextUtils.isEmpty(url)) {
			callBack.handleHttpResultErr(HttpResultTool.HTTP_URL_NULL, what,
					"Url错误");
			return;
		}
		// 组装log
		StringBuilder sb_log = new StringBuilder(url);
		if (params != null && params.size() > 0) {
			sb_log.append("?");
			for (String key : params.keySet()) {
				sb_log.append(key);
				sb_log.append("=");
				sb_log.append(params.get(key));
				sb_log.append("&");
			}
			sb_log = new StringBuilder(sb_log.subSequence(0,
					sb_log.length() - 1));
		} else {
			params = new HashMap<String, String>();
		}
		newRequestGet(App.getContext(), sb_log.toString(),
				new HashMap<String, String>(), new Listener<String>() {

					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						if (!checkCentreStates(response, callBack, what)) {
							return;
						}
						System.out.println(" 接口返回  ：" + response);
						try {
							List<T> list = GsonUtil.toList(response, type);
							if (list != null) {
								callBack.handleHttpResultSucc(
										HttpResultTool.HTTP_OK, what, list);
								return;
							}
						} catch (JsonSyntaxException e) {
							e.printStackTrace();
						}
						callBack.handleHttpResultErr(
								HttpResultTool.HTTP_JSON_ERROR, what, "json错误");
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
						callBack.handleHttpResultErr(HttpResultTool.HTTP_ERROR,
								what, "服务器异常");
					}
				});
	}

	/**
	 * GET
	 * 
	 * @param url
	 * @param params
	 * @param callBack
	 * @param what
	 * @param type
	 */
	public static <T> void requestGet(String url, Map<String, String> params,
			final IDataCallBack callBack, final int what, final Class<T> clz) {
		if (NetworkTool.NetworkType(App.getContext()) < 0) {
			callBack.handleHttpResultErr(HttpResultTool.HTTP_NO_NETWORK, what,
					"网络异常");
			return;
		}
		if (TextUtils.isEmpty(url)) {
			callBack.handleHttpResultErr(HttpResultTool.HTTP_URL_NULL, what,
					"Url错误");
			return;
		}
		// 组装log
		StringBuilder sb_log = new StringBuilder(url);
		if (params != null && params.size() > 0) {
			sb_log.append("?");
			for (String key : params.keySet()) {
				sb_log.append(key);
				sb_log.append("=");
				sb_log.append(params.get(key));
				sb_log.append("&");
			}
			sb_log = new StringBuilder(sb_log.subSequence(0,
					sb_log.length() - 1));
		} else {
			params = new HashMap<String, String>();
		}
		newRequestGet(App.getContext(), sb_log.toString(),
				new HashMap<String, String>(), new Listener<String>() {

					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						if (!checkCentreStates(response, callBack, what)) {
							return;
						}
						System.out.println(" 接口返回  ：" + response);
						try {
							T domain = GsonUtil.toDomain(response, clz);
							if (domain != null) {
								callBack.handleHttpResultSucc(
										HttpResultTool.HTTP_OK, what, domain);
								return;
							}
						} catch (JsonSyntaxException e) {
							e.printStackTrace();
						}
						callBack.handleHttpResultErr(
								HttpResultTool.HTTP_JSON_ERROR, what, "json错误");
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
						callBack.handleHttpResultErr(HttpResultTool.HTTP_ERROR,
								what, "服务器异常");
					}
				});
	}

	/**
	 * get String N_ITEM1 =
	 * "http://www.jiangkou.gov.cn/public/article?itemno=%d&page=%d&maxResult=%d"
	 * ; String requestUrl = String.format(NewsItemFetcher.N_ITEM1,itemNo,page,
	 * maxResult);
	 * 
	 * @param requestUrl
	 */
	public void requestGet(String url, final IDataCallBack callBack,
			final int what, final Type type) {
		if (NetworkTool.NetworkType(App.getInstance()) < 0) {
			callBack.handleHttpResultErr(HttpResultTool.HTTP_NO_NETWORK, what,
					"网络异常");
			return;
		}
		if (TextUtils.isEmpty(url)) {
			callBack.handleHttpResultErr(HttpResultTool.HTTP_URL_NULL, what,
					"Url错误");
			return;
		}
		newRequestQueue(App.getContext()).add(
				new StringRequest(Request.Method.GET, url,
						new Response.Listener<String>() {
							@Override
							public void onResponse(String response) {
								if (!checkCentreStates(response, callBack, what)) {
									return;
								}
								try {
									List<Object> list = GsonUtil.toList(
											response, type);
									if (list != null) {
										callBack.handleHttpResultSucc(
												HttpResultTool.HTTP_OK, what,
												list);
										return;
									}
								} catch (JsonSyntaxException e) {
									e.printStackTrace();
								}

								callBack.handleHttpResultErr(
										HttpResultTool.HTTP_JSON_ERROR, what,
										"json错误");
							}
						}, new Response.ErrorListener() {
							@Override
							public void onErrorResponse(VolleyError error) {
								callBack.handleHttpResultErr(
										HttpResultTool.HTTP_ERROR, what,
										"服务器异常");
							}
						}));
	}

	public static <T> void requestPost(String url, Map<String, String> params,
			final IDataCallBack callBack, final int what, final Type type) {
		if (NetworkTool.NetworkType(App.getInstance()) < 0) {
			callBack.handleHttpResultErr(HttpResultTool.HTTP_NO_NETWORK, what,
					"网络异常");
			return;
		}
		if (TextUtils.isEmpty(url)) {
			callBack.handleHttpResultErr(HttpResultTool.HTTP_URL_NULL, what,
					"Url错误");
			return;
		}
		// 组装log
		StringBuilder sb_log = new StringBuilder(url);
		if (params != null && params.size() > 0) {
			sb_log.append("?");
			for (String key : params.keySet()) {
				sb_log.append(key);
				sb_log.append("=");
				sb_log.append(params.get(key));
				sb_log.append("&");
			}
			sb_log = new StringBuilder(sb_log.subSequence(0,
					sb_log.length() - 1));
		} else {
			params = new HashMap<String, String>();
		}
		newRequestPost(App.getInstance(), url, params, new Listener<String>() {

			@Override
			public void onResponse(String response) {
				// TODO Auto-generated method stub
				if (!checkCentreStates(response, callBack, what)) {
					return;
				}
				try {
					List<Object> list = GsonUtil.toList(response, type);
					if (list != null) {
						callBack.handleHttpResultSucc(HttpResultTool.HTTP_OK,
								what, list);
						return;
					}
				} catch (JsonSyntaxException e) {
					e.printStackTrace();
				}

				callBack.handleHttpResultErr(HttpResultTool.HTTP_JSON_ERROR,
						what, "json错误");
			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				callBack.handleHttpResultErr(HttpResultTool.HTTP_ERROR, what,
						"服务器异常");
			}
		});
	}

	public static boolean checkCentreStates(String json,
			IDataCallBack callBack, int what) {
		try {
			if (TextUtils.isEmpty(json)) {
				callBack.handleHttpResultErr(HttpResultTool.HTTP_JSON_ERROR,
						what, "json错误");
				return false;
			}
			JSONArray jsonArray = new JSONArray(json);
			System.err.print("jsonArray" + jsonArray);
			if (jsonArray.length() <= 0 && jsonArray.isNull(0)) {
				callBack.handleHttpResultErr(HttpResultTool.HTTP_JSON_ERROR,
						what, "json错误");
				return false;
			}
		} catch (JSONException e) {

		}

		return true;
	}

	public static boolean checkCentreState(String json, IDataCallBack callBack,
			int what) {
		try {
			if (TextUtils.isEmpty(json)) {
				callBack.handleHttpResultErr(HttpResultTool.HTTP_JSON_ERROR,
						what, "json错误");
				return false;
			}
			JSONObject jsonObject = new JSONObject(json);
			int status = (Integer) jsonObject.get("sys_status");
			String info = (String) jsonObject.get("info");
			System.err.print("平台status:" + status);
			if (status != 1) {
				callBack.handleHttpResultErr(HttpResultTool.HTTP_ERROR, what,
						info);
				return false;
			}
		} catch (JSONException e) {

		}

		return true;
	}
}
