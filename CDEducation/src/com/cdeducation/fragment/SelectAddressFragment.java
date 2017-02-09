package com.cdeducation.fragment;

import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cdeducation.R;
import com.cdeducation.data.AreaInfo;
import com.cdeducation.model.AreaManager;
import com.libray.util.ViewFinder;

/**
 * 选择地址
 * 
 * @author zhaolin 2015-2-12
 * @Desc:
 */
public class SelectAddressFragment extends BaseFragment {
	ListView listView = null;
	List<AreaInfo> list = null;
	AddressAdapter adapter;
	OnItemListener onItemClickListener;
	public interface OnItemListener {

		public void onItemClick(AreaInfo areaInfo);
	}
    public void setOnItemListener(OnItemListener onItemClickListener){
    	this.onItemClickListener=onItemClickListener;
    }
    public AddressAdapter getAdapter(){
    	return adapter;
    }
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_select_address, container,
				false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		listView = ViewFinder.getView(view, R.id.listView);
		adapter=new AddressAdapter();
		listView.setAdapter(adapter);
	}
    public void initProvinceList(){
    	list= AreaManager.getInstance(getActivity()).getProvinceList();
    	if(adapter!=null)
    	adapter.notifyDataSetChanged();
    }
    public void initCityList(int provinceCode){
    	list= AreaManager.getInstance(getActivity()).getCityList(provinceCode);
    	if(adapter!=null)
        	adapter.notifyDataSetChanged();
    }
    public void initDistrictList(int cityCode){
    	list= AreaManager.getInstance(getActivity()).getDistrictList(cityCode);
    	if(adapter!=null)
        	adapter.notifyDataSetChanged();
    }
	class AddressAdapter extends BaseAdapter {
		private LayoutInflater inflater;

		public AddressAdapter() {
			inflater = LayoutInflater.from(getActivity());
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list != null && list.size() > 0 ? list.size() : 0;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list != null && list.size() > 0 ? list.get(position) : null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder = null;
			if (convertView == null || convertView.getTag() == null) {
				convertView = inflater.inflate(
						R.layout.fragment_select_address_item, parent, false);
				holder = new ViewHolder();
				holder.text = ViewFinder.getView(convertView, R.id.text);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			final AreaInfo areaInfo=(AreaInfo) getItem(position);
			holder.text.setText(areaInfo.name);
			convertView.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(onItemClickListener!=null){
						onItemClickListener.onItemClick(areaInfo);
					}
				}
			});
			return convertView;
		}

		class ViewHolder {
			TextView text;
		}
	}
	@Override
	public void updateData() {
		// TODO Auto-generated method stub
		
	}
}
