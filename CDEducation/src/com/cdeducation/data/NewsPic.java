package com.cdeducation.data;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;


@SuppressWarnings("serial")
public class NewsPic extends BaseDomain {
	@Expose
	public String RecommendTitle;
	@Expose
	public String RecommendURL;
	@Expose
	public String RecommendImg;

	public NewsPic() {

	}

	public static Type getType() {
		return new TypeToken<List<NewsPic>>() {
		}.getType();
	}
}
