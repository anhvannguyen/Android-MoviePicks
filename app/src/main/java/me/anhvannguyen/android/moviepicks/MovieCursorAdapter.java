package me.anhvannguyen.android.moviepicks;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import me.anhvannguyen.android.moviepicks.data.MovieDbContract;

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

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        // Get the url of the poster path
        int posterIndex = cursor.getColumnIndex(MovieDbContract.MovieEntry.COLUMN_POSTER_PATH);
        String posterPath = cursor.getString(posterIndex);
        String fullPosterPath = Utility.getFullImagePath(
                context.getString(R.string.image_poster_w154),
                posterPath
        );
        // Set the imageview with the poster image
        Picasso.with(context)
                //.setIndicatorsEnabled(true)
                .load(fullPosterPath)
                //.error(R.mipmap.ic_launcher)
                .into(viewHolder.mPosterImage);

        // Set the title textview
        int titleIndex = cursor.getColumnIndex(MovieDbContract.MovieEntry.COLUMN_TITLE);
        viewHolder.mTitleTextView.setText(cursor.getString(titleIndex));

        // Set the rating textview
        int voteAverageIndex = cursor.getColumnIndex(MovieDbContract.MovieEntry.COLUMN_VOTE_AVERAGE);
        int voteCountIndex = cursor.getColumnIndex(MovieDbContract.MovieEntry.COLUMN_VOTE_COUNT);
        Double movieRating = cursor.getDouble(voteAverageIndex);
        int movieVoteCount = cursor.getInt(voteCountIndex);
        viewHolder.mRatingTextView.setText("Rating: " + movieRating +
                " (" + movieVoteCount + ")");

        // Set the date textview
        int releaseDateIndex = cursor.getColumnIndex(MovieDbContract.MovieEntry.COLUMN_RELEASE_DATE);
        viewHolder.mReleaseDateTextView.setText(cursor.getString(releaseDateIndex));
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
