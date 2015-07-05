package me.anhvannguyen.android.moviepicks;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import me.anhvannguyen.android.moviepicks.data.Movie;
import me.anhvannguyen.android.moviepicks.data.MovieDbContract;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private final String LOG_TAG = MainActivityFragment.class.getSimpleName();

    private static final int MOVIELIST_LOADER = 0;

//    private EditText mSearchEditText;
//    private ListView mMovieListView;
//    private TextView mEmptyListText;
    private RecyclerView mMovieRecyclerView;

//    private MovieArrayAdapter mMovieAdapter;
    private MovieCursorAdapter mMovieCursorAdapter;
    private MovieRecyclerAdapter mMovieRecyclerAdapter;

    private static final String[] MOVIELIST_PROJECTION = {
            MovieDbContract.MovieEntry._ID,
            MovieDbContract.MovieEntry.COLUMN_TITLE,
            MovieDbContract.MovieEntry.COLUMN_ORIGINAL_TITLE,
            MovieDbContract.MovieEntry.COLUMN_RELEASE_DATE,
            MovieDbContract.MovieEntry.COLUMN_VOTE_AVERAGE,
            MovieDbContract.MovieEntry.COLUMN_VOTE_COUNT,
            MovieDbContract.MovieEntry.COLUMN_POPULARITY,
            MovieDbContract.MovieEntry.COLUMN_POSTER_PATH,
            MovieDbContract.MovieEntry.COLUMN_FAVORITE
    };

    public static final int COL_MOVIE_ID = 0;
    public static final int COL_MOVIE_TITLE = 1;
    public static final int COL_MOVIE_ORIGINAL_TITLE = 2;
    public static final int COL_MOVIE_RELEASE_DATE = 3;
    public static final int COL_MOVIE_VOTE_AVERAGE = 4;
    public static final int COL_MOVIE_VOTE_COUNT = 5;
    public static final int COL_MOVIE_POPULARITY = 6;
    public static final int COL_MOVIE_POSTER_PATH = 7;
    public static final int COL_MOVIE_FAVORITE = 8;

    // callback interface for host activity
    public interface ItemSelectedCallback {
        public void onItemSelected(Uri movieUri);
    }

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

//        mEmptyListText = (TextView) rootView.findViewById(android.R.id.empty);
//        if (!Utility.isNetworkAvailable(getActivity())) {
//            mEmptyListText.setText(getString(R.string.empty_listview_network));
//        }

//        List<Movie> movieList = new ArrayList<Movie>();
//
//        mMovieAdapter = new MovieArrayAdapter(
//                getActivity(),
//                movieList
//        );
        mMovieCursorAdapter = new MovieCursorAdapter(
                getActivity(),
                null,
                0
        );
        mMovieRecyclerAdapter = new MovieRecyclerAdapter(getActivity(), new MovieRecyclerAdapter.MovieAdapterOnClickHandler() {
            @Override
            public void onClick(MovieRecyclerAdapter.ViewHolder viewHolder) {
                if (mMovieRecyclerAdapter.getCursor() != null) {
                    Uri movieUri = MovieDbContract.MovieEntry.buildMovieUri(mMovieRecyclerAdapter.getCursor().getLong(COL_MOVIE_ID));
                    ((ItemSelectedCallback)getActivity()).onItemSelected(movieUri);
                }
            }
        });

//        mSearchEditText = (EditText)rootView.findViewById(R.id.movie_search_edittext);
//        mSearchEditText.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (event.getAction() == KeyEvent.ACTION_DOWN) {
//                    if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {
//                        Toast.makeText(getActivity(), "Not working yet!", Toast.LENGTH_SHORT).show();
//                        mSearchEditText.setText("");
//                        return true;
//                    }
//                }
//                return false;
//            }
//        });

//        mMovieListView = (ListView)rootView.findViewById(R.id.main_movie_listview);
//        mMovieListView.setEmptyView(rootView.findViewById(android.R.id.empty));
//        mMovieListView.setAdapter(mMovieCursorAdapter);
//
//        mMovieListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
//                if (cursor != null) {
//                    Uri movieUri = MovieDbContract.MovieEntry.buildMovieUri(cursor.getLong(COL_MOVIE_ID));
//                    String movieId = MovieDbContract.MovieEntry.getMovieId(movieUri);
//                    //new FetchMovieTrailerTask(getActivity()).execute(movieId);
//                    ((ItemSelectedCallback)getActivity()).onItemSelected(movieUri);
//                }
//
//            }
//        });

        mMovieRecyclerView = (RecyclerView) rootView.findViewById(R.id.main_movie_recyclerview);
        // improve performance if the content of the layout
        // does not change the size of the RecyclerView
        mMovieRecyclerView.setHasFixedSize(true);

        mMovieRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mMovieRecyclerView.setAdapter(mMovieRecyclerAdapter);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(MOVIELIST_LOADER, null, this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(MOVIELIST_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            refreshMovieList();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String sortOrder;

        // Get the sort preference and set the sort order in the loader
        int sortType = Utility.getSortingPreference(getActivity());
        switch (sortType) {
            case Movie.SORT_POPULARITY:
                // descending by popularity
                sortOrder = MovieDbContract.MovieEntry.COLUMN_POPULARITY + " DESC";
                break;
            case Movie.SORT_VOTE_AVERAGE:
                // descending by rating
                sortOrder = MovieDbContract.MovieEntry.COLUMN_VOTE_AVERAGE + " DESC";
                break;
            default:
                sortOrder = null;
        }

        return new CursorLoader(
                getActivity(),
                MovieDbContract.MovieEntry.CONTENT_URI,
                MOVIELIST_PROJECTION,
                null,
                null,
                sortOrder
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
//        mMovieCursorAdapter.swapCursor(cursor);
        mMovieRecyclerAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
//        mMovieCursorAdapter.swapCursor(null);
        mMovieRecyclerAdapter.swapCursor(null);
    }

    public void refreshMovieList() {
        new FetchMovieTask(getActivity()).execute();

    }
}
