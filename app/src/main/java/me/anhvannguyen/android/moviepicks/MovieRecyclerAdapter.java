package me.anhvannguyen.android.moviepicks;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieRecyclerAdapter extends RecyclerView.Adapter<MovieRecyclerAdapter.ViewHolder> {

    private Cursor mCursor;
    private Context mContext;
    private MovieAdapterOnClickHandler mClickHandler;


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView mPosterImage;
        TextView mTitleTextView;
        TextView mRatingTextView;
        TextView mReleaseDateTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mPosterImage = (ImageView) itemView.findViewById(R.id.movie_list_poster_image);
            mTitleTextView = (TextView) itemView.findViewById(R.id.movie_list_title_textview);
            mRatingTextView = (TextView) itemView.findViewById(R.id.movie_list_rating_textview);
            mReleaseDateTextView = (TextView) itemView.findViewById(R.id.movie_list_releasedate_textview);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            mClickHandler.onClick(this);
        }
    }

    public static interface MovieAdapterOnClickHandler {
        void onClick(ViewHolder viewHolder);
    }

    public MovieRecyclerAdapter(Context context, MovieAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item_movie, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        mCursor.moveToPosition(i);

        // Get the url of the poster path
        String posterPath = mCursor.getString(MainActivityFragment.COL_MOVIE_POSTER_PATH);
        String fullPosterPath = Utility.getFullImagePath(
                mContext.getString(R.string.image_poster_w154),
                posterPath
        );
        // Set the imageview with the poster image
        Picasso.with(mContext)
                //.setIndicatorsEnabled(true)
                .load(fullPosterPath)
                .into(viewHolder.mPosterImage);

        // Set the title textview
        String title = mCursor.getString(MainActivityFragment.COL_MOVIE_TITLE);
        viewHolder.mTitleTextView.setText(title);

        // Set the rating textview
        Double movieRating = mCursor.getDouble(MainActivityFragment.COL_MOVIE_VOTE_AVERAGE);
        int movieVoteCount = mCursor.getInt(MainActivityFragment.COL_MOVIE_VOTE_COUNT);
        viewHolder.mRatingTextView.setText(mContext.getString(
                R.string.list_rating_format, movieRating, movieVoteCount));

        // Set the date textview
        String releaseDate = mCursor.getString(MainActivityFragment.COL_MOVIE_RELEASE_DATE);
        String releaseYear = releaseDate.substring(0, 4);
        viewHolder.mReleaseDateTextView.setText(releaseYear);

    }

    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    public Cursor getCursor() {
        return mCursor;
    }
}
