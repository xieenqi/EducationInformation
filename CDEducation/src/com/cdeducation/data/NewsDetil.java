package com.cdeducation.data;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;

@SuppressWarnings("serial")
public class NewsDetil extends BaseDomain {
	/**
	 * ColumnContent.ashx
	 */
	@Expose
	public String ContentText;// 

	/**
	 * InformationGet.ashx
	 */
	@Expose
	public String Title;
	@Expose
	public String ContextApp;
	@Expose
	public String AccessoriesName;
	@Expose
	public String AccessoriesFile;
	@Expose
	public String Author;
	@Expose
	public String Source;
	@Expose
	public String GetdateTime;
	@Expose
	public String index;
	@Expose
	public int Hits;

	public static Type getType() {
		return new TypeToken<List<NewsDetil>>() {
		}.getType();
	}
}
