package com.libray.util;

import android.text.TextUtils;

/**
 * @author t77yq @2014.04.24
 */
public class PicUtils {

  public static String convert(String url, int width, int height) {
    if (!TextUtils.isEmpty(url)&&url.contains(".")) {
      int pos = url.lastIndexOf('.');
      if (pos != -1) {
        return url.substring(0, pos) + "_" + width + "_" + height + url.substring(pos);
      }
    }
    return null;
  }
}
