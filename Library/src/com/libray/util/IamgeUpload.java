package com.libray.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.net.Uri;
import android.provider.MediaStore;

public class IamgeUpload
{
  @SuppressWarnings("deprecation")
  public String getLatestImage(Activity activity)
  {
    String latestImage = null;
    String[] items =
    {
        MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA
    };
    Cursor cursor =
        activity.managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, items, null, null,
            MediaStore.Images.Media._ID + " desc");

    if (cursor != null && cursor.getCount() > 0)
    {
      cursor.moveToFirst();
      for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
      {
        latestImage = cursor.getString(1);
        break;
      }
    }

    return latestImage;
  }

  public void clipImage(final Activity activity, Uri uri, File file)
  {
    final Uri mUri = uri;

    new Thread()
    {
      @Override
      public void run()
      {
        Intent intent = new Intent("com.android.camera.action.CROP");
        // Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.setDataAndType(mUri, "image/*");
        intent.putExtra("crop", "true");// crop=true �������ܳ������Ĳü�ҳ��.
        intent.putExtra("outputFormat", "JPEG");// ���ظ�ʽ
        // intent.putExtra("aspectX", 1);
        // intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 420);
        intent.putExtra("outputY", 420);
        intent.putExtra("return-data", true);
        // intent.putExtra("scale", true);
        // intent.putExtra("noFaceDetection", true);
        activity.startActivityForResult(intent, ImageUtils.CUT_CODE);
      }

    }.start();
  }

  public void saveImage(String path, byte[] attachFile)
  {
    File file = new File(path);
    if (file.exists())
    {
      file.delete();
    }

    try
    {
      file.createNewFile();
      FileOutputStream fos = new FileOutputStream(file);
      fos.write(attachFile);
      fos.close();
    }
    catch (FileNotFoundException e)
    {
      e.printStackTrace();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  public Bitmap toRoundCorner(Bitmap bitmap, int pixels)
  {
    Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
    Canvas canvas = new Canvas(output);
    final int color = 0xff424242;
    final Paint paint = new Paint();
    final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
    final RectF rectF = new RectF(rect);
    final float roundPx = pixels;
    paint.setAntiAlias(true);
    canvas.drawARGB(0, 0, 0, 0);
    paint.setColor(color);
    canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
    paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
    canvas.drawBitmap(bitmap, rect, rect, paint);
    return output;
  }

  @SuppressWarnings("deprecation")
  public static String getPath(Activity activity, Uri uri)
  {

    String[] projection =
    {
      MediaStore.Images.Media.DATA
    };

    Cursor cursor = activity.managedQuery(uri, projection, null, null, null);

    int column_index = cursor

    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

    cursor.moveToFirst();

    return cursor.getString(column_index);
  }

  public static Intent getPhotoPickIntent()
  {
    Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
    intent.setType("image/*");
    intent.putExtra("crop", "true");
    intent.putExtra("aspectX", 1);// 这两项为裁剪框的比例.
    intent.putExtra("aspectY", 1);// x:y=1:2
    intent.putExtra("outputX", 500);
    intent.putExtra("outputY", 500);
    intent.putExtra("return-data", false);
    return intent;
  }

  // //////////////////////////////////////////////////////////////
  // 1.宽高和比例都不设置时,裁剪框可以自行调整(比例和大小都可以随意调整)
  // //////////////////////////////////////////////////////////////
  // 2.只设置裁剪框宽高比(aspect)后,裁剪框比例固定不可调整,只能调整大小
  // //////////////////////////////////////////////////////////////
  // 3.裁剪后生成图片宽高(output)的设置和裁剪框无关,只决定最终生成图片大小
  // //////////////////////////////////////////////////////////////
  // 4.裁剪框宽高比例(aspect)可以和裁剪后生成图片比例(output)不同,此时,
  // 会以裁剪框的宽为准,按照裁剪宽高比例生成一个图片,该图和框选部分可能不同,
  // 不同的情况可能是截取框选的一部分,也可能超出框选部分,向下延伸补足
  // aspectX aspectY 是裁剪框宽高的比例
  // outputX outputY 是裁剪后生成图片的宽高
  // return-data为true时,会直接返回bitmap数据,但是大图裁剪时会出现问题,推荐下面为false时的方式
  // return-data为false时,不会返回bitmap,但需要指定一个MediaStore.EXTRA_OUTPUT保存图片uri
  // //////////////////////////////////////////////////////////////
  public static Intent getCropImageIntent(Uri photoUri)
  {
    Intent intent = new Intent("com.android.camera.action.CROP");
    intent.setDataAndType(photoUri, "image/*");
    intent.putExtra("crop", "true");// crop=true 有这句才能出来最后的裁剪页面.
    intent.putExtra("aspectX", 1);// 这两项为裁剪框的比例.
    intent.putExtra("aspectY", 1);// x:y=1:2
    intent.putExtra("outputX", 500);
    intent.putExtra("outputY", 500);
    intent.putExtra("outputFormat", "JPEG");// 返回格式
    intent.putExtra(MediaStore.EXTRA_OUTPUT, ImageUtils.cropImageUri);
    intent.putExtra("return-data", false);
    return intent;
  }

  @SuppressLint("SimpleDateFormat")
  public static String getPhotoFileName()
  {
    Date date = new Date(System.currentTimeMillis());
    SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
    return dateFormat.format(date) + ".jpg";
  }

  public void startCamearPicCut(Activity activity, File tempFile)
  {
    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    intent.putExtra("camerasensortype", 2);
    intent.putExtra("autofocus", true);
    intent.putExtra("fullScreen", false);
    intent.putExtra("showActionIcons", false);
    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
    activity.startActivityForResult(intent, ImageUtils.CAMERA_CODE);
  }

  @SuppressLint("SimpleDateFormat")
  public void startCamearPicCut2(Activity activity)
  {
    SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
    String displayname = format.format(new Date(System.currentTimeMillis()));
    Intent takephoto = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
    ContentValues values = new ContentValues(3);
    values.put(MediaStore.Images.Media.DISPLAY_NAME, displayname);
    values.put(MediaStore.Images.Media.DESCRIPTION, "this is description");
    values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
    Uri imagePath =
        activity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    takephoto.putExtra(MediaStore.EXTRA_OUTPUT, imagePath);
    activity.startActivityForResult(takephoto, ImageUtils.CAMERA_CODE);
  }

}
