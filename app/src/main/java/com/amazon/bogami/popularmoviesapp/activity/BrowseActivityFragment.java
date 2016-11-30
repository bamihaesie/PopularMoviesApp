package com.amazon.bogami.popularmoviesapp.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.amazon.bogami.popularmoviesapp.task.MovieFetcherTask;
import com.amazon.bogami.popularmoviesapp.R;
import com.amazon.bogami.popularmoviesapp.model.SortingOrder;

public class BrowseActivityFragment extends Fragment {

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.fragment_browse, container, false);

        startMovieFetcher(SortingOrder.POPULARITY);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_browse, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_item_popular) {
            startMovieFetcher(SortingOrder.POPULARITY);
            return true;
        } else if (id == R.id.menu_item_top_rated) {
            startMovieFetcher(SortingOrder.TOP_RATED);
            return true;
        }

        return false;
    }

    private void startMovieFetcher(SortingOrder sortingOrder) {
        new MovieFetcherTask(view, getContext(), getActivity()).execute(sortingOrder);
    }
}
