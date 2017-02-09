package com.cdeducation.service;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.cdeducation.App;
import com.cdeducation.config.Commons;

public class RequestJsonArrayHeader extends JsonRequest<JSONArray> {

	public RequestJsonArrayHeader(int method, String url, JSONArray jsonArray, Listener<JSONArray> listener, ErrorListener errorListener) {
		super(method, url, (jsonArray == null) ? null : jsonArray.toString(), listener, errorListener);
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

	@Override
	protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
		try {
            String jsonString =
                new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONArray(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
		
	}

}
