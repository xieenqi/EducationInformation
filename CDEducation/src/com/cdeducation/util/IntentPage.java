package com.cdeducation.util;

import java.io.File;

import com.cdeducation.data.NewsItems;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

public class IntentPage {

	public static Intent getIntent(String param) {
		Intent intent = null;
		if (!TextUtils.isEmpty(param)) {
			if (param.endsWith(".pdf")) {
				intent = getPdfFileIntent(param);
			} else if (param.endsWith(".doc") || param.endsWith(".docx")) {
				intent = getWordFileIntent(param);
			} else if (param.endsWith(".xlsx") || param.endsWith(".xls")) {
				intent = getExcelFileIntent(param);
			} else if (param.endsWith(".ppt")) {
				intent = getPptFileIntent(param);
			} else if (param.endsWith(".txt")) {
				getTextFileIntent(param, false);
			}
		}
		return intent;
	}

	// android获取一个用于打开PPT文件的intent
	public static Intent getPptFileIntent(String param) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
		return intent;
	}

	// android获取一个用于打开Excel文件的intent
	public static Intent getExcelFileIntent(String param) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "application/vnd.ms-excel");
		return intent;
	}

	// android获取一个用于打开Word文件的intent
	public static Intent getWordFileIntent(String param) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "application/msword");
		return intent;
	}

	// android获取一个用于打开CHM文件的intent
	public static Intent getChmFileIntent(String param) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "application/x-chm");
		return intent;
	}

	// android获取一个用于打开文本文件的intent
	public static Intent getTextFileIntent(String param, boolean paramBoolean) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if (paramBoolean) {
			Uri uri1 = Uri.parse(param);
			intent.setDataAndType(uri1, "text/plain");
		} else {
			Uri uri2 = Uri.fromFile(new File(param));
			intent.setDataAndType(uri2, "text/plain");
		}
		return intent;
	}

	// android获取一个用于打开PDF文件的intent
	public static Intent getPdfFileIntent(String param) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "application/pdf");
		return intent;
	}
	 //Android获取一个用于打开VIDEO文件的intent 
    public static Intent getVideoFileIntent( String param ) { 
   
        Intent intent = new Intent("android.intent.action.VIEW"); 
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
        intent.putExtra("oneshot", 0); 
        intent.putExtra("configchange", 0); 
        Uri uri = Uri.fromFile(new File(param )); 
        intent.setDataAndType(uri, "video/*"); 
        return intent; 
    } 
   
    //Android获取一个用于打开AUDIO文件的intent 
    public static Intent getAudioFileIntent( String param ){ 
   
        Intent intent = new Intent("android.intent.action.VIEW"); 
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
        intent.putExtra("oneshot", 0); 
        intent.putExtra("configchange", 0); 
        Uri uri = Uri.fromFile(new File(param )); 
        intent.setDataAndType(uri, "audio/*"); 
        return intent; 
    }  
}
