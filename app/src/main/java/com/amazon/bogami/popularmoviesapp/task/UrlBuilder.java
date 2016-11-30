package com.amazon.bogami.popularmoviesapp.task;

import com.amazon.bogami.popularmoviesapp.model.SortingOrder;

import java.net.MalformedURLException;
import java.net.URL;

public class UrlBuilder {

    private static final String MOVIE_DB_PREFIX = "https://api.themoviedb.org/3/movie/";

    public static URL getUrl(SortingOrder sortingOrder, String apiKey) {
        URL url = null;
        try {
            url = new URL(MOVIE_DB_PREFIX + getUrlPath(sortingOrder) + "?api_key=" + apiKey);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    private static String getUrlPath(SortingOrder sortingOrder) {
        switch (sortingOrder) {
            case POPULARITY:
                return "popular";
            case TOP_RATED:
                return "top_rated";
            default:
                throw new RuntimeException("Unsupported sorting order");
        }
    }
}
