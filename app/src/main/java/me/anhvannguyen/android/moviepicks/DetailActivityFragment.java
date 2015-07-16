package me.anhvannguyen.android.moviepicks;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Vector;

import me.anhvannguyen.android.moviepicks.data.MovieDbContract;
import me.anhvannguyen.android.moviepicks.data.Trailer;


public class DetailActivityFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {
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
            MovieDbContract.MovieEntry.COLUMN_RUNTIME,
            MovieDbContract.MovieEntry.COLUMN_STATUS,
            MovieDbContract.MovieEntry.COLUMN_TAGLINE,
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
    public static final int COL_MOVIE_RUNTIME = 10;
    public static final int COL_MOVIE_STATUS = 11;
    public static final int COL_MOVIE_TAGLINE = 12;
    public static final int COL_MOVIE_FAVORITE = 13;

    private static final String[] TRAILER_PROJECTION = {
            MovieDbContract.TrailerEntry.COLUMN_NAME,
            MovieDbContract.TrailerEntry.COLUMN_KEY
    };

    public static final int COL_TRAILER_NAME = 0;
    public static final int COL_TRAILER_KEY = 1;

    private TextView mOverviewTextView;
    private TextView mReleaseDateTextView;
    private TextView mVoteAverageTextView;
//    private RatingBar mRatingBar;
    private TextView mVoteCountTextView;
    private ImageView mBackdropImage;
    private ImageView mPosterImage;
    private TextView mRuntimeTextView;
    private TextView mStatusTextView;
    private TextView mTaglineTextView;
    private LinearLayout mTrailerContainer;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;

    private Uri mUri;

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        mOverviewTextView = (TextView) rootView.findViewById(R.id.detail_overview_textview);
        mReleaseDateTextView = (TextView) rootView.findViewById(R.id.detail_release_date_textview);
        mVoteAverageTextView = (TextView) rootView.findViewById(R.id.detail_vote_average_textview);
//        mRatingBar = (RatingBar) rootView.findViewById(R.id.detail_rating_bar);
        mVoteCountTextView = (TextView) rootView.findViewById(R.id.detail_vote_count_textview);
        mBackdropImage = (ImageView) rootView.findViewById(R.id.detail_backdrop_imageview);
        mPosterImage = (ImageView) rootView.findViewById(R.id.detail_poster_imageview);
        mRuntimeTextView = (TextView) rootView.findViewById(R.id.detail_runtime_textview);
        mStatusTextView = (TextView) rootView.findViewById(R.id.detail_status_textview);
        mTaglineTextView = (TextView) rootView.findViewById(R.id.detail_tagline_textview);
        mTrailerContainer = (LinearLayout) rootView.findViewById(R.id.trailer_container);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) rootView.findViewById(R.id.collapsing_toolbar);

//        mRatingBar.setVisibility(View.INVISIBLE);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = arguments.getParcelable(DetailActivityFragment.DETAIL_URI);
        }

        return rootView;
    }

    private void fetchTrailer() {
        if (mUri != null && mTrailerContainer.getChildCount() == 0) {

            String movieId = MovieDbContract.MovieEntry.getMovieId(mUri);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Utility.getTrailerUrl(movieId), null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            if (mTrailerContainer.getChildCount() != 0) {
                                return;
                            }
                            final String TRAILER_TYPE = "Trailer";
                            final String TRAILER_SITE = "YouTube";

                            // These are the names of the JSON objects that need to be extracted.
                            final String MDB_MOVIE_ID = "id";                   // int
                            final String MDB_RESULT = "results";                // result array
                            final String MDB_TRAILER_ID = "id";                 // uuid
                            final String MDB_KEY = "key";                       // String
                            final String MDB_NAME = "name";                     // String
                            final String MDB_SITE = "site";                     // String
                            final String MDB_TYPE = "type";                     // String

                            try {
                                int id = response.getInt(MDB_MOVIE_ID);
                                JSONArray trailerArray = response.getJSONArray(MDB_RESULT);

                                int trailerArrayCount = trailerArray.length();

                                Vector<ContentValues> cVVector = new Vector<ContentValues>(trailerArrayCount);

                                for (int i = 0; i < trailerArrayCount; i++) {
                                    JSONObject movieObject = trailerArray.getJSONObject(i);

                                    final String trailerID = movieObject.getString(MDB_TRAILER_ID);
                                    final String key = movieObject.getString(MDB_KEY);
                                    final String name = movieObject.getString(MDB_NAME);
                                    final String site = movieObject.getString(MDB_SITE);
                                    final String type = movieObject.getString(MDB_TYPE);

                                    // cache data
                                    if (site.equals(TRAILER_SITE) && type.equals(TRAILER_TYPE)) {
                                        ContentValues trailerValue = new ContentValues();

                                        trailerValue.put(MovieDbContract.TrailerEntry.COLUMN_MDB_ID, id);
                                        trailerValue.put(MovieDbContract.TrailerEntry.COLUMN_TRAILER_ID, trailerID);
                                        trailerValue.put(MovieDbContract.TrailerEntry.COLUMN_KEY, key);
                                        trailerValue.put(MovieDbContract.TrailerEntry.COLUMN_NAME, name);
                                        trailerValue.put(MovieDbContract.TrailerEntry.COLUMN_SITE, site);
                                        trailerValue.put(MovieDbContract.TrailerEntry.COLUMN_TYPE, type);

                                        cVVector.add(trailerValue);
                                    }

                                }

                                if (cVVector.size() > 0) {
                                    ContentValues[] contentValues = new ContentValues[cVVector.size()];
                                    cVVector.toArray(contentValues);
                                    getActivity().getContentResolver().bulkInsert(MovieDbContract.TrailerEntry.CONTENT_URI, contentValues);
                                }
                                restartLoader(MOVIE_TRAILER_LOADER);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    });

            VolleySingleton.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest);
        }
    }

    private void fetchMovieDetail() {
        String movieId = MovieDbContract.MovieEntry.getMovieId(mUri);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Utility.getDetailUrl(movieId), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        final String MDB_ID = "id";                 // int
                        final String MDB_RUNTIME = "runtime";       // int
                        final String MDB_HOMEPAGE = "homepage";     // String
                        final String MDB_STATUS = "status";         // String
                        final String MDB_TAGLINE = "tagline";       // String

                        try {
                            int id = response.getInt(MDB_ID);
                            int runtime = response.getInt(MDB_RUNTIME);
                            String homepage = response.getString(MDB_HOMEPAGE);
                            String status = response.getString(MDB_STATUS);
                            String tagline = response.getString(MDB_TAGLINE);

                            mRuntimeTextView.setText(getString(R.string.detail_runtime_format, runtime));
                            mStatusTextView.setText(status);
                            mTaglineTextView.setText(tagline);

                            // cache data
                            ContentValues value = new ContentValues();
                            value.put(MovieDbContract.MovieEntry.COLUMN_RUNTIME, runtime);
                            value.put(MovieDbContract.MovieEntry.COLUMN_HOMEPAGE, homepage);
                            value.put(MovieDbContract.MovieEntry.COLUMN_STATUS, status);
                            value.put(MovieDbContract.MovieEntry.COLUMN_TAGLINE, tagline);

                            getActivity().getContentResolver().update(
                                    MovieDbContract.MovieEntry.CONTENT_URI,
                                    value,
                                    MovieDbContract.MovieEntry._ID + " = ?",
                                    new String[]{String.valueOf(id)}
                            );
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest);
    }

    private void restartLoader(int loader) {
        getLoaderManager().restartLoader(loader, null, this);
    }

    private void loadTrailers(Cursor cursor) {
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

            String originalTitle = cursor.getString(COL_MOVIE_ORIGINAL_TITLE);
//            mOriginalTitleTextView.setText("Original Title: " + originalTitle);

            String overview = cursor.getString(COL_MOVIE_OVERVIEW);
            mOverviewTextView.setText(overview);

            String releaseDate = cursor.getString(COL_MOVIE_RELEASE_DATE);
            mReleaseDateTextView.setText(releaseDate);

            Double voteAverage = cursor.getDouble(COL_MOVIE_VOTE_AVERAGE);
            mVoteAverageTextView.setText(getString(R.string.detail_score_format, voteAverage));

//            mRatingBar.setVisibility(View.VISIBLE);
//            mRatingBar.setRating((float) (voteAverage / (MAX_RATING / mRatingBar.getNumStars())));

            int voteCount = cursor.getInt(COL_MOVIE_VOTE_COUNT);
            mVoteCountTextView.setText(String.valueOf(voteCount));

            if (!cursor.isNull(COL_MOVIE_RUNTIME)) {

                int runtime = cursor.getInt(COL_MOVIE_RUNTIME);
                mRuntimeTextView.setText(getString(R.string.detail_runtime_format, runtime));

                String status = cursor.getString(COL_MOVIE_STATUS);
                mStatusTextView.setText(status);

                String tagline = cursor.getString(COL_MOVIE_TAGLINE);
                mTaglineTextView.setText(tagline);

            } else {
                fetchMovieDetail();
            }

            String backdropPath = cursor.getString(COL_MOVIE_BACKDROP_PATH);
            String backdropFullPath = Utility.getFullImagePath(getString(R.string.image_backdrop_w780), backdropPath);
            Picasso.with(getActivity())
                    .load(backdropFullPath)
                    .into(mBackdropImage);

            String posterPath = cursor.getString(COL_MOVIE_POSTER_PATH);
            String posterFullPath = Utility.getFullImagePath(getString(R.string.image_poster_w342), posterPath);
            Picasso.with(getActivity())
                    .load(posterFullPath)
                    .into(mPosterImage);


            mCollapsingToolbarLayout.setTitle(originalTitle);
            mCollapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

        } else if (loader.getId() == MOVIE_TRAILER_LOADER) {
            if (cursor != null) {
                mTrailerContainer.removeAllViews();
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
            if (mTrailerContainer.getChildCount() <= 0) {
                fetchTrailer();
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (loader.getId() == MOVIE_TRAILER_LOADER) {
        }
    }
}
