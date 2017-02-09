package com.cdeducation.data;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;

@SuppressWarnings("serial")
public class NewsItems extends BaseDomain {
	@Expose
	public int Id;
	@Expose
	public String Title;
	@Expose
	public String GetdateTime;

	public String MenuId;

	public static Type getType() {
		return new TypeToken<List<NewsItems>>() {
		}.getType();
	}
}
