package com.libray.tools;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期操作工具类.
 * 
 */

@SuppressLint("SimpleDateFormat") public class DateUtil
{
  public static final long FEN = 6 * 10 * 1000;
  private static final String FORMAT3 = "yyyy-MM-dd";
  @SuppressWarnings("unused")
private static final String FORMAT2 = "yyyy-MM-dd HH:mm";
  private static final String FORMAT = "yyyy-MM-dd HH:mm:ss";
  @SuppressWarnings("unused")
private static final String FORMAT_UTC = "yyyy-MM-dd 'T' HH:mm:ss";

  public static Date str2Date(String str)
  {
    return str2Date(str, null);
  }

  /**
   * 格式化日期为字符串
   * 
   * @param date
   *        要格式化为字符串的Date实例
   * @return yyyy-MM-dd
   * **/
  @SuppressLint("SimpleDateFormat")
  public static String getFormateTimeDate(Date date)
  {
    try
    {
      SimpleDateFormat format = new SimpleDateFormat(FORMAT3);
      return format.format(date);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return "";
  }

  /**
   * 将字符串转换为日期
   * 
   * @param str
   *        要转换的字符串
   * @return date实例
   * **/
  @SuppressLint("SimpleDateFormat")
  public static Date getUtcDate(String str)
  {
    Date date = null;
    SimpleDateFormat format = new SimpleDateFormat(FORMAT);
    try
    {
      date = format.parse(str);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return date;
  }

  public static Date str2Date(String str, String format)
  {
    if (str == null || str.length() == 0)
    {
      return null;
    }
    if (format == null || format.length() == 0)
    {
      format = FORMAT;
    }
    Date date = null;
    try
    {
      SimpleDateFormat sdf = new SimpleDateFormat(format);
      date = sdf.parse(str);

    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return date;

  }

  public static Calendar str2Calendar(String str)
  {
    return str2Calendar(str, null);

  }

  public static Calendar str2Calendar(String str, String format)
  {

    Date date = str2Date(str, format);
    if (date == null)
    {
      return null;
    }
    Calendar c = Calendar.getInstance();
    c.setTime(date);

    return c;

  }

  public static String date2Str(Calendar c)
  {// yyyy-MM-dd HH:mm:ss
    return date2Str(c, null);
  }

  public static String date2Str(Calendar c, String format)
  {
    if (c == null)
    {
      return null;
    }
    return date2Str(c.getTime(), format);
  }

  public static String date2Str(Date d)
  {// yyyy-MM-dd HH:mm:ss
    return date2Str(d, null);
  }

  @SuppressLint("SimpleDateFormat") public static String date2Str(Date d, String format)
  {// yyyy-MM-dd HH:mm:ss
    if (d == null)
    {
      return null;
    }
    if (format == null || format.length() == 0)
    {
      format = FORMAT;
    }
    SimpleDateFormat sdf = new SimpleDateFormat(format);
    String s = sdf.format(d);
    return s;
  }

  public static String getCurDateStr()
  {
    Calendar c = Calendar.getInstance();
    c.setTime(new Date());
    return c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-"
        + c.get(Calendar.DAY_OF_MONTH) + " " + c.get(Calendar.HOUR_OF_DAY) + ":"
        + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND);
  }

  /**
   * 获得当前日期的字符串格式
   * 
   * @param format
   * @return
   */
  public static String getCurDateStr(String format)
  {
    Calendar c = Calendar.getInstance();
    return date2Str(c, format);
  }

  // 格式到秒
  public static String getMillon(long time)
  {

    return new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(time);

  }

  // 格式到天
  public static String getDay(long time)
  {

    return new SimpleDateFormat("yyyy-MM-dd").format(time);

  }

  // 格式到毫秒
  public static String getSMillon(long time)
  {

    return new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS").format(time);

  }

  /**
   * 将日期字符串转换为毫秒
   * 
   * @param str
   *        要转换的字符串
   * @return date实例
   * 
   * **/
  @SuppressLint("SimpleDateFormat")
  public static long getMillis(String str)
  {
    Date date = null;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    try
    {
      date = format.parse(str);
      if (date != null)
      {
        return date.getTime();
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return 0;
  }
}
