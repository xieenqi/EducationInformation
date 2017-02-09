package com.cdeducation.fragment;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.cdeducation.R;
import com.cdeducation.config.Commons;
import com.cdeducation.config.Config;
import com.cdeducation.data.NewsDetil;
import com.cdeducation.data.NewsItems;
import com.cdeducation.data.NewsPic;
import com.cdeducation.service.IDataCallBack;
import com.cdeducation.service.NewsItemFetcher;
import com.cdeducation.util.HttpResultTool;
import com.cdeducation.util.HttpUtils;
import com.cdeducation.util.IntentPage;
import com.devspark.progressfragment.ProgressFragment;
import com.libray.tools.FileTool;
import com.libray.util.ViewFinder;
import com.cdeducation.appservice.FileDownloadService;

/**
 * web 显示列表的详情页面
 * 
 * @author xnq
 */
public class NewsDetialFragment extends ProgressFragment {
	public static final String ARG_DATA_PIC = "data_pic";
	public static final String ARG_MENUID = "menuid";
	public static final String ARG_DATA = "data";
	public static final String ARG_FRAGS = "frags";
	public static final String ARG_TITLE = "title";
	public static final String ARG_URL = "URL";
	private WebView webView;
	private TextView tv_content;
	private TextView tv_download;
	private View ll_download;
	int frags;
	NewsItems newsItems;
	NewsPic newsPic;
	View rootView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_new_file, container,
				false);
		webView = ViewFinder.getView(rootView, R.id.webview);
		tv_content = ViewFinder.getView(rootView, R.id.tv_content);
		tv_download = ViewFinder.getView(rootView, R.id.tv_download);
		ll_download = ViewFinder.getView(rootView, R.id.ll_download);
		initView();
		return rootView;
	}

	public void initView() {
		if (Build.VERSION.SDK_INT >= 14) {
			webView.shouldDelayChildPressedState();
		}
		webView.setInitialScale(39);// 为25%，最小缩放等级
		webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		WebSettings setting = webView.getSettings();
		setting.setCacheMode(WebSettings.LOAD_NO_CACHE);
		setting.setUseWideViewPort(true);
		setting.setLoadWithOverviewMode(true);
		setting.setDefaultFontSize(45);
		setting.setJavaScriptEnabled(true);
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
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);
				setContentEmpty(true);
			}
		});
		switchPage();
	}

	public void switchPage() {
		String id = "0";
		frags = getActivity().getIntent().getIntExtra(ARG_FRAGS, 0);
		newsItems = (NewsItems) getActivity().getIntent().getSerializableExtra(
				ARG_DATA);
		newsPic = (NewsPic) getActivity().getIntent().getSerializableExtra(
				ARG_DATA_PIC);
		boolean isGet = true;
		if (newsPic != null) {
			id = newsPic.RecommendURL;
			if (!TextUtils.isEmpty(id)) {
				id = id.toLowerCase();
			}
			if (!TextUtils.isEmpty(id) && id.contains("http://")) {
				webView.loadUrl(id);
				isGet = false;
				return;
			} else {
				if (TextUtils.isEmpty(id)) {
					isGet = false;
					return;
				}
			}
		} else {
			if (newsItems != null) {
				id = newsItems.Id + "";
				if (!TextUtils.isEmpty(id) && id.equals("0")) {
					isGet = false;
					return;
				} 
			} else {
				String url = getActivity().getIntent().getStringExtra(ARG_URL);
				if (!TextUtils.isEmpty(url)) {
					webView.loadUrl(url);
				}
				isGet = false;
				return;

			}
		}
		if (!isGet) {
			try {
				setContentShown(true);
				setContentEmpty(true);
			} catch (Exception e) {
			}
			return;
		}

		get(id);
	}

	public void get(String id) {
		HashMap<String, String> params = new HashMap<String, String>();
		String url = NewsItemFetcher.NEWS_CONTENT;// 栏目的内容
		if (frags == 1) {
			url = NewsItemFetcher.NEWS_LIST_INFO;
		}
		if (frags == 1) {
			params.put("Id", id);
		} else {
			params.put("MenuId", id);
		}
		HttpUtils.requestGet(url, params, new IDataCallBack() {

			@SuppressWarnings("unchecked")
			@Override
			public void handleHttpResultSucc(int succCode, int what, Object data) {
				switch (what) {
				case HttpResultTool.HTTP_LOAD_INIT:
					List<NewsDetil> list = (List<NewsDetil>) data;
					if (list != null && list.size() > 0) {
						NewsDetil sp = list.get(0);
						setWebViewData(sp);
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
				try {
					setContentShown(true);
					setContentEmpty(true);
				} catch (Exception e) {
				}

			}
		}, HttpResultTool.HTTP_LOAD_INIT, NewsDetil.getType());
	}

	public void setWebViewData(final NewsDetil sp) {
		String context;
		if (frags == 1) {
			context = sp.ContextApp;
		} else {
			context = sp.ContentText;
		}
		String s = "padding:20px";
		if (context.contains(s)) {
			context = context.replace(s, "padding:0px");
		}
		webView.getSettings().setDefaultTextEncodingName("UTF-8");
		webView.loadDataWithBaseURL("about:blank", context, "text/html",
				"utf-8", null);
		String path = sp.AccessoriesFile;
		if (!TextUtils.isEmpty(path)) {
			if (path.contains("\\")) {
				path = path.replace("\\", "/");
			}
			final String url = Config.BASE_URL + path;
			final String filePath = getFilePath(url);
			boolean isExsit = checkApkExsit(filePath);
			if (isExsit) {
				tv_download.setText("点击打开(如打不开,应安装浏览器或相应的软件)");
			}
			tv_content.setText(sp.AccessoriesName);
			ll_download.setVisibility(View.VISIBLE);
			ll_download.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (checkApkExsit(filePath)) {
						Intent intent = IntentPage.getIntent(filePath);
						if (intent != null) {
							startActivity(intent);
						} else {
							Toast.makeText(getActivity(), "此文件无法打开", 1000)
									.show();
						}
					} else {
						FileDownloadService.downNewFile(url, filePath, 1000,
								sp.AccessoriesName);
					}
				}
			});
		}
	}

	ImageGetter imgGetter = new Html.ImageGetter() {
		@Override
		public Drawable getDrawable(String source) {
			Drawable drawable = Drawable.createFromPath(source);
			drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
					drawable.getIntrinsicHeight());
			return drawable;
		}
	};

	/**
	 * @param urlpath
	 * @return Bitmap 根据url获取布局背景的对象
	 */
	public static Drawable getDrawable(String urlpath) {
		Drawable d = null;
		try {
			URL url = new URL(urlpath);
			URLConnection conn = url.openConnection();
			conn.connect();
			InputStream in;
			in = conn.getInputStream();
			d = Drawable.createFromStream(in, "background.jpg");
			// TODO Auto-generated catch block
		} catch (IOException e) {
			e.printStackTrace();
		}
		return d;
	}

	public void setClickText(String text, boolean click) {
		if (ll_download != null && tv_download != null) {
			tv_download.setText(text);
			ll_download.setEnabled(click);
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		webView.setWebViewClient(null);
	}

	/**
	 * @author zhaolin
	 * @Desc: apk 路径
	 * @param apkUrl
	 * @param verName
	 * @return String
	 */
	public String getFilePath(String url) {
		if (!FileTool.isExist(Commons.DOWNLOAD))
			FileTool.mkdirFile(Commons.DOWNLOAD);

		String tempName = FileTool.GetFileName(url);
		if (TextUtils.isEmpty(tempName)) {
			tempName = "" + System.currentTimeMillis();
		}
		return Commons.DOWNLOAD + File.separator + tempName;
	}

	/**
	 * @author zhaolin
	 * @param filePath
	 * @return boolean
	 */
	public boolean checkApkExsit(String filePath) {
		return FileTool.isExist(filePath);
	}

}
