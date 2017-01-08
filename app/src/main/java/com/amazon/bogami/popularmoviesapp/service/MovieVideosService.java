package com.amazon.bogami.popularmoviesapp.service;

import android.app.IntentService;
import android.content.Intent;

import com.amazon.bogami.popularmoviesapp.model.Trailer;
import com.amazon.bogami.popularmoviesapp.task.UrlBuilder;
import com.amazon.bogami.popularmoviesapp.task.WebResourceDownloader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

import static com.amazon.bogami.popularmoviesapp.activity.DetailActivity.TRAILERS_AVAILABLE;

public class MovieVideosService extends IntentService {

    public static final String MOVIE_ID_ARG = "movieId";
    public static final String API_KEY_ARG = "apiKey";
    public static final String PARAM_OUT_VIDEOS = "videos";

    public MovieVideosService() {
        super("MovieVideosService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        int movieId = intent.getIntExtra(MOVIE_ID_ARG, -1);
        String apiKey = intent.getStringExtra(API_KEY_ARG);

        URL movieReviewsUrl = UrlBuilder.getMovieVideosUrl(movieId, apiKey);
        String responseJson = WebResourceDownloader.downloadResource(movieReviewsUrl);

        ArrayList<Trailer> reviews =  convertResponse(responseJson);

        broadcastReviews(reviews);
    }

    private void broadcastReviews(ArrayList<Trailer> reviews) {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(TRAILERS_AVAILABLE);
        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
        broadcastIntent.putParcelableArrayListExtra(PARAM_OUT_VIDEOS, reviews);
        sendBroadcast(broadcastIntent);
    }

    private ArrayList<Trailer> convertResponse(String responseJson) {
        ArrayList<Trailer> trailers = new ArrayList<>();
        try {
            JSONObject jObject = new JSONObject(responseJson);
            JSONArray jArray = jObject.getJSONArray("results");
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject resultItem = jArray.getJSONObject(i);
                trailers.add(new Trailer(
                        resultItem.getString("id"),
                        resultItem.getString("key"),
                        resultItem.getString("name"),
                        resultItem.getString("site"),
                        resultItem.getInt("size"),
                        resultItem.getString("type")
                ));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return trailers;
    }
}
