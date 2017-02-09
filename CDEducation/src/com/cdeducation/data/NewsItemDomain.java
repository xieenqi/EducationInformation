package com.cdeducation.data;

import java.util.List;

import com.google.gson.annotations.Expose;

public class NewsItemDomain extends BaseDomain {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Expose
	public int total;
	@Expose
	public List<NewsItems> rows;
}
