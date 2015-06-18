package me.anhvannguyen.android.moviepicks.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by anhvannguyen on 6/15/15.
 */
public class MovieDbContract {

    // Content provider URI
    public static final String CONTENT_AUTHORITY = "me.anhvannguyen.android.moviepicks";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIES = "movies";

    /* Inner class that defines the table contents of the movies table */
    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;

        // Database columns
        // Table name
        public static final String TABLE_NAME = "movies";

        // themoviedb.org id entry
        public static final String COLUMN_MDB_ID = "mdb_id";

        // title of the movie
        public static final String COLUMN_TITLE = "title";

        // original title of the movie
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";

        // short overview about the movie
        public static final String COLUMN_OVERVIEW = "overview";

        // release date of the movie, stored as a String with the format yyyy-MM-dd
        public static final String COLUMN_RELEASE_DATE = "release_date";

        // users average rating of the movie on a scale of 10
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";

        // number of users who rated the movie
        public static final String COLUMN_VOTE_COUNT = "vote_count";

        // current popularity rating of the movie
        public static final String COLUMN_POPULARITY = "popularity";

        // the path of movie image poster
        public static final String COLUMN_POSTER_PATH = "poster_path";

        // the path of the movie image back drop path
        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";

        // if the movie is the user's favorite
        public static final String COLUMN_FAVORITE = "favorite";

        public static String getMovieId(Uri uri) {
            return uri.getLastPathSegment();
        }

        // Uri for the movie
        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }
}
