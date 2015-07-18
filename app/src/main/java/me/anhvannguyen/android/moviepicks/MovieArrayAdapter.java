package me.anhvannguyen.android.moviepicks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import me.anhvannguyen.android.moviepicks.data.Movie;

public class MovieArrayAdapter extends ArrayAdapter<Movie> {
    private final String LOG_TAG = MovieArrayAdapter.class.getSimpleName();

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

        // Set indicators to see the location of the image source
        Picasso.with(mContext).setIndicatorsEnabled(true);

        // Poster image
        String posterSize = mContext.getResources().getString(R.string.image_poster_w154);
        String posterPath = movie.getFullPosterPath(posterSize);
        Picasso.with(mContext)
                //.setIndicatorsEnabled(true)
                .load(posterPath)
                .error(R.mipmap.ic_launcher)        // TODO: Find better error/placeholder image
                .into(viewHolder.mPosterImage);

        // Set the title textview
        viewHolder.mTitleTextView.setText(movie.getTitle());

        // Set the rating textview
        // TODO: fix string formatting
        viewHolder.mRatingTextView.setText("Rating: " + movie.getVoteAverage() +
                " (" + movie.getVoteCount() + ")");

        // Set the date textview
        viewHolder.mReleaseDateTextView.setText(movie.getReleaseDate());
        return view;
    }

    // ViewHolder to cache children views.
    // Reduce the findViewById call when recycling the view.
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
