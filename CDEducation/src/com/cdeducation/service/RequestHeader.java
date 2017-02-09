package com.cdeducation.service;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;

public class RequestHeader extends StringRequest {
	private Map<String, String> mMap;
	private Listener<String> mListener;

	public RequestHeader(int method, String url, Map<String, String> map,
			Listener<String> listener, ErrorListener errorListener) {
		super(method, url, listener, errorListener);
		mListener = listener;
		mMap = map;
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		// TODO Auto-generated method stub
//		if (mMap != null) {
//			if (Commons.IS_SET_AUTHORIZATION) {
//				Commons.IS_SET_AUTHORIZATION = false;
//				String email = App.getInstance().getSPEmail();
//				String pass = App.getInstance().getSPPass();
//				mMap.put("Authorization", email + ":" + pass);
//			}
//			mMap.put("Content-Type",
//					"application/x-www-form-urlencoded; charset=UTF-8");
//		}
//		return mMap;
		HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json; charset=UTF-8");
        return headers;
	}
	@Override
	protected Map<String, String> getParams() throws AuthFailureError {
		// TODO Auto-generated method stub
		return super.getParams();
	}

	@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response) {
		String jsonString = null;
		try {
			jsonString = new String(response.data,
					HttpHeaderParser.parseCharset(response.headers));

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Response.success(jsonString,
				HttpHeaderParser.parseCacheHeaders(response));

	}

	@Override
	protected void deliverResponse(String response) {
		// TODO Auto-generated method stub
		mListener.onResponse(response);
	}

}
