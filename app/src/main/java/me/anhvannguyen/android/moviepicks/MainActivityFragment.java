package me.anhvannguyen.android.moviepicks;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import me.anhvannguyen.android.moviepicks.data.Movie;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private EditText mSearchEditText;
    private ListView mMovieListView;

    protected static MovieArrayAdapter mMovieAdapter;

    public MainActivityFragment() {
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

        return rootView;
    }

}
