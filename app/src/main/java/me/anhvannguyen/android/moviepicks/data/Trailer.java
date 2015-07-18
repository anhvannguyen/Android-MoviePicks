package me.anhvannguyen.android.moviepicks.data;

import java.util.Comparator;

public class Trailer {
    public static final String YOUTUBE_BASE_URL = "http://www.youtube.com/";
    public static final String YOUTUBE_WATCH_PATH = "watch";
    public static final String VIDEO_PARAM = "v";

    private int mMovieId;
    private String mMDB_Id;
    private String mKey;
    private String mName;
    private String mSite;
    private String mType;

    public Trailer(int movieId, String mdb_id, String key, String name, String site, String type) {
        mMovieId = movieId;
        mMDB_Id = mdb_id;
        mKey = key;
        mName = name;
        mSite = site;
        mType = type;
    }

    public int getMovieId() {
        return mMovieId;
    }

    public void setMovieId(int movieId) {
        mMovieId = movieId;
    }

    public String getMDB_Id() {
        return mMDB_Id;
    }

    public void setMDB_Id(String MDB_Id) {
        mMDB_Id = MDB_Id;
    }

    public String getKey() {
        return mKey;
    }

    public void setKey(String key) {
        mKey = key;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getSite() {
        return mSite;
    }

    public void setSite(String site) {
        mSite = site;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public static class CompareName implements Comparator<Trailer> {
        @Override
        public int compare(Trailer lhs, Trailer rhs) {
            return lhs.getName().compareTo(rhs.getName());
        }
    }
}
