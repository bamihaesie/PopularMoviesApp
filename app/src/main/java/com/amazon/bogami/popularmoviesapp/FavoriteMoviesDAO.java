package com.amazon.bogami.popularmoviesapp;

import android.app.Activity;
import android.content.SharedPreferences;

import com.amazon.bogami.popularmoviesapp.model.Movie;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FavoriteMoviesDAO {

    private static final String PREF_NAME = "movies";

    private Activity activity;
    private Gson gson;

    public FavoriteMoviesDAO(Activity activity) {
        this.activity = activity;
        gson = new Gson();
    }

    public void addMovieToFavorites(Movie movie) {
        SharedPreferences sharedPref = activity.getSharedPreferences(PREF_NAME, 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Integer.toString(movie.getId()), serializeMovie(movie));
        editor.commit();
    }

    public List<Movie> getFavoriteMovies() {
        List<Movie> movieList = new ArrayList<>();
        SharedPreferences sharedPreferences = activity.getSharedPreferences(PREF_NAME, 0);
        for (String movieString : (Collection<String>)sharedPreferences.getAll().values()) {
            movieList.add(deserializeMovie(movieString));
        }
        return movieList;
    }

    private String serializeMovie(Movie movie) {
        return gson.toJson(movie);
    }

    private Movie deserializeMovie(String movieString) {
        return gson.fromJson(movieString, Movie.class);
    }
}
