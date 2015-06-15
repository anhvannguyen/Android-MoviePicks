package me.anhvannguyen.android.moviepicks.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Comparator;

/**
 * Created by anhvannguyen on 6/12/15.
 */
public class Movie implements Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mTitle);
        dest.writeString(mOriginalTitle);
        dest.writeString(mOverview);
        dest.writeString(mReleaseDate);
        dest.writeDouble(mVoteAverage);
        dest.writeInt(mVoteCount);
        dest.writeDouble(mPopularity);
        dest.writeString(mPosterPath);
        dest.writeString(mBackdropPath);
        dest.writeInt(mFavorite ? 1 : 0);  // boolean

    }

    // Used to recreate the object
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public Movie(Parcel parcel) {
        mId = parcel.readInt();
        mTitle = parcel.readString();
        mOriginalTitle = parcel.readString();
        mOverview = parcel.readString();
        mReleaseDate = parcel.readString();
        mVoteAverage = parcel.readDouble();
        mVoteCount = parcel.readInt();
        mPopularity = parcel.readDouble();
        mPosterPath = parcel.readString();
        mBackdropPath = parcel.readString();
        mFavorite = parcel.readInt() != 0;
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
                return -1;
            } else if (lhs.getVoteAverage() > rhs.getVoteAverage()) {
                return 1;
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
                return -1;
            } else if (lhs.getPopularity() > rhs.getPopularity()) {
                return 1;
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
