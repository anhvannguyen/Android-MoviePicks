package me.anhvannguyen.android.moviepicks.data;

import android.net.Uri;

/**
 * Created by anhvannguyen on 6/12/15.
 */
public class Movie {
    private final String MOVIE_IMAGE_BASE_URL = "http://image.tmdb.org/t/p";

    private int mId;
    private String mTitle;
    private String mOriginalTitle;
    private String mReleaseDate;
    private double mVoteAverage;
    private int mVoteCount;
    private double mPopularity;
    private String mPosterPath;
    private String mBackdropPath;
    private boolean mFavorite;

    public Movie(int id, String title, String originalTitle, String releaseDate, double voteAverage,
                 int voteCount, double popularity, String posterPath, String backdropPath) {
        mId = id;
        mTitle = title;
        mOriginalTitle = originalTitle;
        mReleaseDate = releaseDate;
        mVoteAverage = voteAverage;
        mVoteCount = voteCount;
        mPopularity = popularity;
        mPosterPath = posterPath;
        mBackdropPath = backdropPath;
        mFavorite = false;

    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        mReleaseDate = releaseDate;
    }

    public double getVoteAverage() {
        return mVoteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        mVoteAverage = voteAverage;
    }

    public int getVoteCount() {
        return mVoteCount;
    }

    public void setVoteCount(int voteCount) {
        mVoteCount = voteCount;
    }

    public double getPopularity() {
        return mPopularity;
    }

    public void setPopularity(double popularity) {
        mPopularity = popularity;
    }

    public String getPosterPath() {
        return mPosterPath;
    }

    public void setPosterPath(String posterPath) {
        mPosterPath = posterPath;
    }

    public String getBackdropPath() {
        return mBackdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        mBackdropPath = backdropPath;
    }

    public boolean isFavorite() {
        return mFavorite;
    }

    public void setFavorite(boolean favorite) {
        mFavorite = favorite;
    }

    public String getFullPosterPath(String size) {
        Uri posterPathUri = Uri.parse(MOVIE_IMAGE_BASE_URL).buildUpon()
                .appendPath(size)
                .appendPath(mPosterPath)
                .build();
        return posterPathUri.toString();
    }

    public String getFullBackdropPath(String size) {
        Uri posterPathUri = Uri.parse(MOVIE_IMAGE_BASE_URL).buildUpon()
                .appendPath(size)
                .appendPath(mBackdropPath)
                .build();
        return posterPathUri.toString();
    }
}
