package me.anhvannguyen.android.moviepicks;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import me.anhvannguyen.android.moviepicks.data.MovieDbContract;

public class Utility {
    public static final String MOVIE_API_KEY = MovieDbApiKey.getKey();
    public static final String MOVIE_API_PARAM = "api_key";
    public static final String MOVIE_BASE_URL = "http://api.themoviedb.org/3";
    public static final String MDB_IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";

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

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public static String getTrailerUrl(String movieId) {
        final String MOVIE_PATH = "movie";
        final String MOVIE_ID = movieId;
        final String MOVIE_TRAILERS = "videos";

        // Build themoviedb.org URI
        Uri movieUri = Uri.parse(MOVIE_BASE_URL)
                .buildUpon()
                .appendPath(MOVIE_PATH)
                .appendPath(MOVIE_ID)
                .appendPath(MOVIE_TRAILERS)
                .appendQueryParameter(MOVIE_API_PARAM, MOVIE_API_KEY)
                .build();

        return movieUri.toString();
    }

    public static String getDetailUrl(String movieId) {
        final String MOVIE_PATH = "movie";
        final String MOVIE_ID = movieId;
        final String MOVIE_APPEND = "append_to_response";
        final String MOVIE_TRAILER_APPEND = "videos";

        // Build themoviedb.org URI
        Uri movieUri = Uri.parse(MOVIE_BASE_URL)
                .buildUpon()
                .appendPath(MOVIE_PATH)
                .appendPath(MOVIE_ID)
                .appendQueryParameter(MOVIE_API_PARAM, MOVIE_API_KEY)
                .appendQueryParameter(MOVIE_APPEND, MOVIE_TRAILER_APPEND)
                .build();

        return movieUri.toString();
    }

    public static Void convertTrailerJson(Context context, String movieTrailerJson) throws JSONException {
        final String TRAILER_TYPE = "Trailer";
        final String TRAILER_SITE = "YouTube";

        // These are the names of the JSON objects that need to be extracted.
        final String MDB_MOVIE_ID = "id";                   // int
        final String MDB_RESULT = "results";                // result array
        final String MDB_TRAILER_ID = "id";                 // uuid
        final String MDB_KEY = "key";                       // String
        final String MDB_NAME = "name";                     // String
        final String MDB_SITE = "site";                     // String
        final String MDB_TYPE = "type";                     // String

        JSONObject trailerObject = new JSONObject(movieTrailerJson);
        int movieId = trailerObject.getInt(MDB_MOVIE_ID);

        JSONArray trailerArray = trailerObject.getJSONArray(MDB_RESULT);
        int trailerArrayCount = trailerArray.length();

        Vector<ContentValues> cVVector = new Vector<ContentValues>(trailerArrayCount);

        for (int i = 0; i < trailerArrayCount; i++) {
            JSONObject movieObject = trailerArray.getJSONObject(i);

            String trailerID = movieObject.getString(MDB_TRAILER_ID);
            String key = movieObject.getString(MDB_KEY);
            String name = movieObject.getString(MDB_NAME);
            String site = movieObject.getString(MDB_SITE);
            String type = movieObject.getString(MDB_TYPE);

            // Only adding Trailers from Youtube
            if (site.equals(TRAILER_SITE) && type.equals(TRAILER_TYPE)) {
                ContentValues trailerValue = new ContentValues();

                trailerValue.put(MovieDbContract.TrailerEntry.COLUMN_MDB_ID, movieId);
                trailerValue.put(MovieDbContract.TrailerEntry.COLUMN_TRAILER_ID, trailerID);
                trailerValue.put(MovieDbContract.TrailerEntry.COLUMN_KEY, key);
                trailerValue.put(MovieDbContract.TrailerEntry.COLUMN_NAME, name);
                trailerValue.put(MovieDbContract.TrailerEntry.COLUMN_SITE, site);
                trailerValue.put(MovieDbContract.TrailerEntry.COLUMN_TYPE, type);

                cVVector.add(trailerValue);

            }
        }

        // add to database
        if (cVVector.size() > 0) {
            ContentValues[] contentValues = new ContentValues[cVVector.size()];
            cVVector.toArray(contentValues);
            context.getContentResolver().bulkInsert(MovieDbContract.TrailerEntry.CONTENT_URI, contentValues);
        }
        return null;
    }

    public static Void convertDetailJson(Context context, String movieDetailJson) throws JSONException {
        // Name of JSON Object to extract for the detail
        final String MDB_ID = "id";                 // int
        final String MDB_RUNTIME = "runtime";       // int
        final String MDB_HOMEPAGE = "homepage";     // String
        final String MDB_STATUS = "status";         // String
        final String MDB_TAGLINE = "tagline";       // String

        // Trailer parse constants
        final String MDB_TRAILER = "videos";
        final String TRAILER_TYPE = "Trailer";
        final String TRAILER_SITE = "YouTube";
        // These are the names of the JSON objects that need to be extracted for the trailers
        final String MDB_RESULT = "results";                // result array
        final String MDB_TRAILER_ID = "id";                 // uuid
        final String MDB_KEY = "key";                       // String
        final String MDB_NAME = "name";                     // String
        final String MDB_SITE = "site";                     // String
        final String MDB_TYPE = "type";                     // String

        JSONObject movieDetailObject = new JSONObject(movieDetailJson);

        int id = movieDetailObject.getInt(MDB_ID);
        int runtime = movieDetailObject.getInt(MDB_RUNTIME);
        String homepage = movieDetailObject.getString(MDB_HOMEPAGE);
        String status = movieDetailObject.getString(MDB_STATUS);
        String tagline = movieDetailObject.getString(MDB_TAGLINE);

        ContentValues value = new ContentValues();
        value.put(MovieDbContract.MovieEntry.COLUMN_RUNTIME, runtime);
        value.put(MovieDbContract.MovieEntry.COLUMN_HOMEPAGE, homepage);
        value.put(MovieDbContract.MovieEntry.COLUMN_STATUS, status);
        value.put(MovieDbContract.MovieEntry.COLUMN_TAGLINE, tagline);

        context.getContentResolver().update(
                MovieDbContract.MovieEntry.CONTENT_URI,
                value,
                MovieDbContract.MovieEntry._ID + " = ?",
                new String[]{String.valueOf(id)}
        );

        JSONObject trailerObject = movieDetailObject.getJSONObject(MDB_TRAILER);
        JSONArray trailerArray = trailerObject.getJSONArray(MDB_RESULT);

        int trailerArrayCount = trailerArray.length();

        Vector<ContentValues> cVVector = new Vector<ContentValues>(trailerArrayCount);

        for (int i = 0; i < trailerArrayCount; i++) {
            JSONObject movieObject = trailerArray.getJSONObject(i);

            String trailerID = movieObject.getString(MDB_TRAILER_ID);
            String key = movieObject.getString(MDB_KEY);
            String name = movieObject.getString(MDB_NAME);
            String site = movieObject.getString(MDB_SITE);
            String type = movieObject.getString(MDB_TYPE);

            // Only adding Trailers from Youtube
            if (site.equals(TRAILER_SITE) && type.equals(TRAILER_TYPE)) {
                ContentValues trailerValue = new ContentValues();

                trailerValue.put(MovieDbContract.TrailerEntry.COLUMN_MDB_ID, id);
                trailerValue.put(MovieDbContract.TrailerEntry.COLUMN_TRAILER_ID, trailerID);
                trailerValue.put(MovieDbContract.TrailerEntry.COLUMN_KEY, key);
                trailerValue.put(MovieDbContract.TrailerEntry.COLUMN_NAME, name);
                trailerValue.put(MovieDbContract.TrailerEntry.COLUMN_SITE, site);
                trailerValue.put(MovieDbContract.TrailerEntry.COLUMN_TYPE, type);

                cVVector.add(trailerValue);

            }
        }

        // add to database
        if (cVVector.size() > 0) {
            ContentValues[] contentValues = new ContentValues[cVVector.size()];
            cVVector.toArray(contentValues);
            context.getContentResolver().bulkInsert(MovieDbContract.TrailerEntry.CONTENT_URI, contentValues);
        }

        return null;
    }
}
