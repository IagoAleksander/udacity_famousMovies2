package com.iaz.filmesfamosos.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.iaz.filmesfamosos.R;

public class Prefs {

    private static SharedPreferences sharedPrefs;

    private static SharedPreferences getSharedPrefs(Context context) {
        if (sharedPrefs == null)
            sharedPrefs = context.getSharedPreferences(context.getString(R.string.pref_key), Context.MODE_PRIVATE);

        return sharedPrefs;
    }

    public static void storeLastUpdatedTimePopular(Context context, long timeMillis) {
        SharedPreferences.Editor editor = getSharedPrefs(context).edit();
        editor.putLong(context.getString(R.string.last_updated_popular), timeMillis);
        editor.apply();
    }

    static long getLastUpdatedTimePopular(Context context) {
        return getSharedPrefs(context).getLong(context.getString(R.string.last_updated_popular), 0);
    }

    public static void storeLastUpdatedTimeTopRated(Context context, long timeMillis) {
        SharedPreferences.Editor editor = getSharedPrefs(context).edit();
        editor.putLong(context.getString(R.string.last_updated_top_rated), timeMillis);
        editor.apply();
    }

    static long getLastUpdatedTimeTopRated(Context context) {
        return getSharedPrefs(context).getLong(context.getString(R.string.last_updated_top_rated), 0);
    }

    public static void storeLastUpdatedTimeUpcoming(Context context, long timeMillis) {
        SharedPreferences.Editor editor = getSharedPrefs(context).edit();
        editor.putLong(context.getString(R.string.last_updated_upcoming), timeMillis);
        editor.apply();
    }

    static long getLastUpdatedTimeUpcoming(Context context) {
        return getSharedPrefs(context).getLong(context.getString(R.string.last_updated_upcoming), 0);
    }

}
