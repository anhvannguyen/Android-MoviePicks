package me.anhvannguyen.android.moviepicks.data;

import java.util.Comparator;


public class Movie {
    public static final int SORT_POPULARITY = 1;
    public static final int SORT_VOTE_AVERAGE = 2;

    private final String MOVIE_IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";

    private int mId;
    private String mTitle;
    private String mOriginalTitle;
    private String mOverview;
    private String mReleaseDate;
    private double mVoteAverage;
    private int mVoteCount;
    private double mPopularity;
    private String mPosterPath;
    private String mBackdropPath;
    private int mRuntime;
    private String mStatus;
    private String mTagline;

    private boolean mFavorite;

    public Movie(int id, String title, String originalTitle, String overview, String releaseDate,
                 double voteAverage, int voteCount, double popularity, String posterPath, String backdropPath) {
        mId = id;
        mTitle = title;
        mOriginalTitle = originalTitle;
        mOverview = overview;
        mReleaseDate = releaseDate;
        mVoteAverage = voteAverage;
        mVoteCount = voteCount;
        mPopularity = popularity;
        mPosterPath = posterPath;
        mBackdropPath = backdropPath;
        mFavorite = false;

    }

    public Movie(int id, int runtime, String status, String tagline) {
        mId = id;
        mRuntime = runtime;
        mStatus = status;
        mTagline = tagline;
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

    public String getOriginalTitle() {
        return mOriginalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        mOriginalTitle = originalTitle;
    }

    public String getOverview() {
        return mOverview;
    }

    public void setOverview(String overview) {
        mOverview = overview;
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

    public int getRuntime() {
        return mRuntime;
    }

    public void setRuntime(int runtime) {
        mRuntime = runtime;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

    public String getTagline() {
        return mTagline;
    }

    public void setTagline(String tagline) {
        mTagline = tagline;
    }

    public String getFullPosterPath(String size) {
        return MOVIE_IMAGE_BASE_URL + size + mPosterPath;
    }

    public String getFullBackdropPath(String size) {
        return MOVIE_IMAGE_BASE_URL + size + mBackdropPath;
    }

    public static class OrderByTitle implements Comparator<Movie> {
        @Override
        public int compare(Movie lhs, Movie rhs) {
            return lhs.getTitle().compareTo(rhs.getTitle());
        }
    }

    public static class OrderByDate implements Comparator<Movie> {
        @Override
        public int compare(Movie lhs, Movie rhs) {
            return lhs.getReleaseDate().compareTo(rhs.getReleaseDate());
        }
    }

    public static class OrderByRating implements Comparator<Movie> {
        @Override
        public int compare(Movie lhs, Movie rhs) {
            if (lhs.getVoteAverage() < rhs.getVoteAverage()) {
                return 1;
            } else if (lhs.getVoteAverage() > rhs.getVoteAverage()) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    public static class OrderByVoteCount implements Comparator<Movie> {
        @Override
        public int compare(Movie lhs, Movie rhs) {
            return lhs.getVoteCount() - rhs.getVoteCount();
        }
    }

    public static class OrderByPopularity implements Comparator<Movie> {
        @Override
        public int compare(Movie lhs, Movie rhs) {
            if (lhs.getPopularity() < rhs.getPopularity()) {
                return 1;
            } else if (lhs.getPopularity() > rhs.getPopularity()) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    @Override
    public String toString() {
        return mTitle + " - " + mVoteAverage + "(" + mVoteCount + ")";
    }
}
