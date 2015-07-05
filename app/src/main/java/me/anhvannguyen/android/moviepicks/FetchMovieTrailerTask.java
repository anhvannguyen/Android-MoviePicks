package me.anhvannguyen.android.moviepicks;

import android.content.ContentValues;
import android.content.Context;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

import me.anhvannguyen.android.moviepicks.data.MovieDbContract;

/**
 * Created by anhvannguyen on 6/19/15.
 */
public class FetchMovieTrailerTask extends AsyncTask<String, Void, Void> {
    private final String LOG_TAG = FetchMovieTrailerTask.class.getSimpleName();

    private final String MOVIE_API_KEY = MovieDbApiKey.getKey();
    private final String MOVIE_API_PARAM = "api_key";
    private final String MOVIE_BASE_URL = "http://api.themoviedb.org/3";

    private Context mContext;
    public finishFetchCallback mListener;

    public interface finishFetchCallback {
        public void processTrailer();
    }

    public FetchMovieTrailerTask(Context context, finishFetchCallback callbackResponse) {
        mContext = context;
        mListener = callbackResponse;
    }

    @Override
    protected Void doInBackground(String... params) {
        if (params == null) {
            return null;
        }

        HttpURLConnection urlConnection = null;
        BufferedReader bufferedReader = null;

        String movieDetailJsonStr = null;

        try {
            final String MOVIE_PATH = "movie";
            final String MOVIE_ID = params[0];
            final String MOVIE_TRAILERS = "videos";
            final String MOVIE_REVIEWS = "reviews";

            // Build themoviedb.org URI
            Uri movieUri = Uri.parse(MOVIE_BASE_URL)
                    .buildUpon()
                    .appendPath(MOVIE_PATH)
                    .appendPath(MOVIE_ID)
                    .appendPath(MOVIE_TRAILERS)
                    .appendQueryParameter(MOVIE_API_PARAM, MOVIE_API_KEY)
                    .build();

            URL url = new URL(movieUri.toString());

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
            movieDetailJsonStr = buffer.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
            convertJson(movieDetailJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        mListener.processTrailer();
    }

    private Void convertJson(String movieDetailJson) throws JSONException {
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

        JSONObject trailerObject = new JSONObject(movieDetailJson);
        int movieId = trailerObject.getInt(MDB_MOVIE_ID);

        JSONArray trailerArray = trailerObject.getJSONArray(MDB_RESULT);
        int trailerArrayCount = trailerArray.length();

        Vector<ContentValues> cVVector = new Vector<ContentValues>(trailerArrayCount);

        for (int i=0; i < trailerArrayCount; i++) {
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
        if ( cVVector.size() > 0 ) {
            ContentValues[] contentValues = new ContentValues[cVVector.size()];
            cVVector.toArray(contentValues);
            mContext.getContentResolver().bulkInsert(MovieDbContract.TrailerEntry.CONTENT_URI, contentValues);
        }
        return null;
    }
}
