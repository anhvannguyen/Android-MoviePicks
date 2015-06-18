package me.anhvannguyen.android.moviepicks;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by anhvannguyen on 6/18/15.
 */
public class MovieCursorAdapter extends CursorAdapter {


    public MovieCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_movie, parent, false);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

    }

    public static class ViewHolder {
        ImageView mPosterImage;
        TextView mTitleTextView;
        TextView mRatingTextView;
        TextView mReleaseDateTextView;

        public ViewHolder(View view) {
            mPosterImage = (ImageView)view.findViewById(R.id.movie_list_poster_image);
            mTitleTextView = (TextView)view.findViewById(R.id.movie_list_title_textview);
            mRatingTextView = (TextView)view.findViewById(R.id.movie_list_rating_textview);
            mReleaseDateTextView = (TextView)view.findViewById(R.id.movie_list_releasedate_textview);
        }
    }
}
