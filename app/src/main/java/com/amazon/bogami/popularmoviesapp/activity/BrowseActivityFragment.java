package com.amazon.bogami.popularmoviesapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

    private static final String SORTING_ORDER_STATE = "sortingOrderState";

    private View view;

    private SortingOrder sortingOrder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.fragment_browse, container, false);

        sortingOrder = restoreSortingOrder();

        handleSortingOrder(sortingOrder);

        return view;
    }

    private SortingOrder restoreSortingOrder() {
        SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        String storedSortingOrder = preferences.getString(SORTING_ORDER_STATE, SortingOrder.POPULARITY.name());

        return SortingOrder.valueOf(storedSortingOrder);
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

        saveSortingOrder(sortingOrder);

        switch (sortingOrder) {
            case FAVORITES:
                displayFavorites();
                break;
            default:
                new MovieFetcherTask(view, getContext(), getActivity()).execute(sortingOrder);
                break;
        }
    }

    private void saveSortingOrder(SortingOrder sortingOrder) {
        SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString(SORTING_ORDER_STATE, sortingOrder.name());
        edit.commit();
    }

    private void displayFavorites() {

        FavoriteMoviesDAO favoriteMoviesDAO = new FavoriteMoviesDAO(getActivity());
        final List<Movie> movies = favoriteMoviesDAO.getFavoriteMovies();

        String[] moviePosters = new String[movies.size()];
        for (int i = 0; i < movies.size(); i++) {
            moviePosters[i] = "http://image.tmdb.org/t/p/w185/" + movies.get(i).getPosterPath();
        }

        GridView gridview = (GridView) view.findViewById(R.id.gridview);
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
