package com.cdeducation.fragment;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.cdeducation.App;
import com.cdeducation.NewsActivity;
import com.cdeducation.R;
import com.cdeducation.adapter.NewsItemsAdapter;
import com.cdeducation.data.NewsItemDomain;
import com.cdeducation.data.NewsItems;
import com.cdeducation.model.NewsItemsModel;
import com.cdeducation.service.NewsItemFetcher;
import com.cdeducation.util.HttpUtils;
import com.libray.util.ViewFinder;

/**
 * 搜索界面
 * 
 * @author zhaolin 2015-2-12
 * @Desc:
 */

public class SearchFragment extends BaseListFragment {
	EditText search_input;
	View search_bt;
	NewsItemsAdapter adapter;
	NewsItemsModel model;
	Button bt_type;
	Button bt_startTime;
	Button bt_endTime;
	String startTime = "", endTime = "", type = "";
	PopupWindow pu;
	private final static int HTTP_SEARCH_LIST = 137;
	String keywords;
	Calendar c;

	Handler handler = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {
			super.dispatchMessage(msg);
			if (msg.what == 2) {
				bt_startTime.setText("不限");
				bt_endTime.setText("不限");
				startTime = "";
				endTime = "";
			}
		}

	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_search, container, false);
		initView(rootView);
		initListener();
		setLoading(false, false);
		final LinearLayout menu_left = (LinearLayout) rootView.findViewById(R.id.menu_left);
		menu_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				getActivity().finish();
			}
		});
		c = Calendar.getInstance();
		// endTime=(c.get(Calendar.MONTH)+1)+"/"+c.get(Calendar.DAY_OF_MONTH)+"/"+c.get(Calendar.YEAR);
		return rootView;

	}

	@Override
	public void fragmentVisibleHint(boolean isVisibleToUser) {
		// TODO Auto-generated method stub

	}

	public void initView(View root) {
		initListView();
		search_bt = ViewFinder.getView(root, R.id.search_bt);
		search_input = ViewFinder.getView(root, R.id.cet_search);
		bt_type = ViewFinder.getView(root, R.id.bt_type);
		bt_startTime = ViewFinder.getView(root, R.id.bt_startTime);
		bt_endTime = ViewFinder.getView(root, R.id.bt_endTime);
		model = new NewsItemsModel();
		adapter = new NewsItemsAdapter(getActivity(), model);
		actualListView.setAdapter(adapter);
		setModelDisabled();
		closePullDownRefresh();
		search_input.addTextChangedListener(watcher);
	}

	private TextWatcher watcher=new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
			if(s.length()==0){
				adapter.cleanData();
			}
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}
		
		@Override
		public void afterTextChanged(Editable s) {
		}
	};
	
	public void initListener() {
		search_bt.setOnClickListener(click);
		bt_type.setOnClickListener(click);
		bt_startTime.setOnClickListener(click);
		bt_endTime.setOnClickListener(click);

		actualListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				NewsItems pptItem = model.getNItem(position - 1);
				if (pptItem == null)
					return;
				Intent intent = new Intent(getActivity(), NewsActivity.class);
				intent.putExtra(NewsDetialFragment.ARG_DATA, pptItem);
				intent.putExtra(NewsDetialFragment.ARG_FRAGS, 1);
				startActivity(intent);
				getActivity().overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
			}
		});
	}

	private OnClickListener click = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.search_bt:
				closeInput(search_input);
				loadNewData();
				break;
			case R.id.bt_type:
				popuSelectType(bt_type);
				break;
			case R.id.bt_startTime:
				selectTime(bt_startTime, 1);
				break;
			case R.id.bt_endTime:
				selectTime(bt_endTime, 2);
				break;
			case R.id.type1:
				bt_type.setText("按【Id号】搜索");
				type = "Id";
				pu.dismiss();
				break;
			case R.id.type2:
				bt_type.setText("按【标题】搜索");
				type = "Title";
				pu.dismiss();
				break;
			case R.id.type3:
				bt_type.setText("按【作者】搜索");
				type = "Author";
				pu.dismiss();
				break;
			case R.id.type4:
				bt_type.setText("按【来源】搜索");
				type = "Source";
				pu.dismiss();
				break;
			case R.id.type5:
				bt_type.setText("按【内容】搜索");
				type = "ContextApp";
				pu.dismiss();
				break;
			case R.id.type6:
				bt_type.setText("搜 索 类 型");
				type = "";
				pu.dismiss();
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 搜索类型选择
	 * 
	 * @param but
	 */
	private void popuSelectType(Button but) {
		View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.popu_type, null);
		pu = new PopupWindow(getActivity());
		pu.setContentView(contentView);
		pu.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.gray_corners_white_bg));
		contentView.findViewById(R.id.type1).setOnClickListener(click);
		contentView.findViewById(R.id.type2).setOnClickListener(click);
		contentView.findViewById(R.id.type3).setOnClickListener(click);
		contentView.findViewById(R.id.type4).setOnClickListener(click);
		contentView.findViewById(R.id.type5).setOnClickListener(click);
		contentView.findViewById(R.id.type6).setOnClickListener(click);
		pu.setWidth(but.getWidth());
		pu.setHeight(but.getHeight() * 7);
		pu.setTouchable(true);
		pu.setFocusable(true);
		pu.showAsDropDown(but, 0, 0);
		pu.update();
	}

	@SuppressWarnings("deprecation")
	private void selectTime(final Button vv, final int type) {

		final DatePickerDialog dataTime = new DatePickerDialog(getActivity(), R.style.MyDialogStyle, new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				vv.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year);
				if (type == 1) {
					startTime = (monthOfYear + 1) + "/" + dayOfMonth + "/" + year;
				} else {
					endTime = (monthOfYear + 1) + "/" + dayOfMonth + "/" + year;
				}
				if (type == 1) {
					vv.setText(startTime);
				} else {
					vv.setText(endTime);
				}
			}
		}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
		dataTime.setTitle("");
//		dataTime.setButton("确定", new DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				DatePicker datePicker = dataTime.getDatePicker();
//				if (type == 1) {
//					startTime = (datePicker.getMonth() + 1) + "/" + datePicker.getDayOfMonth() + "/" + datePicker.getYear();
//				} else {
//					endTime = (datePicker.getMonth() + 1) + "/" + datePicker.getDayOfMonth() + "/" + datePicker.getYear();
//				}
//				
//			}
//		});
		dataTime.setButton2("不限", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				handler.sendEmptyMessage(2);

			}
		});
		dataTime.show();
	}

	private void searchData() {
		keywords = search_input.getText().toString();
		if (TextUtils.isEmpty(keywords)) {
			showToast("请输入搜索内容");
			return;
		}
		if (!TextUtils.isEmpty(startTime)) {
			if (TextUtils.isEmpty(endTime)) {
				showToast("请选择结束时间");
				return;
			}
		}
		if (!TextUtils.isEmpty(endTime)) {
			if (TextUtils.isEmpty(startTime)) {
				showToast("请选择开始时间");
				return;
			}
		}
		if (model.size() == 0)
			setLoading(true, false);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("keywords", keywords);
		params.put("SearchType", type);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		params.put("page", page_curr + "");
		params.put("pageRows", page_rows + "");
		// params.put("MenuId", "");

		HttpUtils.requestGet(NewsItemFetcher.NEWS_PAGE_LIST, params, this, HTTP_SEARCH_LIST, NewsItemDomain.class);
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void updateData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleHttpResultSucc(int succCode, int what, Object data) {
		// TODO Auto-generated method stub
		onRefreshComplete();
		switch (what) {
		case HTTP_SEARCH_LIST:
			if (data != null) {
				NewsItemDomain domain = (NewsItemDomain) data;
				if (data != null && domain != null) {
					List<NewsItems> nItems = domain.rows;
					if (page_curr == 1) {
						setLoading(false, true);
						model.createNItems(nItems);
					} else {
						model.setNItem(nItems);
					}
					page_curr++;
				} else {
					setLoading(false, false);
				}
				if (domain != null && model.size() == domain.total) {
					setModelDisabled();
				}
			} else {
				setLoading(false, false);
				setNoData("没有数据");
			}
			break;
		}
	}

	@Override
	public void handleHttpResultErr(int errCode, int what, Object data) {
		// TODO Auto-generated method stub
		setLoading(false, false);
		onRefreshComplete();
		setNoData("没有数据");
	}

	@Override
	protected void loadMoreData() {
		// TODO Auto-generated method stub
		searchData();
	}

	@Override
	protected void loadNewData() {
		// TODO Auto-generated method stub
		page_curr = 1;
		searchData();
	}
}
