package com.amazon.bogami.popularmoviesapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.amazon.bogami.popularmoviesapp.FavoriteMoviesDAO;
import com.amazon.bogami.popularmoviesapp.R;
import com.amazon.bogami.popularmoviesapp.adapter.ImageListAdapter;
import com.amazon.bogami.popularmoviesapp.model.Movie;
import com.amazon.bogami.popularmoviesapp.model.SortingOrder;
import com.amazon.bogami.popularmoviesapp.task.MovieFetcherTask;

import java.util.List;

public class BrowseActivityFragment extends Fragment {

    private static final String STATE_SORTING_ORDER = "sortingOrder";

    private View view;

    private SortingOrder sortingOrder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        super.onCreate(savedInstanceState);System.out.println(this);

        if (savedInstanceState != null) {
            sortingOrder = (SortingOrder) savedInstanceState.getSerializable(STATE_SORTING_ORDER);
        } else {
            sortingOrder = SortingOrder.POPULARITY;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.fragment_browse, container, false);

        handleSortingOrder(sortingOrder);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putSerializable(STATE_SORTING_ORDER, sortingOrder);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_browse, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_item_popular) {
            sortingOrder = SortingOrder.POPULARITY;
            handleSortingOrder(SortingOrder.POPULARITY);
            return true;
        } else if (id == R.id.menu_item_top_rated) {
            sortingOrder = SortingOrder.TOP_RATED;
            handleSortingOrder(SortingOrder.TOP_RATED);
            return true;
        } else if (id == R.id.menu_item_favorites) {
            sortingOrder = SortingOrder.FAVORITES;
            handleSortingOrder(SortingOrder.FAVORITES);
            return true;
        }

        return false;
    }

    private void handleSortingOrder(SortingOrder sortingOrder) {
        switch (sortingOrder) {
            case FAVORITES:
                displayFavorites();
                break;
            default:
                new MovieFetcherTask(view, getContext(), getActivity()).execute(sortingOrder);
                break;
        }
    }

    private void displayFavorites() {

        FavoriteMoviesDAO favoriteMoviesDAO = new FavoriteMoviesDAO(getActivity());
        final List<Movie> movies = favoriteMoviesDAO.getFavoriteMovies();

        System.out.println("Found " + movies.size() + " movies!");

        String[] moviePosters = new String[movies.size()];
        for (int i = 0; i < movies.size(); i++) {
            moviePosters[i] = "http://image.tmdb.org/t/p/w185/" + movies.get(i).getPosterPath();
        }

        GridView gridview = (GridView) getActivity().findViewById(R.id.gridview);
        ImageListAdapter adapter = new ImageListAdapter(getActivity(), moviePosters);
        gridview.setAdapter(adapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), DetailActivity.class);
                intent.putExtra("movie", movies.get(i));
                getActivity().startActivity(intent);
            }
        });
    }
}
