package me.anhvannguyen.android.moviepicks;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import me.anhvannguyen.android.moviepicks.data.MovieDbContract;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private final String LOG_TAG = DetailActivityFragment.class.getSimpleName();

    private static final int MOVIE_DETAIL_LOADER = 0;
    public static final String DETAIL_URI = "URI";
    private final float MAX_RATING = 10.0f;

    private static final String[] MOVIEDETAIL_PROJECTION = {
            MovieDbContract.MovieEntry._ID,
            MovieDbContract.MovieEntry.COLUMN_TITLE,
            MovieDbContract.MovieEntry.COLUMN_ORIGINAL_TITLE,
            MovieDbContract.MovieEntry.COLUMN_OVERVIEW,
            MovieDbContract.MovieEntry.COLUMN_RELEASE_DATE,
            MovieDbContract.MovieEntry.COLUMN_VOTE_AVERAGE,
            MovieDbContract.MovieEntry.COLUMN_VOTE_COUNT,
            MovieDbContract.MovieEntry.COLUMN_POPULARITY,
            MovieDbContract.MovieEntry.COLUMN_POSTER_PATH,
            MovieDbContract.MovieEntry.COLUMN_BACKDROP_PATH,
            MovieDbContract.MovieEntry.COLUMN_FAVORITE
    };

    public static final int COL_MOVIE_ID = 0;
    public static final int COL_MOVIE_TITLE = 1;
    public static final int COL_MOVIE_ORIGINAL_TITLE = 2;
    public static final int COL_MOVIE_OVERVIEW = 3;
    public static final int COL_MOVIE_RELEASE_DATE = 4;
    public static final int COL_MOVIE_VOTE_AVERAGE = 5;
    public static final int COL_MOVIE_VOTE_COUNT = 6;
    public static final int COL_MOVIE_POPULARITY = 7;
    public static final int COL_MOVIE_POSTER_PATH = 8;
    public static final int COL_MOVIE_BACKDROP_PATH = 9;
    public static final int COL_MOVIE_FAVORITE = 10;

    private TextView mIdTextView;
    private TextView mTitleTextView;
    private TextView mOriginalTitleTextView;
    private TextView mOverviewTextView;
    private TextView mReleaseDateTextView;
    private TextView mVoteAverageTextView;
    private RatingBar mRatingBar;
    private TextView mVoteCountTextView;
    private TextView mPopularityTextView;
    private ImageView mBackdropImage;
    private ImageView mPosterImage;

    private Uri mUri;

    public DetailActivityFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        mIdTextView = (TextView)rootView.findViewById(R.id.detail_id_textview);
        mTitleTextView = (TextView)rootView.findViewById(R.id.detail_title_textview);
        mOriginalTitleTextView = (TextView)rootView.findViewById(R.id.detail_original_title_textview);
        mOverviewTextView = (TextView)rootView.findViewById(R.id.detail_overview_textview);
        mReleaseDateTextView = (TextView)rootView.findViewById(R.id.detail_release_date_textview);
        mVoteAverageTextView = (TextView)rootView.findViewById(R.id.detail_vote_average_textview);
        mRatingBar = (RatingBar)rootView.findViewById(R.id.detail_rating_bar);
        mVoteCountTextView = (TextView)rootView.findViewById(R.id.detail_vote_count_textview);
        mPopularityTextView = (TextView)rootView.findViewById(R.id.detail_popuarity_textview);
        mBackdropImage = (ImageView)rootView.findViewById(R.id.detail_backdrop_imageview);
        mPosterImage = (ImageView)rootView.findViewById(R.id.detail_poster_imageview);

        mRatingBar.setVisibility(View.INVISIBLE);

        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = arguments.getParcelable(DetailActivityFragment.DETAIL_URI);
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(MOVIE_DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_detail_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_test) {
            if (mUri != null) {
                String movieId = MovieDbContract.MovieEntry.getMovieId(mUri);
                new FetchMovieDetailsTask(getActivity()).execute(movieId);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        if (mUri != null) {

            return new CursorLoader(
                    getActivity(),
                    mUri,
                    MOVIEDETAIL_PROJECTION,
                    null,
                    null,
                    null
            );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (!cursor.moveToFirst()) {
            return;
        }

        int id = cursor.getInt(COL_MOVIE_ID);
        mIdTextView.setText("ID: " + id);

        String title = cursor.getString(COL_MOVIE_TITLE);
        mTitleTextView.setText("Title: " + title);

        String originalTitle = cursor.getString(COL_MOVIE_ORIGINAL_TITLE);
        mOriginalTitleTextView.setText("Original Title: " + originalTitle);

        String overview = cursor.getString(COL_MOVIE_OVERVIEW);
        mOverviewTextView.setText("Overview: " + overview);

        String releaseDate = cursor.getString(COL_MOVIE_RELEASE_DATE);
        mReleaseDateTextView.setText("Released: " + releaseDate);

        Double voteAverage = cursor.getDouble(COL_MOVIE_VOTE_AVERAGE);
        mVoteAverageTextView.setText("Rating: " + voteAverage);

        mRatingBar.setVisibility(View.VISIBLE);
        mRatingBar.setRating((float) (voteAverage / (MAX_RATING / mRatingBar.getNumStars())));

        int voteCount = cursor.getInt(COL_MOVIE_VOTE_COUNT);
        mVoteCountTextView.setText("Vote Count: " + voteCount);

        Double popularity = cursor.getDouble(COL_MOVIE_POPULARITY);
        mPopularityTextView.setText("Popularity: " + popularity);

        String backdropPath = cursor.getString(COL_MOVIE_BACKDROP_PATH);
        String backdropFullPath = Utility.getFullImagePath(getString(R.string.image_backdrop_w780), backdropPath);
        Picasso.with(getActivity())
                .load(backdropFullPath)
                .into(mBackdropImage);

        String posterPath = cursor.getString(COL_MOVIE_POSTER_PATH);
        String posterFullPath = Utility.getFullImagePath(getString(R.string.image_poster_w185), posterPath);
        Picasso.with(getActivity())
                .load(posterFullPath)
                .into(mPosterImage);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
