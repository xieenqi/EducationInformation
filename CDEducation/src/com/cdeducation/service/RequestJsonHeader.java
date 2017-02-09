package com.cdeducation.service;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cdeducation.App;
import com.cdeducation.config.Commons;

public class RequestJsonHeader extends JsonObjectRequest {

	public RequestJsonHeader(int method, String url, JSONObject jsonRequest, Listener<JSONObject> listener, ErrorListener errorListener) {
		super(method, url, jsonRequest, listener, errorListener);
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		Map<String, String> mMap = new HashMap<String, String>();
		if (Commons.IS_SET_AUTHORIZATION) {
			Commons.IS_SET_AUTHORIZATION = false;
			String email = App.getInstance().getSPEmail();
			String pass = App.getInstance().getSPPass();
			mMap.put("Authorization", email + ":" + pass);
		}
		mMap.put("Content-Type", "application/json; charset=UTF-8");
		return mMap;
	}

}
