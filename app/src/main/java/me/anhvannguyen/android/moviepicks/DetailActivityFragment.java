package me.anhvannguyen.android.moviepicks;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import me.anhvannguyen.android.moviepicks.data.Movie;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {
    private final String LOG_TAG = DetailActivityFragment.class.getSimpleName();

    public static final String EXTRA_MOVIE = "movie_detail";
    private final float MAX_RATING = 10.0f;

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

        Movie movie = getActivity().getIntent().getParcelableExtra(EXTRA_MOVIE);

        mIdTextView.setText("ID: " + movie.getId());
        mTitleTextView.setText("Title: " + movie.getTitle());
        mOriginalTitleTextView.setText("Original Title: " + movie.getOriginalTitle());
        mOverviewTextView.setText("Overview: " + movie.getOverview());
        mReleaseDateTextView.setText("Released: " + movie.getReleaseDate());
        mVoteAverageTextView.setText("Rating: " + movie.getVoteAverage());
        mRatingBar.setRating((float)(movie.getVoteAverage() / (MAX_RATING / mRatingBar.getNumStars())));
        mVoteCountTextView.setText("Vote Count: " + movie.getVoteCount());
        mPopularityTextView.setText("Popularity: " + movie.getPopularity());

        String backdropFullPath = movie.getFullBackdropPath(getString(R.string.image_backdrop_w780));
        Picasso.with(getActivity())
                .load(backdropFullPath)
                .into(mBackdropImage);

        String posterFullPath = movie.getFullPosterPath(getString(R.string.image_poster_w185));
        Picasso.with(getActivity())
                .load(posterFullPath)
                .into(mPosterImage);

        return rootView;
    }
}
