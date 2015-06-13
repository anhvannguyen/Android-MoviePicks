package me.anhvannguyen.android.moviepicks;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by anhvannguyen on 6/13/15.
 */
public class Utility {
    public static String getSortingPreference(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_sortorder_key),
                context.getString(R.string.pref_sort_value_popular));
    }
}
