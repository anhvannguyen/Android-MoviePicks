package me.anhvannguyen.android.moviepicks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import me.anhvannguyen.android.moviepicks.data.Movie;

/**
 * Created by anhvannguyen on 6/12/15.
 */
public class MovieArrayAdapter extends ArrayAdapter<Movie> {
    Context mContext;
    public MovieArrayAdapter(Context context, List<Movie> objects) {
        super(context, R.layout.list_item_movie, objects);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for the position
        Movie movie = getItem(position);

        View view = convertView;

        ViewHolder viewHolder;

        // Check if an existing view is being reused, if not, inflate the view
        if (view == null) {
            view = LayoutInflater.from(mContext)
                    .inflate(R.layout.list_item_movie, parent, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)view.getTag();
        }

        // Poster image
        String posterSize = mContext.getResources().getString(R.string.image_poster_w185);
        String posterPath = movie.getFullPosterPath(posterSize);
        // TODO: set image for poster
        viewHolder.mPosterImage.setImageResource(R.mipmap.ic_launcher);

        // Set the title textview
        viewHolder.mTitleTextView.setText(movie.getTitle());

        // Set the rating textview
        // TODO: fix string formatting
        viewHolder.mRatingTextView.setText("Rating: " + movie.getVoteAverage() +
                " (" + movie.getVoteCount() + ")");
        return view;
    }

    // ViewHolder to cache children views.
    // Reduce the findViewById call when recycling the view.
    public static class ViewHolder {
        ImageView mPosterImage;
        TextView mTitleTextView;
        TextView mRatingTextView;

        public ViewHolder(View view) {
            mPosterImage = (ImageView)view.findViewById(R.id.movie_list_poster_image);
            mTitleTextView = (TextView)view.findViewById(R.id.movie_list_title_textview);
            mRatingTextView = (TextView)view.findViewById(R.id.movie_list_rating_textview);
        }
    }
}
