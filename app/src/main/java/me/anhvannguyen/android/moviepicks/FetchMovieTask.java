package me.anhvannguyen.android.moviepicks;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by anhvannguyen on 6/11/15.
 */
public class FetchMovieTask extends AsyncTask<Void, Void, String[]> {
    private final String LOG_TAG = FetchMovieTask.class.getSimpleName();

    private final String MOVIE_API_KEY = MovieDbApiKey.getKey();
    private final String MOVIE_API_PARAM = "api_key";
    private final String MOVIE_BASE_URL = "http://api.themoviedb.org/3";

    @Override
    protected String[] doInBackground(Void... params) {

        HttpURLConnection urlConnection = null;
        BufferedReader bufferedReader = null;

        String movieJsonStr;

        try {
            // Temp path/params
            String REFERENCE_PATH = "discover";
            String ITEM_CONTENT_PATH = "movie";
            String SORT_PARAM = "sort_by";
            String SORT_OPTION = "popularity.desc";

            // Build themoviedb.org URI
            Uri movieUri = Uri.parse(MOVIE_BASE_URL)
                    .buildUpon()
                    .appendPath(REFERENCE_PATH)
                    .appendPath(ITEM_CONTENT_PATH)
                    .appendQueryParameter(SORT_PARAM, SORT_OPTION)
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
                return null;
            }

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
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
                return null;
            }
            movieJsonStr = buffer.toString();

            //Log.d(LOG_TAG, "themoviedb.org JSON:" + movieJsonStr);

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the movie data, there's no point in attempting
            // to parse it.
            return null;
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
            return convertJson(movieJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String[] strings) {
        if (strings != null) {
            MainActivityFragment.mMovieAdapter.clear();
            MainActivityFragment.mMovieAdapter.addAll(strings);
        }
    }

    private String[] convertJson(String movieJsonStr) throws JSONException {
        // These are the names of the JSON objects that need to be extracted.
        final String MDB_RESULT = "results";
        final String MDB_ID = "id";                         // int
        final String MDB_TITLE = "title";                   // String
        final String MDB_ORIGINAL_TITLE = "original_title"; // String
        final String MDB_RELEASE_DATE = "release_date";     // String
        final String MDB_VOTE_AVERAGE = "vote_average";     // float
        final String MDB_VOTE_COUNT = "vote_count";         // int
        final String MDB_POPULARITY = "popularity";         // float
        final String MDB_POSTER_PATH = "poster_path";       // String
        final String MDB_BACKDROP_PATH = "backdrop_path";   // String

        JSONObject movieJson = new JSONObject(movieJsonStr);
        JSONArray movieArray = movieJson.getJSONArray(MDB_RESULT);

        int movieArrayCount = movieArray.length();

        String[] results = new String[movieArrayCount];

        for (int i=0; i < movieArrayCount; i++) {
            JSONObject movieObject = movieArray.getJSONObject(i);
            results[i] = movieObject.getString(MDB_TITLE);
        }

//        for (String s : results) {
//            Log.v(LOG_TAG, "Movie Title: " + s);
//        }

        return results;
    }
}
