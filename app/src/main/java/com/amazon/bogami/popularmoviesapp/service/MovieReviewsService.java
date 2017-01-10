package com.amazon.bogami.popularmoviesapp.service;

import android.app.IntentService;
import android.content.Intent;

import com.amazon.bogami.popularmoviesapp.NoNetworkConnectivityException;
import com.amazon.bogami.popularmoviesapp.model.Review;
import com.amazon.bogami.popularmoviesapp.task.UrlBuilder;
import com.amazon.bogami.popularmoviesapp.task.WebResourceDownloader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

import static com.amazon.bogami.popularmoviesapp.activity.DetailActivity.NO_CONNECTIVITY;
import static com.amazon.bogami.popularmoviesapp.activity.DetailActivity.REVIEWS_AVAILABLE;

public class MovieReviewsService extends IntentService {

    public static final String MOVIE_ID_ARG = "movieId";
    public static final String API_KEY_ARG = "apiKey";
    public static final String PARAM_OUT_REVIEWS = "reviews";

    public MovieReviewsService() {
        super("MovieReviewsService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        int movieId = intent.getIntExtra(MOVIE_ID_ARG, -1);
        String apiKey = intent.getStringExtra(API_KEY_ARG);

        URL movieReviewsUrl = UrlBuilder.getMovieReviewsUrl(movieId, apiKey);

        try {
            String responseJson =
                    WebResourceDownloader.downloadResource(getApplicationContext(), movieReviewsUrl);
            ArrayList<Review> reviews =  convertResponse(responseJson);
            broadcastReviews(reviews);
        } catch (NoNetworkConnectivityException e) {
            broadcastNoNetworkConnectivity();
        }
    }

    private void broadcastReviews(ArrayList<Review> reviews) {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(REVIEWS_AVAILABLE);
        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
        broadcastIntent.putParcelableArrayListExtra(PARAM_OUT_REVIEWS, reviews);
        sendBroadcast(broadcastIntent);
    }

    private void broadcastNoNetworkConnectivity() {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(NO_CONNECTIVITY);
        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
        sendBroadcast(broadcastIntent);
    }

    private ArrayList<Review> convertResponse(String responseJson) {
        ArrayList<Review> reviews = new ArrayList<>();
        try {
            JSONObject jObject = new JSONObject(responseJson);
            JSONArray jArray = jObject.getJSONArray("results");
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject resultItem = jArray.getJSONObject(i);
                reviews.add(new Review(
                        resultItem.getString("id"),
                        resultItem.getString("author"),
                        resultItem.getString("content"),
                        resultItem.getString("url")
                ));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return reviews;
    }
}
