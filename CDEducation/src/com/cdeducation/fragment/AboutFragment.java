package com.cdeducation.fragment;

import java.util.HashMap;
import java.util.List;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.cdeducation.R;
import com.cdeducation.data.NewsDetil;
import com.cdeducation.service.IDataCallBack;
import com.cdeducation.service.NewsItemFetcher;
import com.cdeducation.service.NewsMenuId;
import com.cdeducation.util.HttpResultTool;
import com.cdeducation.util.HttpUtils;
import com.devspark.progressfragment.ProgressFragment;
import com.libray.util.*;

/**
 * 关于我们 的界面
 * 
 * @author t77yq @2014-07-28.
 */
public class AboutFragment extends ProgressFragment {

	WebView webView;
	View rootView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_about, container, false);
		webView = ViewFinder.getView(rootView, R.id.webview);
		initView();
		return rootView;
	}

	public void initView() {
		if (Build.VERSION.SDK_INT >= 14) {
			webView.shouldDelayChildPressedState();
		}
		webView.setInitialScale(25);// 为25%，最小缩放等级
		webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		WebSettings setting = webView.getSettings();
		setting.setCacheMode(WebSettings.LOAD_NO_CACHE);
		setting.setUseWideViewPort(true);
		setting.setLoadWithOverviewMode(true);
		setting.setJavaScriptEnabled(true);
		setting.setDefaultFontSize(45);
		setting.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);// 自适应屏幕宽
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				setContentShown(false);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				setContentShown(true);
			}

			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);
				setContentEmpty(true);
			}
		});
		get();
	}

	public void get() {
		HashMap<String, String> params = new HashMap<String, String>();
		String url = NewsItemFetcher.NEWS_CONTENT;// 栏目的内容
		params.put("MenuId", NewsMenuId.MENUID_CALL_US + "");
		HttpUtils.requestGet(url, params, new IDataCallBack() {

			@SuppressWarnings("unchecked")
			@Override
			public void handleHttpResultSucc(int succCode, int what, Object data) {
				switch (what) {
				case HttpResultTool.HTTP_LOAD_INIT:
					List<NewsDetil> list = (List<NewsDetil>) data;
					if (list != null && list.size() > 0) {
						NewsDetil sp = list.get(0);
						String context = sp.ContentText;
						if (!TextUtils.isEmpty(context)) {
							webView.getSettings().setDefaultTextEncodingName("UTF-8");
							webView.loadDataWithBaseURL("about:blank", context, "text/html", "utf-8", null);
						} else {
							try {
								setContentShown(true);
								setContentEmpty(true);
							} catch (Exception e) {
							}
						}
						System.out.print("解析的內容  " + context);
					} else {
						try {
							setContentShown(true);
							setContentEmpty(true);
						} catch (Exception e) {
						}
					}
					break;
				}
			}

			@Override
			public void handleHttpResultErr(int errCode, int what, Object data) {
				// TODO Auto-generated method stub
				try {
					setContentShown(true);
					setContentEmpty(true);
				} catch (Exception e) {
				}
			}
		}, HttpResultTool.HTTP_LOAD_INIT, NewsDetil.getType());
	}

	@Override
	public void onDetach() {
		super.onDetach();
		webView.setWebViewClient(null);
	}
}
