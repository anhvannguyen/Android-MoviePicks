package me.anhvannguyen.android.moviepicks.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by anhvannguyen on 6/16/15.
 */
public class MovieContentProvider extends ContentProvider {
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDbHelper mOpenHelper;

    private static final int MOVIES = 100;
    private static final int MOVIES_WITH_ID = 101;

    private static final int TRAILERS = 200;
    private static final int TRAILERS_WITH_MOVIEID = 201;
    private static final int TRAILERS_WITH_MOVIEID_AND_TRAILERID = 202;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieDbContract.CONTENT_AUTHORITY;

        uriMatcher.addURI(authority, MovieDbContract.PATH_MOVIES, MOVIES);
        uriMatcher.addURI(authority, MovieDbContract.PATH_MOVIES + "/#", MOVIES_WITH_ID);

        uriMatcher.addURI(authority, MovieDbContract.PATH_TRAILERS, TRAILERS);
        uriMatcher.addURI(authority, MovieDbContract.PATH_TRAILERS + "/#", TRAILERS_WITH_MOVIEID);
        uriMatcher.addURI(authority, MovieDbContract.PATH_TRAILERS + "/#/#", TRAILERS_WITH_MOVIEID_AND_TRAILERID);

        return uriMatcher;

    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case MOVIES:
                return MovieDbContract.MovieEntry.CONTENT_TYPE;
            case MOVIES_WITH_ID:
                return MovieDbContract.MovieEntry.CONTENT_ITEM_TYPE;
            case TRAILERS:
                return MovieDbContract.TrailerEntry.CONTENT_TYPE;
            case TRAILERS_WITH_MOVIEID:
                return MovieDbContract.TrailerEntry.CONTENT_TYPE;
            case TRAILERS_WITH_MOVIEID_AND_TRAILERID:
                return MovieDbContract.TrailerEntry.CONTENT_ITEM_TYPE;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor returnCursor;
        switch (sUriMatcher.match(uri)) {
            case MOVIES: {
                returnCursor = mOpenHelper.getReadableDatabase().query(
                        MovieDbContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case MOVIES_WITH_ID: {
                String movieId = MovieDbContract.MovieEntry.getMovieId(uri);
                returnCursor = mOpenHelper.getReadableDatabase().query(
                        MovieDbContract.MovieEntry.TABLE_NAME,
                        projection,
                        MovieDbContract.MovieEntry._ID + " = ?",
                        new String[]{movieId},
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case TRAILERS: {
                returnCursor = mOpenHelper.getReadableDatabase().query(
                        MovieDbContract.TrailerEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case TRAILERS_WITH_MOVIEID: {
                String movieId = MovieDbContract.TrailerEntry.getMovieId(uri);
                returnCursor = mOpenHelper.getReadableDatabase().query(
                        MovieDbContract.TrailerEntry.TABLE_NAME,
                        projection,
                        MovieDbContract.TrailerEntry.COLUMN_MDB_ID + " = ?",
                        new String[]{movieId},
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case TRAILERS_WITH_MOVIEID_AND_TRAILERID: {
                String movieId = MovieDbContract.TrailerEntry.getMovieId(uri);
                String trailerId = MovieDbContract.TrailerEntry.getTrailerId(uri);
                returnCursor = mOpenHelper.getReadableDatabase().query(
                        MovieDbContract.TrailerEntry.TABLE_NAME,
                        projection,
                        MovieDbContract.TrailerEntry.COLUMN_MDB_ID + " = ? AND " +
                                MovieDbContract.TrailerEntry.COLUMN_MDB_ID + " = ?",
                        new String[]{movieId, trailerId},
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // notify content resolver that there is a change
        returnCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return returnCursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch (match) {
            case MOVIES: {
                long _id = db.insert(
                        MovieDbContract.MovieEntry.TABLE_NAME,
                        null,
                        values);
                if (_id > 0)
                    returnUri = MovieDbContract.MovieEntry.buildMovieUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case TRAILERS: {
                long _id = db.insert(
                        MovieDbContract.TrailerEntry.TABLE_NAME,
                        null,
                        values);
                if (_id > 0)
                    returnUri = MovieDbContract.TrailerEntry.buildTrailerUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "1";
        switch (match) {
            case MOVIES:
                rowsDeleted = db.delete(
                        MovieDbContract.MovieEntry.TABLE_NAME,
                        selection,
                        selectionArgs
                );
                break;
            case TRAILERS:
                rowsDeleted = db.delete(
                        MovieDbContract.TrailerEntry.TABLE_NAME,
                        selection,
                        selectionArgs
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case MOVIES:
                rowsUpdated = db.update(
                        MovieDbContract.MovieEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs
                );
                break;
            case TRAILERS:
                rowsUpdated = db.update(
                        MovieDbContract.TrailerEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIES:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(
                                MovieDbContract.MovieEntry.TABLE_NAME,
                                null,
                                value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}
