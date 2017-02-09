package com.cdeducation.fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.cdeducation.R;
import com.libray.util.ViewFinder;

/**
 * @author t77yq @2014-07-28.
 */
public class FeedBackFragment extends BaseFragment {

	EditText editText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View root=inflater.inflate(R.layout.fragment_feed_back, container, false);
    	 editText=ViewFinder.getView(root, R.id.editText);
    	return root;
    }
    public String getContent(){
    	if(editText!=null){
    		return editText.getText().toString();
    	}
    	return "";
    }
	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
       
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
	@Override
	public void updateData() {
		// TODO Auto-generated method stub
		
	}
}
