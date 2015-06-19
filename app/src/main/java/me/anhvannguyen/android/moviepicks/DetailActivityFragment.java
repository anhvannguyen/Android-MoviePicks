package me.anhvannguyen.android.moviepicks;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
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
    public static final String EXTRA_MOVIE = "movie_detail";
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

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(MOVIE_DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Intent intent = getActivity().getIntent();
        if (intent == null) {
            return null;
        }

        return new CursorLoader(
                getActivity(),
                intent.getData(),
                MOVIEDETAIL_PROJECTION,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (!cursor.moveToFirst()) {
            return;
        }

        mIdTextView = (TextView)getView().findViewById(R.id.detail_id_textview);
        int id = cursor.getInt(COL_MOVIE_ID);
        mIdTextView.setText("ID: " + id);

        mTitleTextView = (TextView)getView().findViewById(R.id.detail_title_textview);
        String title = cursor.getString(COL_MOVIE_TITLE);
        mTitleTextView.setText("Title: " + title);

        mOriginalTitleTextView = (TextView) getView().findViewById(R.id.detail_original_title_textview);
        String originalTitle = cursor.getString(COL_MOVIE_ORIGINAL_TITLE);
        mOriginalTitleTextView.setText("Original Title: " + originalTitle);

        mOverviewTextView = (TextView) getView().findViewById(R.id.detail_overview_textview);
        String overview = cursor.getString(COL_MOVIE_OVERVIEW);
        mOverviewTextView.setText("Overview: " + overview);

        mReleaseDateTextView = (TextView) getView().findViewById(R.id.detail_release_date_textview);
        String releaseDate = cursor.getString(COL_MOVIE_RELEASE_DATE);
        mReleaseDateTextView.setText("Released: " + releaseDate);

        mVoteAverageTextView = (TextView) getView().findViewById(R.id.detail_vote_average_textview);
        Double voteAverage = cursor.getDouble(COL_MOVIE_VOTE_AVERAGE);
        mVoteAverageTextView.setText("Rating: " + voteAverage);

        mRatingBar = (RatingBar) getView().findViewById(R.id.detail_rating_bar);
        mRatingBar.setRating((float) (voteAverage / (MAX_RATING / mRatingBar.getNumStars())));

        mVoteCountTextView = (TextView) getView().findViewById(R.id.detail_vote_count_textview);
        int voteCount = cursor.getInt(COL_MOVIE_VOTE_COUNT);
        mVoteCountTextView.setText("Vote Count: " + voteCount);

        mPopularityTextView = (TextView) getView().findViewById(R.id.detail_popuarity_textview);
        Double popularity = cursor.getDouble(COL_MOVIE_POPULARITY);
        mPopularityTextView.setText("Popularity: " + popularity);

        mBackdropImage = (ImageView) getView().findViewById(R.id.detail_backdrop_imageview);
        String backdropPath = cursor.getString(COL_MOVIE_BACKDROP_PATH);
        String backdropFullPath = Utility.getFullImagePath(getString(R.string.image_backdrop_w780), backdropPath);
        Picasso.with(getActivity())
                .load(backdropFullPath)
                .into(mBackdropImage);

        mPosterImage = (ImageView) getView().findViewById(R.id.detail_poster_imageview);
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
