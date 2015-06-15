package me.anhvannguyen.android.moviepicks;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import me.anhvannguyen.android.moviepicks.data.Movie;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {
    private final String LOG_TAG = DetailActivityFragment.class.getSimpleName();

    public static final String EXTRA_MOVIE = "movie_detail";

    private TextView mIdTextView;
    private TextView mTitleTextView;
    private TextView mOriginalTitleTextView;
    private TextView mOverviewTextView;
    private TextView mReleaseDateTextView;
    private TextView mVoteAverageTextView;
    private TextView mVoteCountTextView;
    private TextView mPopularityTextView;
    private TextView mPosterPathTextView;
    private TextView mBackdropPathTextView;
    private TextView mFavoriteTextView;

    public DetailActivityFragment() {
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
        mVoteCountTextView = (TextView)rootView.findViewById(R.id.detail_vote_count_textview);
        mPopularityTextView = (TextView)rootView.findViewById(R.id.detail_popuarity_textview);
        mPosterPathTextView = (TextView)rootView.findViewById(R.id.detail_poster_path_textview);
        mBackdropPathTextView = (TextView)rootView.findViewById(R.id.detail_backdrop_path_textview);
        mFavoriteTextView = (TextView)rootView.findViewById(R.id.detail_favorite_textview);

        Movie movie = getActivity().getIntent().getParcelableExtra(EXTRA_MOVIE);

        mIdTextView.setText("ID: " + movie.getId());
        mTitleTextView.setText("Title: " + movie.getTitle());
        mOriginalTitleTextView.setText("Original Title: " + movie.getOriginalTitle());
        mOverviewTextView.setText("Overview: " + movie.getOverview());
        mReleaseDateTextView.setText("Released:" + movie.getReleaseDate());
        mVoteAverageTextView.setText("Rating: " + movie.getVoteAverage());
        mVoteCountTextView.setText("Vote Count: " + movie.getVoteCount());
        mPopularityTextView.setText("Popularity:" + movie.getPopularity());
        mPosterPathTextView.setText("Poster Image: " + movie.getPosterPath());
        mBackdropPathTextView.setText("Backdrop Image:" + movie.getBackdropPath());
        mFavoriteTextView.setText("Favorite: " + movie.isFavorite());

        return rootView;
    }
}
