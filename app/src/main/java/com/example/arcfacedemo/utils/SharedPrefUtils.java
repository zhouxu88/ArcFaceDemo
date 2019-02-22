package com.example.arcfacedemo.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * SharedPreferences工具类
 * @author zhouxu
 *
 */
public final class SharedPrefUtils {

	/**
	 * 取得SharedPreference对象
	 * @param con
	 * @return
	 */
	private static SharedPreferences pref(Context con) {
		return PreferenceManager.getDefaultSharedPreferences(con);
	}
	
	/**
	 * 移除指定的key
	 * @param context
	 * @param key
	 */
	public static void remove(Context context, String key) {
		try {
			pref(context.getApplicationContext()).edit().remove(key).apply();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 判断SharedPreferences是否包含特定key
	 * @param context
	 * @param key
	 * @return
	 */
	public static boolean contains(Context context, String key) {
		try {
			return pref(context.getApplicationContext()).contains(key);
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * String型数据的取得
	 * @param context
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static String getString(Context context, String key, String defValue) {	
		try {
			return pref(context.getApplicationContext()).getString(key, defValue);
		} catch (Exception e) {
			e.printStackTrace();
			return defValue;
		}
	}

	/**
	 * boolean型数据的取得
	 * @param context
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static boolean getBoolean(Context context, String key, boolean defValue) {
		try{
			return pref(context.getApplicationContext()).getBoolean(key, defValue);
		} catch (Exception e) {
			e.printStackTrace();
			return defValue;
		}
	}

	/**
	 * int型数据的取得
	 * @param context
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static int getInt(Context context, String key, int defValue) {
		try{
			return pref(context.getApplicationContext()).getInt(key, defValue);
		} catch (Exception e) {
			e.printStackTrace();
			return defValue;
		}
	}

	/**
	 * long型数据的取得
	 * @param context
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static long getLong(Context context, String key, long defValue) {
		try{
			return pref(context.getApplicationContext()).getLong(key, defValue);
		} catch (Exception e) {
			e.printStackTrace();
			return defValue;
		}
	}

	/**
	 * float型数据的取得
	 * @param context
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static float getFloat(Context context, String key, float defValue) {
		try{
			return pref(context.getApplicationContext()).getFloat(key, defValue);
		} catch (Exception e) {
			e.printStackTrace();
			return defValue;
		}
	}

	/**
	 * String型数据的写入
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void writeString(Context context, String key, String value) {
		try {
			pref(context.getApplicationContext()).edit().putString(key, value).apply();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * boolean型数据的写入
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void writeBoolean(Context context, String key, boolean value) {
		try {
			pref(context.getApplicationContext()).edit().putBoolean(key, value).apply();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * int型数据的写入
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void writeInt(Context context, String key, int value) {
		try {
			pref(context.getApplicationContext()).edit().putInt(key, value).apply();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * long型数据的写入
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void writeLong(Context context, String key, long value) {
		try {
			pref(context.getApplicationContext()).edit().putLong(key, value).apply();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * float型数据的写入
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void writeFloat(Context context, String key, float value) {
		try {
			pref(context.getApplicationContext()).edit().putFloat(key, value).apply();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
