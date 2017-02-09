package com.cdeducation.fragment;

import com.cdeducation.R;
import com.libray.util.*;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

public class AddPhotoFragment extends DialogFragment
{

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState)
  {
    Dialog dialog = new Dialog(getActivity(), R.style.dialogBottom);
    dialog.setContentView(R.layout.fragment_addphoto);
    Button take_picture_buttton =
        (Button) dialog.findViewById(R.id.take_picture_buttton);
    take_picture_buttton.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        ImageUtils.openCameraImageUri(getActivity());
        dismiss();
      }
    });
    Button photo_buttton = (Button) dialog.findViewById(R.id.photo_buttton);
    photo_buttton.setOnClickListener(new OnClickListener()
    {
          @Override
          public void onClick(View v)
          {
            ImageUtils.openLocalImage(getActivity());
            dismiss();
          }
        });
    Button cancel_buttton = (Button) dialog.findViewById(R.id.me_addPhoto_cancel_buttton);
    cancel_buttton.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        dismiss();
      }
    });
    RelativeLayout layout = (RelativeLayout) dialog.findViewById(R.id.me_addPhoto_menu_layout);
    layout.setOnClickListener(new OnClickListener()
    {

      @Override
      public void onClick(View v)
      {
        dismiss();
      }
    });
    return dialog;
  }
}
