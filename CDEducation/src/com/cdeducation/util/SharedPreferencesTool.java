package com.cdeducation.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferencesTool {
	private static SharedPreferences.Editor editor = null;
	private static SharedPreferences sharedPreferences = null;

	public static SharedPreferences getSharedPreferencesObject(
			Context paramContext) {
		if (sharedPreferences == null)
			sharedPreferences = PreferenceManager
					.getDefaultSharedPreferences(paramContext);
		return sharedPreferences;
	}

	private static SharedPreferences.Editor getEditorObject(Context paramContext) {
		if (editor == null)
			editor = PreferenceManager
					.getDefaultSharedPreferences(paramContext).edit();
		return editor;
	}

	public static Boolean getSharedPreferences(Context paramContext,
			String paramString, Boolean paramBoolean) {
		return getSharedPreferencesObject(paramContext).getBoolean(paramString,
				paramBoolean);
	}

	public static void setEditor(Context paramContext, String paramString,
			Boolean paramBoolean) {
		getEditorObject(paramContext).putBoolean(paramString, paramBoolean)
				.commit();
	}
}
