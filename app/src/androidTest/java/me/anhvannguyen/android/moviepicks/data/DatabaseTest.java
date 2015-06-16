package me.anhvannguyen.android.moviepicks.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import me.anhvannguyen.android.moviepicks.TestUtilities;

/**
 * Created by anhvannguyen on 6/15/15.
 */
public class DatabaseTest extends AndroidTestCase {
    private MovieDbHelper movieDB;
    private SQLiteDatabase db;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        RenamingDelegatingContext context = new RenamingDelegatingContext(getContext(), "test_");
        movieDB = new MovieDbHelper(context);
        db = movieDB.getWritableDatabase();
    }

    public void testCreateDB() {
        assertTrue("Error opening database", db.isOpen());
    }

    public void testDbEntry() {
        ContentValues newValue = TestUtilities.createMovieTestValue();

        // test insert
        long movieRowId;
        movieRowId = db.insert(MovieDbContract.MovieEntry.TABLE_NAME, null, newValue);
        assertTrue("Error inserting into database", movieRowId != -1);

        // test query
        Cursor cursor = db.query(
                MovieDbContract.MovieEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        // Move the cursor to the first valid database row and check to see if we have any rows
        assertTrue("Error: No Records returned from database query", cursor.moveToFirst());

        TestUtilities.validateCurrentRecord("testDbEntry movie entry failed to validate",
                cursor, newValue);

        // Update the favorite column by searching for the movie id
        int idIndex = cursor.getColumnIndex(MovieDbContract.MovieEntry._ID);
        String movieId = String.valueOf(cursor.getInt(idIndex));
        assertEquals("Error: Incorrect ID", "135397", movieId);
        db.update(
                MovieDbContract.MovieEntry.TABLE_NAME,      // table name
                TestUtilities.setFavorite(),                // content value to update
                MovieDbContract.MovieEntry._ID + "=?",      // where clause
                new String[] {movieId}                      // where args (String[])
        );

        // return a cursor with the movie entry that we updated
        Cursor updateCursor = db.query(
                MovieDbContract.MovieEntry.TABLE_NAME,
                null,
                MovieDbContract.MovieEntry._ID + "=?",
                new String[] { movieId },
                null,
                null,
                null
        );

        // Check if favorite is updated to "true"
        assertTrue(updateCursor.moveToFirst());

        int favoriteIndex = updateCursor.getColumnIndex(MovieDbContract.MovieEntry.COLUMN_FAVORITE);
        boolean isFavorite = updateCursor.getInt(favoriteIndex) != 0;
        assertEquals("Error: Favorite column not updated", true, isFavorite);

        assertFalse("Error: More than one record returned from database query", cursor.moveToNext());

        cursor.close();
    }

    @Override
    protected void tearDown() throws Exception {
        db.close();
        super.tearDown();
    }
}
