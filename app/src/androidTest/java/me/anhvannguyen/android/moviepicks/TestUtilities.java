package me.anhvannguyen.android.moviepicks;

import android.content.ContentValues;
import android.database.Cursor;
import android.test.AndroidTestCase;

import java.util.Map;
import java.util.Set;

import me.anhvannguyen.android.moviepicks.data.MovieDbContract;

/**
 * Created by anhvannguyen on 6/15/15.
 */
public class TestUtilities extends AndroidTestCase {

    public static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }

    // Generate some test data
    public static ContentValues createMovieTestValue() {
        ContentValues testValue = new ContentValues();
        testValue.put(MovieDbContract.MovieEntry._ID, 135397);
        testValue.put(MovieDbContract.MovieEntry.COLUMN_TITLE, "Jurassic World");
        testValue.put(MovieDbContract.MovieEntry.COLUMN_ORIGINAL_TITLE, "Jurassic World");
        testValue.put(MovieDbContract.MovieEntry.COLUMN_OVERVIEW, "Twenty-two years after the events of Jurassic Park, Isla Nublar now features a fully functioning dinosaur theme park, Jurassic World, as originally envisioned by John Hammond.");
        testValue.put(MovieDbContract.MovieEntry.COLUMN_RELEASE_DATE, "2015-06-12");
        testValue.put(MovieDbContract.MovieEntry.COLUMN_VOTE_AVERAGE, 7.1);
        testValue.put(MovieDbContract.MovieEntry.COLUMN_VOTE_COUNT, 312);
        testValue.put(MovieDbContract.MovieEntry.COLUMN_POPULARITY, 90.5835);
        testValue.put(MovieDbContract.MovieEntry.COLUMN_POSTER_PATH, "/uXZYawqUsChGSj54wcuBtEdUJbh.jpg");
        testValue.put(MovieDbContract.MovieEntry.COLUMN_BACKDROP_PATH, "/dkMD5qlogeRMiEixC4YNPUvax2T.jpg");

        return testValue;
    }

    // Mark movie as favorite for user
    public static ContentValues setFavorite() {
        ContentValues updateValue = new ContentValues();
        updateValue.put(MovieDbContract.MovieEntry.COLUMN_FAVORITE, 1);
        return updateValue;
    }
}
