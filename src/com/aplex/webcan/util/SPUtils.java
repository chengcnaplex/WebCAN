package com.aplex.webcan.util;

import android.R.integer;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SPUtils {
	private static String SPFILE_NAME = "config";
	private static SharedPreferences sp;
	private static void initSharedPreferences(Context context){
		if (sp == null) {
			synchronized (SPUtils.class) {
				if (sp == null) {
					sp = context.getSharedPreferences(SPFILE_NAME, Context.MODE_PRIVATE);
				}
			}
		}
	}
	public static void setString(Context context, String key,String value){
		initSharedPreferences(context);
		Editor edit = sp.edit();
		edit.putString(key, value);
		edit.commit();
	}
	public static String getString(Context context,String key,String defValue){
		initSharedPreferences(context);
		String value = sp.getString(key, defValue);
		return value;
	}
	
	public static int getInt(Context context, String key, int defValue){
		initSharedPreferences(context);
		int value = sp.getInt(key, defValue);
		return value;
	}
	
	public static void setInt(Context context, String key, int value){
		initSharedPreferences(context);
		Editor edit = sp.edit();
		edit.putInt(key, value);
		edit.commit();
	}
}
