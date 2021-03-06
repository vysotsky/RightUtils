package com.rightutils.rightutils.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import org.codehaus.jackson.map.ObjectMapper;

/**
 * Created by Anton Maniskevich on 12/22/14.
 */
public abstract class CacheUtils {

	private static final String TAG = CacheUtils.class.getSimpleName();

	public static boolean debug = false;

	public interface CallBack<T> {
		public boolean run(T cache);
	}

    @Deprecated
    public static synchronized <T> void getCache(ObjectMapper mapper, Class<T> type, Context context, CallBack callback, boolean saveCache) {
        getCache(mapper,type,context,callback);
    }

	public static synchronized <T> void getCache(ObjectMapper mapper, Class<T> type, Context context, CallBack callback) {
		T cache = null;
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		if (!"".equals(sharedPreferences.getString(type.getSimpleName(), ""))) {
			try {
				cache = mapper.readValue(sharedPreferences.getString(type.getSimpleName(), ""), type);
			} catch (Exception e) {
				Log.e(TAG, "get CACHE", e);
			}
		}
		if (cache == null) {
			try {
				cache = type.newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if(callback != null){
			log(context,"Cache Read: " + cache.toString());
			if(callback.run(cache)){
				log(context,"Cache Write: " + cache.toString());
                try {
                    sharedPreferences.edit().remove(type.getSimpleName()).commit();
                    sharedPreferences.edit().putString(type.getSimpleName(), mapper.writeValueAsString(cache)).commit();
                } catch (Exception e) {
                    Log.e(TAG, "save CACHE", e);
                }
            }
		}
	}

	private static void log(Context context,String log){
		if(debug) {
			Log.i(TAG + " " + context.getClass().getSimpleName(), log);
		}
	}

}

