package me.anhvannguyen.android.moviepicks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import me.anhvannguyen.android.moviepicks.data.Movie;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private final String LOG_TAG = MainActivityFragment.class.getSimpleName();

    private EditText mSearchEditText;
    private ListView mMovieListView;

    protected static MovieArrayAdapter mMovieAdapter;

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

        String[] fakeMovieList = {
                "300 - Rise of an Empire",
                "Jurassic World",
                "The 11th Hour",
                "Guardians of the Galaxy",
                "Transformers: Age of Extinction",
                "Maleficent",
                "X-Men: Days of Future Past",
                "Big Hero 6",
                "Dawn of the Planet of the Apes",
                "The Amazing Spider-Man 2",
                "Godzilla"
        };

        List<Movie> movieList = new ArrayList<Movie>();

        mMovieAdapter = new MovieArrayAdapter(
                getActivity(),
                movieList
        );

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

        mMovieListView = (ListView)rootView.findViewById(R.id.main_movie_listview);
        mMovieListView.setAdapter(mMovieAdapter);

        mMovieListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = (Movie)parent.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(DetailActivityFragment.EXTRA_MOVIE, movie);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshMovieList();
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

    public void refreshMovieList() {
        String prefChoice = Utility.getSortingPreference(getActivity());
        new FetchMovieTask().execute(prefChoice);
    }
}
