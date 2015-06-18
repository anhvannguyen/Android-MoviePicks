package me.anhvannguyen.android.moviepicks;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by anhvannguyen on 6/13/15.
 */
public class Utility {
    public static String MDB_IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";

    public static int getSortingPreference(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return Integer.parseInt(prefs.getString(context.getString(R.string.pref_sortorder_key),
                context.getString(R.string.pref_sort_value_popular)));
    }

    public static String getFullImagePath(String size, String posterPath) {
        return MDB_IMAGE_BASE_URL + size + posterPath;
    }

    public static Date getDateFromString(String dateString) {
        Date date = null;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
