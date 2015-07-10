package me.anhvannguyen.android.moviepicks.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

import me.anhvannguyen.android.moviepicks.MovieDbApiKey;
import me.anhvannguyen.android.moviepicks.R;
import me.anhvannguyen.android.moviepicks.Utility;
import me.anhvannguyen.android.moviepicks.VolleySingleton;
import me.anhvannguyen.android.moviepicks.data.MovieDbContract;

/**
 * Created by anhvannguyen on 7/7/15.
 */
public class MovieSyncAdapter extends AbstractThreadedSyncAdapter {
    private final String LOG_TAG = MovieSyncAdapter.class.getCanonicalName();

    public static final int SYNC_INTERVAL = 60 * 180;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL/3;

    public MovieSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        HttpURLConnection urlConnection = null;
        BufferedReader bufferedReader = null;

        String movieJsonStr;

        try {
            final String MOVIE_API_KEY = MovieDbApiKey.getKey();
            final String MOVIE_API_PARAM = "api_key";
            final String MOVIE_BASE_URL = "http://api.themoviedb.org/3";

            // themoviedb.org path/params
            final String REFERENCE_PATH = "discover";
            final String ITEM_CONTENT_PATH = "movie";
            final String SORT_PARAM = "sort_by";
            final String SORT_OPTION = "popularity.desc";

            // Build themoviedb.org URI
            Uri movieUri = Uri.parse(MOVIE_BASE_URL)
                    .buildUpon()
                    .appendPath(REFERENCE_PATH)
                    .appendPath(ITEM_CONTENT_PATH)
                    .appendQueryParameter(SORT_PARAM, SORT_OPTION)
                            //.appendQueryParameter(VOTECOUNT_PARAM, Integer.toString(MIN_VOTE_COUNT))
                    .appendQueryParameter(MOVIE_API_PARAM, MOVIE_API_KEY)
                    .build();

            URL url = new URL(movieUri.toString());

            //Log.d(LOG_TAG, "Movie URI " + movieUri.toString());

            // Create the request to the URL, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            int responseCode = urlConnection.getResponseCode();

            if (responseCode != HttpURLConnection.HTTP_OK) {
                return;
            }

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return;
            }
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return;
            }
            movieJsonStr = buffer.toString();

            //Log.d(LOG_TAG, "themoviedb.org JSON:" + movieJsonStr);

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the movie data, there's no point in attempting
            // to parse it.
            return;
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        try {
            convertJson(movieJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        return;
    }

    private void convertJson(String movieJsonStr) throws JSONException {
        // These are the names of the JSON objects that need to be extracted.
        final String MDB_RESULT = "results";
        final String MDB_ID = "id";                         // int
        final String MDB_TITLE = "title";                   // String
        final String MDB_ORIGINAL_TITLE = "original_title"; // String
        final String MDB_OVERVIEW = "overview";             // String
        final String MDB_RELEASE_DATE = "release_date";     // String
        final String MDB_VOTE_AVERAGE = "vote_average";     // float
        final String MDB_VOTE_COUNT = "vote_count";         // int
        final String MDB_POPULARITY = "popularity";         // float
        final String MDB_POSTER_PATH = "poster_path";       // String
        final String MDB_BACKDROP_PATH = "backdrop_path";   // String

        JSONObject movieJson = new JSONObject(movieJsonStr);
        JSONArray movieArray = movieJson.getJSONArray(MDB_RESULT);

        int movieArrayCount = movieArray.length();

        Vector<ContentValues> cVVector = new Vector<ContentValues>(movieArrayCount);

        // Get the data from the JSON
        for (int i=0; i < movieArrayCount; i++) {
            JSONObject movieObject = movieArray.getJSONObject(i);

            int id = movieObject.getInt(MDB_ID);
            String title = movieObject.getString(MDB_TITLE);
            String originalTitle = movieObject.getString(MDB_ORIGINAL_TITLE);
            String overview = movieObject.getString(MDB_OVERVIEW);
            String releaseDate = movieObject.getString(MDB_RELEASE_DATE);
            Double voteAverage = movieObject.getDouble(MDB_VOTE_AVERAGE);
            int voteCount = movieObject.getInt(MDB_VOTE_COUNT);
            Double popularity = movieObject.getDouble(MDB_POPULARITY);
            String posterPath = movieObject.getString(MDB_POSTER_PATH);
            String backdropPath = movieObject.getString(MDB_BACKDROP_PATH);

            ContentValues movieValues = new ContentValues();

            movieValues.put(MovieDbContract.MovieEntry._ID, id);
            movieValues.put(MovieDbContract.MovieEntry.COLUMN_TITLE, title);
            movieValues.put(MovieDbContract.MovieEntry.COLUMN_ORIGINAL_TITLE, originalTitle);
            movieValues.put(MovieDbContract.MovieEntry.COLUMN_OVERVIEW, overview);
            movieValues.put(MovieDbContract.MovieEntry.COLUMN_RELEASE_DATE, releaseDate);
            movieValues.put(MovieDbContract.MovieEntry.COLUMN_VOTE_AVERAGE, voteAverage);
            movieValues.put(MovieDbContract.MovieEntry.COLUMN_VOTE_COUNT, voteCount);
            movieValues.put(MovieDbContract.MovieEntry.COLUMN_POPULARITY, popularity);
            movieValues.put(MovieDbContract.MovieEntry.COLUMN_POSTER_PATH, posterPath);
            movieValues.put(MovieDbContract.MovieEntry.COLUMN_BACKDROP_PATH, backdropPath);

            cVVector.add(movieValues);
        }

        // delete all item from movie database
        getContext().getContentResolver().delete(
                MovieDbContract.MovieEntry.CONTENT_URI,
                null,
                null
        );
        // add to database
        if ( cVVector.size() > 0 ) {
            ContentValues[] contentValues = new ContentValues[cVVector.size()];
            cVVector.toArray(contentValues);
            getContext().getContentResolver().bulkInsert(MovieDbContract.MovieEntry.CONTENT_URI, contentValues);
        }

        for (ContentValues value : cVVector) {
            int movieId = value.getAsInteger(MovieDbContract.MovieEntry._ID);
            requestDetail(movieId);
        }

    }

    private void requestDetail(int id) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Utility.getDetailUrl(String.valueOf(id)),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Utility.convertDetailJson(getContext(), response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }

                });

        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

    /**
     * Helper method to have the sync adapter sync immediately
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if ( null == accountManager.getPassword(newAccount) ) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    /**
     * Helper method to schedule the sync adapter periodic execution
     */
    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
        MovieSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(context);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }

}
