package com.libray.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

public class ImageUtils {
	public static File mCurrentPhotoFile = null;
	private static File PHOTO_DIR = null;

	public static final int CAMERA_CODE = 1;
	public static final int PHOTO_CODE = 2;
	public static final int CUT_CODE = 3;
	public static Uri imageUriFromCamera;
	public static Uri cropImageUri;

	/**
	 * 调用相机拍照
	 * 
	 * @param activity
	 */
	public static void openCameraImageUri(final Activity activity) {
		ImageUtils.imageUriFromCamera = ImageUtils.createImagePathUri(activity);
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// MediaStore.EXTRA_OUTPUT参数不设置时,系统会自动生成一个uri,但是只会返回一个缩略图
		// 返回图片在onActivityResult中通过以下代码获取
		// Bitmap bitmap = (Bitmap) data.getExtras().get("data");
		intent.putExtra(MediaStore.EXTRA_OUTPUT, ImageUtils.imageUriFromCamera);
		activity.startActivityForResult(intent, CAMERA_CODE);
	}

	/**
	 * 调用相机拍照
	 * 
	 * @param activity
	 */
	public static void openCameraImage(Activity activity) {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			PHOTO_DIR = new File(CommonUtils.getPhotoCachePic(activity));
			PHOTO_DIR.mkdir();
			mCurrentPhotoFile = new File(PHOTO_DIR, IamgeUpload.getPhotoFileName());
			Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
			Uri fromFile = Uri.fromFile(mCurrentPhotoFile);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, fromFile);
			activity.startActivityForResult(intent, CAMERA_CODE);
		} else {
			ViewTool.showToast(activity, "No sd");
		}

	}

	/**
	 * 调用本地相册的图片自动剪切
	 * 
	 * @param activity
	 */
	public static void openLocalImageAutoCut(Activity activity) {
		Intent intent = IamgeUpload.getPhotoPickIntent();
		activity.startActivityForResult(intent, PHOTO_CODE);
	}

	/**
	 * 打开本地相册图片需调用cropImage（）方法手动剪切
	 * 
	 * @param activity
	 */
	public static void openLocalImage(Activity activity) {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		activity.startActivityForResult(intent, PHOTO_CODE);
	}

	/**
	 * 剪切图片
	 * 
	 * @param activity
	 * @param srcUri
	 */
	public static void cropImage(Activity activity, Uri srcUri) {
		ImageUtils.cropImageUri = ImageUtils.createImagePathUri(activity);
		Intent intent = IamgeUpload.getCropImageIntent(srcUri);
		activity.startActivityForResult(intent, CUT_CODE);// CUT_CODE
	}

	/**
	 * 创建一条图片地址uri,用于保存拍照后的照片
	 * 
	 * @param context
	 * @return 图片的uri
	 */
	public static Uri createImagePathUri(Context context) {
		Uri imageFilePath = null;
		String status = Environment.getExternalStorageState();
		SimpleDateFormat timeFormatter = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
		long time = System.currentTimeMillis();
		String imageName = timeFormatter.format(new Date(time));
		// ContentValues是我们希望这条记录被创建时包含的数据信息
		ContentValues values = new ContentValues(3);
		values.put(MediaStore.Images.Media.DISPLAY_NAME, imageName);
		values.put(MediaStore.Images.Media.DATE_TAKEN, time);
		values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			// 判断是否有SD卡,优先使用SD卡存储,当没有SD卡时使用手机存储
			imageFilePath = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
		} else {
			imageFilePath = context.getContentResolver().insert(MediaStore.Images.Media.INTERNAL_CONTENT_URI, values);
		}
		return imageFilePath;
	}

}
