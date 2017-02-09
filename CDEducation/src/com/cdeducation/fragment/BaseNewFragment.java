package com.cdeducation.fragment;

import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.cdeducation.App;
import com.cdeducation.NewsActivity;
import com.cdeducation.NewsListActivity;
import com.cdeducation.R;
import com.cdeducation.config.Commons;
import com.cdeducation.data.NewsItems;
import com.cdeducation.data.UserInfo;
import com.cdeducation.service.IDataCallBack;
import com.cdeducation.service.NewsItemFetcher;
import com.cdeducation.service.NewsMenuId;
import com.cdeducation.util.HttpUtils;
import com.libray.tools.JsonUtil;
import com.libray.util.*;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public abstract class BaseNewFragment extends Fragment implements IDataCallBack {
	View rootView;
	private ProgressDialog pd;
	public static final int REQUET_INTENT_LOGIN = 10001;
	public static final int REQUET_INTENT_REGISTER = 10002;
	public static final int REQUET_INTENT_USER_EDIR = 10003;
	public static final int REQUET_INTENT_FINDPASS = 10004;
	public static final int REQUET_INTENT_FIXPASS = 10005;
	public static final int REQUET_INTENT_SELECT_ADDRESS = 10006;
	public static final int REQUET_INTENT_EDIT_NAME = 10007;
	int http_count = 0;

	public void intentNewsList(String menuid, String title) {
		Intent intent = new Intent(getActivity(), NewsListActivity.class);
		intent.putExtra(MainNoticFragment.KEY_MENUID, menuid);
		intent.putExtra(MainNoticFragment.KEY_ISACTIVITY, true);
		intent.putExtra(MainNoticFragment.KEY_TITLE, title);

		startActivity(intent);
		getActivity().overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
	}

	public void initMenuData(List<NewsItems> list, LinearLayout layout, final String menuId) {
		if (list == null || list.size() == 0 || layout == null)
			return;
		layout.removeAllViews();
		for (NewsItems news : list) {
			if (news != null) {
				View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_newsitem_item, null);
				TextView tv_title = ViewFinder.getView(view, R.id.tv_title);
				TextView tv_date = ViewFinder.getView(view, R.id.tv_date);
				tv_title.setText(news.Title);
				tv_date.setText(news.GetdateTime);
				view.setTag(news);
				view.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						NewsItems newsItems = (NewsItems) v.getTag();
						Intent intent = new Intent(getActivity(), NewsActivity.class);
						intent.putExtra(NewsDetialFragment.ARG_DATA, newsItems);
						intent.putExtra(NewsDetialFragment.ARG_FRAGS, 1);
						intent.putExtra(NewsDetialFragment.ARG_MENUID, menuId);
						startActivity(intent);
						getActivity().overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
					}
				});
				layout.addView(view);
			}
		}

	}

	public void getListData(String menuid, int http_type) {
		http_count++;
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("MenuId", menuid + "");
		params.put("TopNum", "3");
		params.put("IsRecommend", "0");
		HttpUtils.requestGet(NewsItemFetcher.NEWS_INFO_LIST, params, this, http_type, NewsItems.getType());
	}

	public void showDialog() {
		if (pd == null) {
			pd = new ProgressDialog(getActivity());
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		}
		pd.setMessage(this.getString(R.string.pull_to_refresh_refreshing_label));
		if (!pd.isShowing()) {
			pd.show();
		}
	}

	public void dismissDialog() {
		if (pd != null && pd.isShowing()) {
			pd.dismiss();
		}
	}

	public void showToast(String text) {
		Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 关闭键盘事件
	 * 
	 * @author
	 * @update
	 */
	public void closeInput(View view) {
		if (view != null) {
			InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
			if (inputMethodManager != null) {
				inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}
	}

	/**
	 * 关闭键盘事件
	 * 
	 * @author
	 * @update
	 */
	public void closeInput(Activity context) {
		InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (inputMethodManager != null && context.getCurrentFocus() != null) {
			inputMethodManager.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	public void upload(String type, String data) {
		try {
			update(type, data);
		} catch (JSONException e) {
			e.printStackTrace();
			showToast("更新失败");
		}
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		fragmentVisibleHint(isVisibleToUser);
		super.setUserVisibleHint(isVisibleToUser);
	}

	public abstract void fragmentVisibleHint(boolean isVisibleToUser);

	public void swichType(String type, String data) {
		if (!"sex".equals(type) && !"icon".equals(type) && !"area".equals(type)) {
			Intent intent = getActivity().getIntent();
			intent.putExtra("type", type);
			intent.putExtra("data", data);
			getActivity().setResult(Activity.RESULT_OK, intent);
			getActivity().finish();
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		HttpUtils.newRequestQueue(getActivity()).cancelAll(new Object());
	}

	/**
	 * 利用Volley异步加载图片
	 * 
	 * 注意方法参数: getImageListener(ImageView view, int defaultImageResId, int
	 * errorImageResId) 第一个参数:显示图片的ImageView 第二个参数:默认显示的图片资源 第三个参数:加载错误时显示的图片资源
	 */
	public void loadImageByVolley(String url, ImageView view) {
		ImageLoader imageLoader = new ImageLoader(HttpUtils.newRequestQueue(getActivity()), BitmapLruCache.getInstance());
		ImageListener listener = ImageLoader.getImageListener(view, R.drawable.login_avatar, R.drawable.login_avatar);
		imageLoader.get(url, listener);
	}

	public abstract void updateData();

	public void getUserInfo() {
		Commons.IS_SET_AUTHORIZATION = true;
		HttpUtils.newRequestJsonArrayPost(getActivity(), NewsItemFetcher.USR_INFO, new JSONArray(), new Response.Listener<JSONArray>() {

			@Override
			public void onResponse(JSONArray response) {
				// TODO Auto-generated method stub
				try {

					UserInfo userInfo = JsonUtil.toBeanNoName(response.toString(), UserInfo.class);
					if (userInfo != null) {
						App.saveUserInfo(userInfo);
						updateData();
					}
				} catch (Exception e) {
					e.printStackTrace();
					showToast("服务器异常");
				}
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				showToast("服务器异常");
			}
		});
	}

	/**
	 * parm:type 修改个人信息的类别：names昵称,icon头像,mobile手机号,sex性别,area地区 parm:data
	 * 修改个人信息数据，如果type=icon,data使用bas64将图片打成字符串
	 * 
	 * @throws JSONException
	 */
	public void update(final String type, final String data) throws JSONException {
		showDialog();
		Commons.IS_SET_AUTHORIZATION = true;
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("type", type);
		jsonObject.put("data", data);
		HttpUtils.newRequestJsonObjectPost(getActivity(), NewsItemFetcher.USER_UPDATE, jsonObject, new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				dismissDialog();
				try {
					int result = JsonUtil.toBeanInt(response.toString());
					if (result == 0) {
						showToast("更新成功");
						swichType(type, data);
					} else if (result == 1) {
						showToast("更新错误");
					} else if (result == 2) {
						showToast("更新类别错误");
					} else if (result == 3) {
						showToast("你还没有登录");
					} else {
						showToast("更新失败");
					}
				} catch (Exception e) {
					e.printStackTrace();
					showToast("更新失败");
				}
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				dismissDialog();
				showToast("更新失败");
			}
		});
	}
}
