package me.anhvannguyen.android.moviepicks;

import android.content.Intent;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import me.anhvannguyen.android.moviepicks.data.MovieDbContract;
import me.anhvannguyen.android.moviepicks.data.Trailer;


public class DetailActivityFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor>, FetchMovieTrailerTask.finishFetchCallback {
    private final String LOG_TAG = DetailActivityFragment.class.getSimpleName();

    private static final int MOVIE_DETAIL_LOADER = 0;
    private static final int MOVIE_TRAILER_LOADER = 1;
    public static final String DETAIL_URI = "URI";

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

    private static final String[] TRAILER_PROJECTION = {
            MovieDbContract.TrailerEntry.COLUMN_NAME,
            MovieDbContract.TrailerEntry.COLUMN_KEY
    };

    public static final int COL_TRAILER_NAME = 0;
    public static final int COL_TRAILER_KEY = 1;

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
    private LinearLayout mTrailerContainer;

    private FetchMovieTrailerTask.finishFetchCallback mDelegate;

    private Uri mUri;

    public DetailActivityFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        mDelegate = this;

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
        mTrailerContainer = (LinearLayout)rootView.findViewById(R.id.trailer_container);


        mRatingBar.setVisibility(View.INVISIBLE);

        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = arguments.getParcelable(DetailActivityFragment.DETAIL_URI);
        }


        if (mUri != null && savedInstanceState == null) {
            String movieId = MovieDbContract.MovieEntry.getMovieId(mUri);
            new FetchMovieTrailerTask(getActivity(), mDelegate).execute(movieId);
        }
        return rootView;
    }

    @Override
    public void processTrailer() {
        getLoaderManager().restartLoader(MOVIE_TRAILER_LOADER, null, this);
    }

    private void loadTrailers(Cursor cursor) {
        if (cursor == null || cursor.getCount() == 0) {
            return;
        }
            while (cursor.moveToNext()) {
                String trailerName = cursor.getString(COL_TRAILER_NAME);
                final String trailerKey = cursor.getString(COL_TRAILER_KEY);

                Button trailerButton = new Button(getActivity());
                trailerButton.setText(trailerName);
                trailerButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri youtubeUri = Uri.parse(Trailer.YOUTUBE_BASE_URL)
                                .buildUpon()
                                .appendPath(Trailer.YOUTUBE_WATCH_PATH)
                                .appendQueryParameter(Trailer.VIDEO_PARAM, trailerKey)
                                .build();
                        startActivity(new Intent(Intent.ACTION_VIEW, youtubeUri));
                    }
                });
                mTrailerContainer.addView(trailerButton);

            }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(MOVIE_DETAIL_LOADER, null, this);
        getLoaderManager().initLoader(MOVIE_TRAILER_LOADER, null, this);
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
                new FetchMovieTrailerTask(getActivity(), mDelegate).execute(movieId);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        if (i == MOVIE_DETAIL_LOADER) {
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
        } else if (i == MOVIE_TRAILER_LOADER) {
            if (mUri != null) {
                String sortOder = MovieDbContract.TrailerEntry.COLUMN_NAME + " ASC";
                Uri trailerUri = MovieDbContract.TrailerEntry.buildMovieTrailerUri(mUri);

                return new CursorLoader(
                        getActivity(),
                        trailerUri,
                        TRAILER_PROJECTION,
                        null,
                        null,
                        sortOder
                );
            }
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (loader.getId() == MOVIE_DETAIL_LOADER) {
            if (!cursor.moveToFirst()) {
                return;
            }

            final float MAX_RATING = 10.0f;

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
        } else if (loader.getId() == MOVIE_TRAILER_LOADER) {
            if (cursor != null) {
                loadTrailers(cursor);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (loader.getId() == MOVIE_TRAILER_LOADER) {
        }
    }
}
