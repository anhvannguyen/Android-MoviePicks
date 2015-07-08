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
    public static final String PATH_TRAILERS = "trailers";

    public static final String PATH_DETAIL_TRAILER = "trailer";

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

        // the time length of the movie in minutes
        public static final String COLUMN_RUNTIME = "runtime";

        // website url for the movie
        public static final String COLUMN_HOMEPAGE = "homepage";

        // movie status
        public static final String COLUMN_STATUS = "status";

        // movie tagline
        public static final String COLUMN_TAGLINE = "tagline";

        // if the movie is the user's favorite
        public static final String COLUMN_FAVORITE = "favorite";

        // get the id from the uri
        public static String getMovieId(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        // Uri for the movie
        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

    public static final class TrailerEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRAILERS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILERS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILERS;

        // Table name
        public static final String TABLE_NAME = "trailers";

        // themoviedb.org movie ID
        public static final String COLUMN_MDB_ID = "movie_id";

        // themoviedb.org trailer ID
        public static final String COLUMN_TRAILER_ID = "trailer_id";

        // trailer key
        public static final String COLUMN_KEY = "key";

        // trailer name
        public static final String COLUMN_NAME = "name";

        // website where trailer is hosted
        public static final String COLUMN_SITE = "site";

        // type of video (Trailer/Featurette)
        public static final String COLUMN_TYPE = "type";

        public static String getTrailerId(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static Uri buildTrailerUri(long trailerId) {
            return ContentUris.withAppendedId(CONTENT_URI, trailerId);
        }

        public static Uri buildMovieTrailerUri(Uri uri) {
            return uri.buildUpon().appendPath(PATH_DETAIL_TRAILER).build();
        }

    }
}
