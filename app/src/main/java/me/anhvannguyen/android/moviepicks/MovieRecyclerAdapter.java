package me.anhvannguyen.android.moviepicks;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by anhvannguyen on 7/1/15.
 */
public class MovieRecyclerAdapter extends RecyclerView.Adapter<MovieRecyclerAdapter.ViewHolder> {

    private Cursor mCursor;

    public class ViewHolder extends RecyclerView.ViewHolder {
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
        }
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item_movie, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

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
