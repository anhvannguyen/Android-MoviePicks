package me.anhvannguyen.android.moviepicks;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by anhvannguyen on 6/19/15.
 */
public class FetchMovieDetailsTask extends AsyncTask<String, Void, Void> {
    private final String LOG_TAG = FetchMovieDetailsTask.class.getSimpleName();

    private final String MOVIE_API_KEY = MovieDbApiKey.getKey();
    private final String MOVIE_API_PARAM = "api_key";
    private final String MOVIE_BASE_URL = "http://api.themoviedb.org/3";

    @Override
    protected Void doInBackground(String... params) {

//        if (params == null) {
//            return null;
//        }

        HttpURLConnection urlConnection = null;
        BufferedReader bufferedReader = null;

        String movieDetailJsonStr = null;

        try {
            final String MOVIE_PATH = "movie";
            final String MOVIE_ID = params[0];  //"118340"; // Placeholder test
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
            Log.v(LOG_TAG, movieDetailJsonStr);

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

        return null;
    }
}
