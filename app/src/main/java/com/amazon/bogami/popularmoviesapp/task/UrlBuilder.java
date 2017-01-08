package com.amazon.bogami.popularmoviesapp.task;

import java.net.MalformedURLException;
import java.net.URL;

public class UrlBuilder {

    private static final String MOVIE_DB_PREFIX = "https://api.themoviedb.org/3/movie/";

    public static URL getPopularMoviesUrl(String apiKey) {
        return getUrl("popular", apiKey);
    }

    public static URL getTopRatedMoviesUrl(String apiKey) {
        return getUrl("top_rated", apiKey);
    }

    public static URL getMovieReviewsUrl(int movieId, String apiKey) {
        return getUrl(movieId + "/reviews", apiKey);
    }

    public static URL getMovieVideosUrl(int movieId, String apiKey) {
        return getUrl(movieId + "/videos", apiKey);
    }

    private static URL getUrl(String suffix, String apiKey) {
        URL url = null;
        try {
            url = new URL(MOVIE_DB_PREFIX + suffix + "?api_key=" + apiKey);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }
}
