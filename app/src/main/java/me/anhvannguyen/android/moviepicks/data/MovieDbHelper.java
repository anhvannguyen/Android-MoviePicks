package me.anhvannguyen.android.moviepicks.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by anhvannguyen on 6/15/15.
 */
public class MovieDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "moviepicks.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieDbContract.MovieEntry.TABLE_NAME + " (" +
                MovieDbContract.MovieEntry._ID + " INTEGER PRIMARY KEY," +
                MovieDbContract.MovieEntry.COLUMN_TITLE + " TEXT NOT NULL," +
                MovieDbContract.MovieEntry.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL," +
                MovieDbContract.MovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL," +
                MovieDbContract.MovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL," +
                MovieDbContract.MovieEntry.COLUMN_VOTE_AVERAGE + " REAL NOT NULL," +
                MovieDbContract.MovieEntry.COLUMN_VOTE_COUNT + " INTEGER NOT NULL," +
                MovieDbContract.MovieEntry.COLUMN_POPULARITY + " REAL NOT NULL," +
                MovieDbContract.MovieEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL," +
                MovieDbContract.MovieEntry.COLUMN_BACKDROP_PATH + " TEXT NOT NULL," +
                MovieDbContract.MovieEntry.COLUMN_FAVORITE + " INTEGER NOT NULL DEFAULT 0" +
                " );";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieDbContract.MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
