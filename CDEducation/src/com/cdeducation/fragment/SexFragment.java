package com.cdeducation.fragment;


import com.cdeducation.R;
import com.libray.util.ViewTool;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

public class SexFragment 
{
  private OnUpadteListener listener = null;

  public void newInstance(Context context)
  {
    SexDialog instance = new SexDialog(context, R.style.MyDialogStyle);
    instance.show();
  }

  public interface OnUpadteListener
  {

    public void OnUpadte(int type);

  }

  public void setOnUpadteListener(OnUpadteListener onUpadteListener)
  {
    listener = onUpadteListener;
  }

  public class SexDialog extends Dialog
  {
    public SexDialog(Context context, int theme)
    {
      super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.fragment_dialog_sex);
      ViewTool.windowDeploy(0, 0, getWindow());
      getWindow().setGravity(Gravity.CENTER);
      setCanceledOnTouchOutside(true);
      ImageView male_layout = (ImageView) findViewById(R.id.me_myinfo_sex_male_layout);
      male_layout.setOnClickListener(onClickListener);
      ImageView female_layout = (ImageView) findViewById(R.id.me_myinfo_sex_female_layout);
      female_layout.setOnClickListener(onClickListener);
    }

    private void onUpadteText(int sexType)
    {
      if (sexType == 1)
      {
        listener.OnUpadte(sexType);
      }
      else if (sexType == 2)
      {
        listener.OnUpadte(sexType);
      }
      if (isShowing())
      {
        dismiss();
      }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener()
    {

      @Override
      public void onClick(View v)
      {
        switch (v.getId())
        {
          case R.id.me_myinfo_sex_male_layout:
            onUpadteText(1);
            break;

          case R.id.me_myinfo_sex_female_layout:
            onUpadteText(2);
            break;
        }
      }
    };
  }
}
