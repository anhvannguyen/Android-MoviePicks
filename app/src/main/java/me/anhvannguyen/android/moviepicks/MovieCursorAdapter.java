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


public class MovieCursorAdapter extends CursorAdapter {
    private final String LOG_TAG = MovieCursorAdapter.class.getSimpleName();


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
        String posterPath = cursor.getString(MainActivityFragment.COL_MOVIE_POSTER_PATH);
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
        String title = cursor.getString(MainActivityFragment.COL_MOVIE_TITLE);
        viewHolder.mTitleTextView.setText(title);

        // Set the rating textview
        Double movieRating = cursor.getDouble(MainActivityFragment.COL_MOVIE_VOTE_AVERAGE);
        int movieVoteCount = cursor.getInt(MainActivityFragment.COL_MOVIE_VOTE_COUNT);
        viewHolder.mRatingTextView.setText("Rating: " + movieRating +
                " (" + movieVoteCount + ")");

        // Set the date textview
        String releaseDate = cursor.getString(MainActivityFragment.COL_MOVIE_RELEASE_DATE);
        viewHolder.mReleaseDateTextView.setText(releaseDate);
    }

    // Viewholder to cache the children view
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
